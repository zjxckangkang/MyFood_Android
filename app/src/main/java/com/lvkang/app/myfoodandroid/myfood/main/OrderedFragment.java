package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lvkang.app.myfoodandroid.library.ILoadingLayout;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase.OnRefreshListener;
import com.lvkang.app.myfoodandroid.library.PullToRefreshListView;
import com.lvkang.app.myfoodandroid.adapter.OrderedFoodListAdapter;
import com.lvkang.app.myfoodandroid.myfood.http.HttpService;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFoods;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFoodsList;
import com.lvkang.app.myfoodandroid.myfood.utils.DeviceId;
import com.lvkang.app.myfoodandroid.myfood.utils.MyToast;
import com.lvkang.myfoodtoandroid.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderedFragment extends Fragment {

	private Context mContext;
	private View mMainView;
	MyHandler mHandler;
	int addHeight;
	View lastView = null;
	View thisView = null;
	View footView = null;
	View includenternetFailedView = null;
	TextView footTextView = null;
	private boolean isUpdate = false;
	PullToRefreshListView orderListView;
	OrderedFoodListAdapter adapterOrderedFood;
	private ILoadingLayout startLabels;

	public static getOrderedFoodListRunnable getOrderedFoodListrunnable;
	private static final int OFFSET = 20;
	private static final long maxOrderId = 999999999999L;

	List<OrderFoods> orderFoodsList = new ArrayList<OrderFoods>();

	private static List<OrderFoods> parseStringToOrderFoods(String jsonString) {
		OrderFoodsList orderFoodsList = new OrderFoodsList();
		try {
			orderFoodsList = JSON.parseObject(jsonString, OrderFoodsList.class);

			return orderFoodsList.getOrderFoodsList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private void setStartLableTime() {
		String label = DateUtils.formatDateTime(
				mContext.getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		startLabels.setLastUpdatedLabel("更新于:" + label);
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x122:
				if (msg.obj != null) {
					// 首次更新成功
					List<OrderFoods> newOrderFoodsList = parseStringToOrderFoods(msg.obj
							.toString());
					orderFoodsList.clear();
					if (newOrderFoodsList != null) {
						orderFoodsList.addAll(newOrderFoodsList);

						includenternetFailedView.setVisibility(View.GONE);
						adapterOrderedFood.changeSelectedPosition();
						orderListView.getRefreshableView().setSelection(0);
						adapterOrderedFood.notifyDataSetChanged();
						if (newOrderFoodsList.size() < OFFSET) {
							footTextView.setText(R.string.no_more);
						} else {
							footTextView.setText(R.string.load_more);
						}
						setStartLableTime();
					} else {
						// 首次更新失败
						footTextView.setText(R.string.loading_more_failed);
					}
				} else {
					// 首次更新失败
					footTextView.setText(R.string.loading_more_failed);
				}
				break;
			case 0x123:
				if (msg.obj != null) {
					// 刷新成功
					List<OrderFoods> newOrderFoodsList = parseStringToOrderFoods(msg.obj
							.toString());
					orderFoodsList.clear();
					if (newOrderFoodsList != null) {
						orderFoodsList.addAll(newOrderFoodsList);
						includenternetFailedView.setVisibility(View.GONE);
						adapterOrderedFood.changeSelectedPosition();
						if (orderFoodsList.size() > 0) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									orderListView.getRefreshableView()
											.smoothScrollToPosition(0);
								}
							});
							adapterOrderedFood.notifyDataSetChanged();
						}
						if (newOrderFoodsList.size() < OFFSET) {
							footTextView.setText(R.string.no_more);
						} else {
							footTextView.setText(R.string.load_more);

						}
						setStartLableTime();
					} else {
						// 刷新失败
						MyToast.Show(
								mContext,
								getResources().getString(
										R.string.internet_failed));
						footTextView.setText(R.string.loading_more_failed);
					}

				} else {
					// 刷新失败
					MyToast.Show(mContext,
							getResources().getString(R.string.internet_failed));
					footTextView.setText(R.string.loading_more_failed);
				}

				MainActivity.setRefreshStateOK();
				orderListView.onRefreshComplete();
				break;
			case 0x124:
				// 追加更多成功
				if (msg.obj != null) {
					List<OrderFoods> newOrderFoodsList = parseStringToOrderFoods(msg.obj
							.toString());
					if (newOrderFoodsList != null) {

						orderFoodsList.addAll(newOrderFoodsList);
						adapterOrderedFood.changeSelectedPosition();
						adapterOrderedFood.notifyDataSetChanged();
						if (newOrderFoodsList.size() < OFFSET) {
							footTextView.setText(R.string.no_more);
						} else {
							footTextView.setText(R.string.load_more);
						}
					} else {

						// 追加更多失败
						footTextView.setText(R.string.loading_more_failed);
					}
				} else {
					// 追加更多失败
					footTextView.setText(R.string.loading_more_failed);

				}
				isUpdate = false;

				break;

			default:
				break;
			}

		}
	}

	private class getOrderedFoodListRunnable implements Runnable {
		private int offset;
		private long lastOrderId;
		private boolean isFirstRefresh = false;

		getOrderedFoodListRunnable() {

			this(maxOrderId);
		}

		getOrderedFoodListRunnable(boolean isFirstRefresh) {

			this(maxOrderId);
			this.isFirstRefresh = isFirstRefresh;
		}

		getOrderedFoodListRunnable(long lastOrderId) {
			this(OFFSET, lastOrderId);
		}

		getOrderedFoodListRunnable(int offset, long lastOrderId) {
			this.offset = offset;
			this.lastOrderId = lastOrderId;

		}

		@Override
		public void run() {
			String result = null;
			if (!isFirstRefresh) {
				try {
					Thread.sleep(MainActivity.threadDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				result = (HttpService.getOrderedFoodList(
						DeviceId.getdeviceIdString(getActivity()), offset,
						lastOrderId));

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (lastOrderId < maxOrderId) {
				// 追加
				try {
					Thread.sleep(MainActivity.threadDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(0x124, result));
			} else {

				if (isFirstRefresh == true) {
					// 首次更新

					mHandler.sendMessage(mHandler.obtainMessage(0x122, result));

				} else {

					// 更新

					mHandler.sendMessage(mHandler.obtainMessage(0x123, result));

				}

			}

		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHandler = new MyHandler();
		LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		orderListView = (PullToRefreshListView) mMainView
				.findViewById(R.id.ordered_listview);
		footView = lif.inflate(R.layout.listview_pull_down_refresh_foot,
				orderListView.getRefreshableView(), false);
		orderListView.getRefreshableView().addFooterView(footView);
		footTextView = (TextView) footView
				.findViewById(R.id.listview_refresh_foot);
		includenternetFailedView = mMainView
				.findViewById(R.id.ordered_fragment_include);
		getOrderedFoodListrunnable = new getOrderedFoodListRunnable();
		new Thread(new getOrderedFoodListRunnable(true)).start();
		adapterOrderedFood = new OrderedFoodListAdapter(getActivity(),
				orderFoodsList, R.layout.ordered_listview_item,
				R.layout.ordered_adapter);
		startLabels = orderListView.getLoadingLayoutProxy(true, false);
		orderListView.setAdapter(adapterOrderedFood);
		orderListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				new Thread(getOrderedFoodListrunnable).start();
				MainActivity.startRefreshState();

			}
		});
		orderListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						updateOrderFoodList();

					}
				});

		footView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateOrderFoodList();
			}
		});

	}

	private void updateOrderFoodList() {
		if (isUpdate == false) {
			isUpdate = true;
			footTextView.setText(R.string.loading_more);
			if (orderFoodsList != null && orderFoodsList.size() > 0) {
				new Thread(new getOrderedFoodListRunnable(orderFoodsList.get(
						orderFoodsList.size() - 1).getOrderId())).start();

			} else {
				new Thread(new getOrderedFoodListRunnable(maxOrderId - 1))
						.start();

			}

		}
	}
	public static void setOrderFoodPraiseState(final int orderFoodId,final long orderId,final boolean isPraise){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					HttpService.setOrderFoodPraisestate(orderId, orderFoodId, isPraise);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.ordered_fragment, container,
				false);

		return mMainView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyOptionsMenu() {
		super.onDestroyOptionsMenu();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}
