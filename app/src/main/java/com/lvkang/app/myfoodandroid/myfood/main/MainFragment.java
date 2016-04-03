package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lvkang.app.myfoodandroid.library.ILoadingLayout;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase.OnRefreshListener;
import com.lvkang.app.myfoodandroid.library.PullToRefreshListView;
import com.lvkang.app.myfoodandroid.adapter.AdapterFoodKinds;
import com.lvkang.app.myfoodandroid.adapter.AdapterFoodNormall;
import com.lvkang.app.myfoodandroid.adapter.AdapterKind;
import com.lvkang.app.myfoodandroid.adapter.PopAdapterOrderFood;
import com.lvkang.app.myfoodandroid.dialog.MyAlertDialog;
import com.lvkang.app.myfoodandroid.dialog.MyProgressDialog;
import com.lvkang.app.myfoodandroid.myfood.http.HttpService;
import com.lvkang.app.myfoodandroid.myfood.json.Food;
import com.lvkang.app.myfoodandroid.myfood.json.Foods;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFood;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFoods;
import com.lvkang.app.myfoodandroid.myfood.utils.DeviceId;
import com.lvkang.app.myfoodandroid.myfood.utils.MyToast;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.app.myfoodandroid.myfood.utils.SharePreferenceUtils;
import com.lvkang.myfoodtoandroid.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment {

	private static PullToRefreshListView listView;
	private static ListView attrListView;
	private static Button buttonShopstart, buttonBasket;

	// buttonShowLV, buttonOK, buttonSend,
	private static TextView shopNum, shopPrice;
	private static AdapterFoodNormall adapterFoodSales, adapterFoodPraise;
	private static AdapterFoodKinds adapterFoodKinds;
	private boolean needUpdate = false;

	PopAdapterOrderFood popAdapterOrderFood;

	private static AdapterKind kindAdAdapter;
	public static GetFoodsJsonThread getFoodsJsonThread;
	static MyProgressDialog myProgressDialog;
	MyAlertDialog confirmDeleteAllDialog;
	static MyAlertDialog needUpdateAlertDialog;
	private static ILoadingLayout startLabels;

	PopupWindow popupWindow;
	View parentView;
	View contentView;
	View shop_bar;
	static TextView food_kind;
	static View food_kind_hint;
	static View includeInternetFailedView;
	static ListView popListViewOrderFood;
	static Button buttonAllDelete, buttonPopShopStart, buttonPopBasket;
	TextView dismissTextView;
	static Handler uiHandler = new Handler();

	// 定义缓存文件夹
	static File cacheFile;

	static ArrayList<HashMap<String, Object>> foodMenuArrayList = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> classifiedFoodMenuArrayListSales = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> classifiedFoodMenuArrayListPraise = new ArrayList<HashMap<String, Object>>();

	private static ArrayList<HashMap<String, Object>> classifiedFoodMenuArrayListKind = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> foodMenuArrayListPositionArrayList = new ArrayList<HashMap<String, Object>>();
	// 保存不同的分类
	private static ArrayList<String> kindsList = new ArrayList<String>();
	// 保存特殊分类，比如销量排行
	static ArrayList<String> attrArrayList = new ArrayList<String>();

	// 保存每个分类的长度
	private static int[] kindLength = new int[1];
	// 保存每个分类第一次出现的position
	public static int[] kindPosition = new int[1];

	// 只要点击了加减按钮 orderFoodArrayList就会被刷新，存放的是数量大于0的条目。
	static ArrayList<HashMap<String, Object>> orderPopFoodMenuArrayList = new ArrayList<HashMap<String, Object>>();
	private List<Food> foodList;

	final MyHanler m_Hanler = new MyHanler(MainFragment.this);

	protected View mMainView;
	public static Context mContext;

	public MainFragment() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.menu_fragment, container, false);

		return mMainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		myProgressDialog = new MyProgressDialog(getActivity(), "加载中", "请稍等",
				false, false);
		myProgressDialog.showProgressDialog();

		cacheFile = new File(Environment.getExternalStorageDirectory(),
				"MyFood");

		if (!cacheFile.exists()) {
			cacheFile.mkdirs();

		}

		initPopupWindow();

		Print.P("onCreat", this.getClass().getName());
		food_kind = (TextView) mMainView.findViewById(R.id.food_kind);
		listView = (PullToRefreshListView) mMainView.findViewById(R.id.list);
		buttonShopstart = (Button) mMainView.findViewById(R.id.shopstart);
		buttonBasket = (Button) mMainView.findViewById(R.id.shopbasket);
		attrListView = (ListView) mMainView.findViewById(R.id.attrlist);
		shopNum = (TextView) mMainView.findViewById(R.id.shopnum);
		shopPrice = (TextView) mMainView.findViewById(R.id.shopprice);
		attrArrayList.clear();
		attrArrayList.add("销量排行");
		attrArrayList.add("好评排行");
		includeInternetFailedView = mMainView
				.findViewById(R.id.mainfragment_include);
		includeInternetFailedView.setVisibility(View.GONE);
		food_kind_hint.setVisibility(View.GONE);

		attrListView.setVisibility(View.GONE);
		startLabels = listView.getLoadingLayoutProxy(true, false);
		buttonShopstart.setOnClickListener(new MyButtonOnClickListener());

		buttonBasket.setOnClickListener(new MyButtonOnClickListener());

		// 获取foodmenu

		getFoodsJsonThread = new GetFoodsJsonThread();
		new Thread(new GetFoodsJsonThread(true)).start();
		initNeedUpdateAlertDialog();
		initConfirmDeleteAllDialog();
		new Thread(new checkUpdate()).start();
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new Thread(new GetFoodsJsonThread()).start();
				MainActivity.startRefreshState();

			}
		});

	}

	private static void setStartLableTime() {
		String label = DateUtils.formatDateTime(
				mContext.getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		startLabels.setLastUpdatedLabel("更新于:" + label);
	}

	private class checkUpdate implements Runnable {

		@Override
		public void run() {
			try {
				needUpdate = HttpService.needUpdate();
				if (needUpdate) {
					m_Hanler.sendMessage(m_Hanler.obtainMessage(0x378));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void initNeedUpdateAlertDialog() {
		needUpdateAlertDialog = new MyAlertDialog(getActivity(), getResources()
				.getString(R.string.need_update_to_use), getResources()
				.getString(R.string.find_new_version), false);
		needUpdateAlertDialog.getBuilder().setPositiveButton(
				getResources().getString(R.string.go_to_update),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Uri uri = Uri.parse(MainActivity.IP_STRING);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);

						try {
							Field field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, false);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
		needUpdateAlertDialog.creatAlertDialog();

	}

	private void initConfirmDeleteAllDialog() {
		confirmDeleteAllDialog = new MyAlertDialog(getActivity(), "真的要清空吗",
				"清空", false);
		confirmDeleteAllDialog
				.getBuilder()
				.setPositiveButton("清空", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						confirmDeleteAllDialog.dismissAlertDialog();
						for (int i = 0; i < orderPopFoodMenuArrayList.size(); i++) {
							orderPopFoodMenuArrayList.get(i).put(
									Food.FOOD_NUMBER, 0);
						}

						if (popupWindow != null && popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						shopNum.setText("0");
						shopPrice.setText("0.0");
						setButtonDrawable(false);
						if (adapterFoodKinds != null) {
							adapterFoodKinds.notifyDataSetChanged();
						}
						if (adapterFoodSales != null) {
							adapterFoodSales.notifyDataSetChanged();
						}
						if (adapterFoodPraise != null) {
							adapterFoodPraise.notifyDataSetChanged();
						}
						popAdapterOrderFood.notifyDataSetChanged();

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						confirmDeleteAllDialog.dismissAlertDialog();

					}
				});
		confirmDeleteAllDialog.creatAlertDialog();

	}

	public static ArrayList<HashMap<String, Object>> classifyKindAndPosition(
			ArrayList<HashMap<String, Object>> foodMenuArrayList) {
		ArrayList<HashMap<String, Object>> foodMenuArrayListKind = new ArrayList<HashMap<String, Object>>();
		kindLength = new int[kindsList.size()];

		for (int j = 0; j < kindsList.size(); j++) {

			kindLength[j] = 0;
			for (int i = 0; i < foodMenuArrayList.size(); i++) {
				if (kindsList.get(j).equals(
						foodMenuArrayList.get(i).get(Food.FOOD_ATTR))) {
					foodMenuArrayListKind.add(foodMenuArrayList.get(i));

					kindLength[j]++;
				}

			}

		}
		kindPosition = new int[kindLength.length + 1];
		kindPosition[0] = 0;

		for (int i = 0; i < kindLength.length; i++) {
			for (int j = 0; j < i + 1; j++) {
				kindPosition[i + 1] += kindLength[j];

			}
		}

		return foodMenuArrayListKind;

	}

	public static boolean isExistFoodAttr(ArrayList<String> kindsList,
			String kind) {
		for (int i = 0; i < kindsList.size(); i++) {
			if (kind.equals(kindsList.get(i))) {
				return true;

			}
		}

		return false;

	}

	public static ArrayList<HashMap<String, Object>> findKindPosition(
			ArrayList<HashMap<String, Object>> foodMenuArrayListKind,
			ArrayList<String> kindsList) {
		ArrayList<HashMap<String, Object>> foodMenuArrayListPositionArrayList = new ArrayList<HashMap<String, Object>>();
		String kind = "";
		int j = 0;
		for (int i = 0; i < foodMenuArrayListKind.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			if (!kind.equals(foodMenuArrayListKind.get(i).get(Food.FOOD_ATTR))) {

				map.put("position", j + "");
				map.put(Food.FOOD_ATTR,
						foodMenuArrayListKind.get(i).get(Food.FOOD_ATTR));
				j++;
				kind = foodMenuArrayListKind.get(i).get(Food.FOOD_ATTR)
						.toString();
			} else {
				map.put("position", (j - 1) + "");
				map.put(Food.FOOD_ATTR, 0);
			}
			foodMenuArrayListPositionArrayList.add(map);

		}

		return foodMenuArrayListPositionArrayList;

	}

	public static ArrayList<String> findKind(
			ArrayList<HashMap<String, Object>> foodMenuArrayList) {
		ArrayList<String> kindsList = new ArrayList<String>();
		for (int i = 0; i < foodMenuArrayList.size(); i++) {
			if (!isExistFoodAttr(kindsList,
					foodMenuArrayList.get(i).get(Food.FOOD_ATTR).toString())) {
				kindsList.add(foodMenuArrayList.get(i).get(Food.FOOD_ATTR)
						.toString());
			}
		}

		return kindsList;
	}

	public static ArrayList<HashMap<String, Object>> sortByKind(
			ArrayList<HashMap<String, Object>> foodMenuArrayList) {

		ArrayList<HashMap<String, Object>> classifiedFoodMenuArrayListKind = new ArrayList<HashMap<String, Object>>();
		kindsList = findKind(foodMenuArrayList);
		classifiedFoodMenuArrayListKind = classifyKindAndPosition(foodMenuArrayList);
		foodMenuArrayListPositionArrayList = findKindPosition(
				classifiedFoodMenuArrayListKind, kindsList);

		return classifiedFoodMenuArrayListKind;
	}

	public static ArrayList<HashMap<String, Object>> sortByKey(
			ArrayList<HashMap<String, Object>> foodMenuArrayList,
			String keyString) {
		ArrayList<HashMap<String, Object>> classifiedFoodMenuArrayList = new ArrayList<HashMap<String, Object>>();

		int[] salse = new int[foodMenuArrayList.size()];
		int[] location = new int[foodMenuArrayList.size()];

		for (int k = 0; k < foodMenuArrayList.size(); k++) {

			salse[k] = Integer.parseInt(foodMenuArrayList.get(k).get(keyString)
					.toString());
			location[k] = k;
		}

		for (int k = 0; k < salse.length - 1; k++) {
			for (int i = k; i < salse.length; i++) {

				int start = salse[k];
				int end = salse[i];
				int change = salse[k];

				int locationChange = location[k];

				if (start > end) {

					salse[k] = salse[i];
					salse[i] = change;

					location[k] = location[i];
					location[i] = locationChange;

				}

			}

		}

		for (int j = location.length - 1; j >= 0; j--) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map = foodMenuArrayList.get(location[j]);
			classifiedFoodMenuArrayList.add(map);

		}

		return classifiedFoodMenuArrayList;

	}

	public static void saveFoodMenuBySP(Context context, String menuString) {

		if (SharePreferenceUtils.saveMenuInfo(context, menuString)) {
			Print.P("saveFoodMenuBySP-----succeed");
		} else {
			Print.P("saveFoodMenuBySP-----failed");

		}

	}

	public static String loadFoodMenuBySP(Context context) {
		String menuString = SharePreferenceUtils.getMenuInfo(context);

		Print.P("loadFoodMenuBySP");
		return menuString;

	}

	public static void addNum(int num) {
		int numOlder = Integer.parseInt(shopNum.getText().toString());

		int numNow = numOlder + num;
		shopNum.setText(numNow + "");
		if (numNow > 0) {
			setButtonDrawable(true);
		} else {
			setButtonDrawable(false);

		}

	}

	private static void setButtonDrawable(boolean hasOrdered) {
		if (hasOrdered) {
			buttonShopstart.setBackgroundResource(R.drawable.button);
			buttonBasket.setClickable(true);

			buttonPopShopStart.setBackgroundResource(R.drawable.button);
			buttonPopBasket.setClickable(true);

		} else {
			buttonShopstart.setBackgroundResource(R.drawable.button_sub);
			buttonBasket.setClickable(false);
			buttonPopShopStart.setBackgroundResource(R.drawable.button_sub);
			buttonPopBasket.setClickable(false);

		}

	}

	public static void addPrice(Float price) {

		shopPrice.setText(Float.parseFloat(shopPrice.getText().toString())
				+ price + "");

	}

	public static void refreshNumAndPriceAndAdapter() {
		int num = 0;
		float price = 0;
		int addNum = 0;
		float addPrice = 0;
		ArrayList<HashMap<String, Object>> orderArrayList = findOrderFood();
		for (int i = 0; i < orderArrayList.size(); i++) {
			num = Integer.parseInt(orderArrayList.get(i).get(Food.FOOD_NUMBER)
					.toString());
			price = Float.parseFloat(orderArrayList.get(i)
					.get(Food.FOOD_UNIT_PRICE).toString());
			addNum += num;
			addPrice += price * num;
		}
		shopNum.setText(addNum + "");
		shopPrice.setText(addPrice + "");
		if (addNum > 0) {
			setButtonDrawable(true);
		} else {
			setButtonDrawable(false);

		}

		if (adapterFoodKinds != null) {
			adapterFoodKinds.notifyDataSetChanged();
		}
		if (adapterFoodSales != null) {
			adapterFoodSales.notifyDataSetChanged();
		}
		if (adapterFoodPraise != null) {
			adapterFoodPraise.notifyDataSetChanged();
		}

	}

	public static ArrayList<HashMap<String, Object>> findOrderFood() {
		ArrayList<HashMap<String, Object>> orderFoodMenu = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < foodMenuArrayList.size(); i++) {
			if (foodMenuArrayList.get(i).get(Food.FOOD_NUMBER).toString() != null
					&& Integer.parseInt(foodMenuArrayList.get(i)
					.get(Food.FOOD_NUMBER).toString()) > 0) {
				orderFoodMenu.add(foodMenuArrayList.get(i));
			}

		}

		return orderFoodMenu;
	}

	/**
	 * 将所点food的ArrayList<HashMap<String, Object>>形式转换成OrderFoodJSONObject形式
	 * 
	 * @return OrderFoodJSONObject orderFoodJSONObject
	 */

	public static OrderFoods getOrderFood(
			ArrayList<HashMap<String, Object>> foodMenuArrayList) {
		OrderFoods orderfoodJSONObject = new OrderFoods();

		for (int i = 0; i < foodMenuArrayList.size(); i++) {

			OrderFood orderFood = new OrderFood();

			HashMap<String, Object> a = foodMenuArrayList.get(i);

			if (a.get(Food.FOOD_NUMBER) != null
					&& Integer.parseInt(a.get(Food.FOOD_NUMBER).toString()) > 0) {

				orderFood.setOrderFoodId(Integer.parseInt(a.get(Food.FOOD_ID)
						.toString()));
				orderFood.setOrderFoodNum(Integer.parseInt(a.get(
						Food.FOOD_NUMBER).toString()));
				orderFood.setOrderFoodName(a.get(Food.FOOD_NAME).toString());
				orderFood.setOrderFoodAttr(a.get(Food.FOOD_ATTR).toString());
				orderFood.setOrderFoodUnitPrice(Float.parseFloat(a.get(
						Food.FOOD_UNIT_PRICE).toString()));
				orderfoodJSONObject.getFoodList().add(orderFood);
			}

		}

		return orderfoodJSONObject;

	}

	/**
	 * 使用弱引用的静态Handler类 防止内存泄露
	 * 
	 * @author kangkang
	 *
	 */

	private static class MyHanler extends Handler {
		private final WeakReference<MainFragment> mFragmentReference;

		private MyHanler(MainFragment mainFragment) {
			mFragmentReference = new WeakReference<MainFragment>(mainFragment);
		}

		@Override
		public void handleMessage(Message msg) {
			MainFragment mainFragment = mFragmentReference.get();
			if (mainFragment != null) {
				super.handleMessage(msg);
				if (msg.what == 0x111) {
					foodMenuArrayList = parseJSONStringToMenu(msg.obj
							.toString());
					if (foodMenuArrayList != null) {
						classifiedFoodMenuArrayListKind = sortByKind(foodMenuArrayList);
						// classifiedFoodMenuArrayListSales = sortByKey(
						// foodMenuArrayList, Food.FOOD_SALES);
						classifiedFoodMenuArrayListSales = sortByKey(
								foodMenuArrayList, Food.FOOD_SALES);
						classifiedFoodMenuArrayListPraise = sortByKey(
								foodMenuArrayList, Food.FOOD_PRAISE);
						//
						adapterFoodKinds = setListViewAdapter(
								classifiedFoodMenuArrayListKind, kindsList,
								kindLength, mContext);
						adapterFoodSales = setListViewAdapter(
								classifiedFoodMenuArrayListSales, mContext);

						adapterFoodPraise = setListViewAdapter(
								classifiedFoodMenuArrayListPraise, mContext);

						kindAdAdapter = new AdapterKind(mContext,
								R.layout.attrlist_item, R.id.simple_textview,
								R.id.simple_view, R.id.line3, kindsList,
								attrArrayList);
						attrListView.setAdapter(kindAdAdapter);
						listView.setAdapter(adapterFoodKinds);
						listView.getRefreshableView().setOnScrollListener(
								new MyScrollListener());
						if (msg.arg1 == 1 && msg.arg2 == 0) {
							// first refresh succeed
							setStartLableTime();

						} else if (msg.arg1 == 0 && msg.arg2 == 0) {
							// refresh succeed
							setStartLableTime();
						} else if (msg.arg1 == 1 && msg.arg2 == -1) {
							// first refresh succeed from sp

						} else if (msg.arg1 == 0 && msg.arg2 == -1) {
							// refresh succeed from sp
							MyToast.Show(
									mContext.getApplicationContext(),
									mContext.getResources().getString(
											R.string.internet_failed));
						}
						shopNum.setText(0 + "");
						shopPrice.setText(0 + "");
						setButtonDrawable(false);
						if (kindsList.size() > 0) {
							setKindHintText(kindsList.get(0));
							food_kind_hint.setVisibility(View.VISIBLE);
						}
						attrListView.setVisibility(View.VISIBLE);
						includeInternetFailedView.setVisibility(View.GONE);
					}
					MainActivity.setRefreshStateOK();
					listView.onRefreshComplete();
					myProgressDialog.dismissProgressDialog();

				} else if (msg.what == 0x010) {
					if (msg.arg1 == 1 && msg.arg2 == 0) {
						// first refresh failed from sp

					} else if (msg.arg1 == 0 && msg.arg2 == 0) {
						// refresh failed from sp
						MyToast.Show(
								mContext.getApplicationContext(),
								mContext.getResources().getString(
										R.string.internet_failed));
					}
					attrListView.setVisibility(View.GONE);
					if (kindsList.size() > 0) {
						setKindHintText(kindsList.get(0));
						food_kind_hint.setVisibility(View.VISIBLE);
					}
					includeInternetFailedView.setVisibility(View.VISIBLE);
					MainActivity.setRefreshStateOK();
					listView.onRefreshComplete();
					myProgressDialog.dismissProgressDialog();
				} else if (msg.what == 0x378) {
					needUpdateAlertDialog.showAlertDialog();
				}
			}
		}
	}

	static AdapterFoodKinds setListViewAdapter(
			ArrayList<HashMap<String, Object>> classifiedFoodMenuArrayListKind,
			ArrayList<String> kindsList, int[] kindPosition, Context context) {

		AdapterFoodKinds adapterFoodKinds = new AdapterFoodKinds(context,
				classifiedFoodMenuArrayListKind, R.layout.food_adapter,
				new String[] { Food.FOOD_JPG, Food.FOOD_NAME,
						Food.FOOD_UNIT_PRICE, Food.FOOD_NUMBER,
						Food.FOOD_REMARKS, Food.FOOD_SALES, Food.FOOD_ATTR,
						Food.FOOD_INTRO, Food.FOOD_PRAISE }, new int[] {
						R.id.itemimage, R.id.foodname, R.id.foodunitprice,
						R.id.itemadd, R.id.itemcut, R.id.number,
						R.id.foodremarks, R.id.food_sales, R.id.food_kind,
						R.id.imageview_progressbar, R.id.foodintro,
						R.id.food_praise, R.id.food_kind_hint_include_include });

		return adapterFoodKinds;

	}

	static AdapterFoodNormall setListViewAdapter(
			ArrayList<HashMap<String, Object>> classifiedFoodMenuArrayList,
			Context context) {

		AdapterFoodNormall listItemAdapter = new AdapterFoodNormall(context,
				classifiedFoodMenuArrayList, R.layout.food_adapter,
				new String[] { Food.FOOD_JPG, Food.FOOD_NAME,
						Food.FOOD_UNIT_PRICE, Food.FOOD_NUMBER,
						Food.FOOD_REMARKS, Food.FOOD_SALES, Food.FOOD_INTRO,
						Food.FOOD_PRAISE }, new int[] { R.id.itemimage,
						R.id.foodname, R.id.foodunitprice, R.id.itemadd,
						R.id.itemcut, R.id.number, R.id.foodremarks,
						R.id.food_sales, R.id.imageview_progressbar,
						R.id.foodintro, R.id.food_praise });

		return listItemAdapter;

	}

	public static void setKindHintText(String kind) {
		food_kind.setText(kind);
	}

	private static class MyScrollListener implements OnScrollListener {

		int isScroll = 0;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			isScroll = scrollState;

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 如果左侧listview选中的position+1>特殊arraylist的大小,防止在特殊adapter中的滑动引起attrlistview变化
			firstVisibleItem -= 1;
			if (firstVisibleItem < 0) {
				firstVisibleItem = 0;
			}
			if (AdapterKind.selectedPositon + 1 > attrArrayList.size()
					&& (isScroll == 1 || isScroll == 2)) {

				if (!foodMenuArrayListPositionArrayList.get(firstVisibleItem)
						.get("position").equals(-1)) {
					//

					String nameString = classifiedFoodMenuArrayListKind
							.get(firstVisibleItem).get(Food.FOOD_NAME)
							.toString();
					final int findSelectedID = Integer
							.parseInt(foodMenuArrayListPositionArrayList
									.get(firstVisibleItem).get("position")
									.toString())
							+ attrArrayList.size();
					if (AdapterKind.selectedPositon != findSelectedID) {
						final boolean isUp;
						if (AdapterKind.selectedPositon < findSelectedID) {
							// 下一
							isUp = false;
							Print.P("isUp: " + isUp + "Move");
						} else {
							// 上移
							isUp = true;
							Print.P("isUp: " + isUp + "Move");

						}
						setKindHintText(kindsList.get(findSelectedID
								- attrArrayList.size()));
						AdapterKind.selectedPositon = findSelectedID;
						kindAdAdapter.notifyDataSetChanged();
						uiHandler.post(new Runnable() {

							@Override
							public void run() {

								int offset = 4;
								if (isUp) {

									int numcut = findSelectedID - offset;
									if (numcut < 0) {
										numcut = 0;
									}
									attrListView.smoothScrollToPosition(numcut);
								} else {

									int numadd = findSelectedID + offset;
									if (numadd > kindsList.size()
											+ attrArrayList.size()) {
										numadd = kindsList.size()
												+ attrArrayList.size();
									}
									Print.P(numadd + "Move");
									attrListView.smoothScrollToPosition(numadd);
								}

							}
						});

					}

				}
			}

		}

	}

	public static void changeAdapter(int i) {
		switch (i) {
		case 0:
			food_kind_hint.setVisibility(View.GONE);
			listView.getRefreshableView().setAdapter(adapterFoodSales);
			break;
		case 1:
			food_kind_hint.setVisibility(View.GONE);
			listView.getRefreshableView().setAdapter(adapterFoodPraise);
			break;
		default:
			food_kind_hint.setVisibility(View.VISIBLE);
			listView.getRefreshableView().setAdapter(adapterFoodKinds);
			int position = 0;
			// 加入了下拉刷新后，会产生偏移，故+1。
			position = kindPosition[i - attrArrayList.size()] + 1;
			listView.getRefreshableView().setSelection(position);
			adapterFoodKinds.notifyDataSetChanged();
			break;
		}
	}

	public class MyButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.popupwindow_shop:

			case R.id.shopstart:

				if (findOrderFood().size() > 0) {

					Intent intent = new Intent(mContext, ShopActivity.class);
					startActivity(intent);
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}

				} else {
					MyToast.Show(mContext, "请先挑选食品！");
				}

				break;
			case R.id.shopbasket:

				openPopupWindow();

				break;

			case R.id.popupwindow_textview:

			case R.id.popupwindow_basket:
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				break;

			case R.id.popupwindow_hearder_delete_all:
				if (findOrderFood().size() > 0) {

					confirmDeleteAllDialog.showAlertDialog();
				}

				break;

			default:
				break;
			}

		}

	}

	public static String getOrderFoodObjectStringFromFoodMenu(
			ArrayList<HashMap<String, Object>> foodMenuArrayListMap) {
		OrderFoods orderFoodJSONObject = getOrderFood(foodMenuArrayList);
		orderFoodJSONObject.setOrderCustomerName("TestOrderCustomerName");
		orderFoodJSONObject.setOrderDateTime(new Date().getTime() + "");
		orderFoodJSONObject.setOrderId(-1);
		orderFoodJSONObject.setOrderTableId(-1);
		String orderFoodJSONString = JSON.toJSONString(orderFoodJSONObject,
				SerializerFeature.UseSingleQuotes);
		return orderFoodJSONString;
	}

	public static String getOrderFoodObjectStringFromOrderFoodList(
			ArrayList<HashMap<String, Object>> orderFoodArrayListMap,
			int tableId, String remarks) {

		OrderFoods orderfoodJSONObject = new OrderFoods();

		for (int i = 0; i < orderFoodArrayListMap.size(); i++) {

			OrderFood orderFood = new OrderFood();

			HashMap<String, Object> a = orderFoodArrayListMap.get(i);

			if (a.get(Food.FOOD_NUMBER) != null
					&& Integer.parseInt(a.get(Food.FOOD_NUMBER).toString()) > 0) {

				orderFood.setOrderFoodId(Integer.parseInt(a.get(Food.FOOD_ID)
						.toString()));
				orderFood.setOrderFoodNum(Integer.parseInt(a.get(
						Food.FOOD_NUMBER).toString()));
				orderFood.setOrderFoodName(a.get(Food.FOOD_NAME).toString());

				orderfoodJSONObject.getFoodList().add(orderFood);
			}

		}

		orderfoodJSONObject.setOrderCustomerName(DeviceId
				.getdeviceIdString(mContext));
		orderfoodJSONObject.setOrderTableId(tableId);

		orderfoodJSONObject.setOrderRemarks(remarks);

		String orderFoodJSONString = JSON.toJSONString(orderfoodJSONObject,
				SerializerFeature.UseSingleQuotes);

		return orderFoodJSONString;
	}

	/**
	 * 使用GET方式获得FoodMenu 得到后写入listitem 通知handler更新baseAdapter
	 * 
	 * @author kangkang
	 *
	 */

	class GetFoodsJsonThread implements Runnable {
		boolean isFirstRefresh = false;

		GetFoodsJsonThread() {

		}

		GetFoodsJsonThread(boolean isFirstRefresh) {
			this.isFirstRefresh = isFirstRefresh;
		}

		public void run() {
			if (!isFirstRefresh) {
				try {
					Thread.sleep(MainActivity.threadDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			foodMenuArrayList.clear();
			String foodListString = null;
			foodList = null;
			// 先去网络下载
			foodListString = HttpService.getFoodList();
			if (foodListString != null) {
				// 网络下载成功
				// 存储得到的foodlist到sharepreferences
				saveFoodMenuBySP(mContext, foodListString);
				if (isFirstRefresh) {
					// first refresh succeed
					m_Hanler.sendMessage(m_Hanler.obtainMessage(0x111, 1, 0,
							foodListString));
				} else {
					// refresh succeed
					m_Hanler.sendMessage(m_Hanler.obtainMessage(0x111, 0, 0,
							foodListString));
				}

			} else {
				// 网络下载失败
				try {
					// sp中找到了
					if (SharePreferenceUtils.getMenuInfo(mContext) != null
							&& SharePreferenceUtils.getMenuInfo(mContext)
									.length() > 0) {
						foodListString = SharePreferenceUtils
								.getMenuInfo(mContext);
						if (isFirstRefresh) {
							// first refresh failed
							// -1表示是从sp中获得的
							m_Hanler.sendMessage(m_Hanler.obtainMessage(0x111,
									1, -1, foodListString));

						} else {
							// refresh failed
							m_Hanler.sendMessage(m_Hanler.obtainMessage(0x111,
									0, -1, foodListString));

						}
					} else {
						// sp中找不到
						if (isFirstRefresh) {
							// first refresh failed
							m_Hanler.sendMessage(m_Hanler.obtainMessage(0x010,
									1, 0));

						} else {
							// refresh failed
							m_Hanler.sendMessage(m_Hanler.obtainMessage(0x010,
									0, 0));

						}

					}

				} catch (Exception e1) {
					e1.printStackTrace();
					if (isFirstRefresh) {
						// first refresh failed
						m_Hanler.sendMessage(m_Hanler
								.obtainMessage(0x010, 1, 0));

					} else {
						// refresh failed
						m_Hanler.sendMessage(m_Hanler
								.obtainMessage(0x010, 0, 0));

					}

				}

			}

		}

	};

	public static ArrayList<HashMap<String, Object>> parseJSONStringToMenu(
			String foodListString) {
		ArrayList<HashMap<String, Object>> foodMenuArrayList = new ArrayList<HashMap<String, Object>>();
		try {
			List<Food> foods = parseStringToFoodList(foodListString);
			for (int i = 0; i < foods.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(Food.FOOD_ID, foods.get(i).getFoodId());
				map.put(Food.FOOD_NAME, foods.get(i).getFoodName());
				map.put(Food.FOOD_UNIT_PRICE, foods.get(i).getFoodUnitPrice());
				map.put(Food.FOOD_REMAIN, foods.get(i).getFoodRemain());
				map.put(Food.FOOD_REMARKS, foods.get(i).getFoodRemarks());
				map.put(Food.FOOD_ATTR, foods.get(i).getFoodAttr());
				map.put(Food.FOOD_JPG, foods.get(i).getFoodJpg());
				map.put(Food.FOOD_SALES, foods.get(i).getFoodSales());
				map.put(Food.FOOD_INTRO, foods.get(i).getFoodIntro());
				map.put(Food.FOOD_PRAISE, foods.get(i).getFoodPraise());
				map.put(Food.FOOD_NUMBER, 0);
				foodMenuArrayList.add(map);

			}

			return foodMenuArrayList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	public static List<Food> parseStringToFoodList(String stringJSON) {
		Foods getfood = new Foods();
		try {

			getfood = JSON.parseObject(stringJSON, Foods.class);

			List<Food> foodList = getfood.getFoodList();
			return foodList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getfood.getFoodList();
	}

	public void initPopupWindow() {

		contentView = getActivity().getLayoutInflater().inflate(
				R.layout.order_food_popupwindow, null);
		parentView = mMainView.findViewById(R.id.main_activity_layout);
		shop_bar = mMainView.findViewById(R.id.shop_bar_include);
		food_kind_hint = mMainView.findViewById(R.id.food_kind_hint);
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		dismissTextView = (TextView) contentView
				.findViewById(R.id.popupwindow_textview);
		buttonPopShopStart = (Button) contentView
				.findViewById(R.id.popupwindow_shop);

		buttonPopBasket = (Button) contentView
				.findViewById(R.id.popupwindow_basket);
		popListViewOrderFood = (ListView) contentView
				.findViewById(R.id.popup_window_listview);
		buttonPopShopStart.setOnClickListener(new MyButtonOnClickListener());
		dismissTextView.setOnClickListener(new MyButtonOnClickListener());
		buttonPopBasket.setOnClickListener(new MyButtonOnClickListener());
		LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		View headerView = lif.inflate(R.layout.popupwindow_listview_header,
				popListViewOrderFood, false);

		popListViewOrderFood.addHeaderView(headerView);
		buttonAllDelete = (Button) headerView
				.findViewById(R.id.popupwindow_hearder_delete_all);
		buttonAllDelete.setOnClickListener(new MyButtonOnClickListener());

	}

	public void openPopupWindow() {

		popAdapterOrderFood = setPopListViewAdapter(findOrderFood(), mContext);
		popListViewOrderFood.setAdapter(popAdapterOrderFood);

		int[] location = new int[2];

		shop_bar.getLocationInWindow(location);

		popupWindow.getContentView().measure(0, 0);
		int height = popupWindow.getContentView().getMeasuredHeight();

		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0,
				location[1] - height);
	}

	public static void refreshPopOrderFood() {

		if (adapterFoodKinds != null) {
			adapterFoodKinds.notifyDataSetChanged();
		}
		if (adapterFoodSales != null) {
			adapterFoodSales.notifyDataSetChanged();
		}
		if (adapterFoodPraise != null) {
			adapterFoodPraise.notifyDataSetChanged();
		}

	}

	private static PopAdapterOrderFood setPopListViewAdapter(
			ArrayList<HashMap<String, Object>> getOrderFoodMenuArrayList,
			Context context) {

		orderPopFoodMenuArrayList = getOrderFoodMenuArrayList;
		PopAdapterOrderFood popAdapterOrderFood = new PopAdapterOrderFood(
				context, orderPopFoodMenuArrayList, R.layout.order_adapter,
				new String[] { Food.FOOD_NUMBER, Food.FOOD_NAME,
						Food.FOOD_UNIT_PRICE }, new int[] {
						R.id.order_food_num, R.id.order_food_name,
						R.id.order_food_unitprice, R.id.order_food_price,
						R.id.num_add, R.id.num_cut, R.id.num_delete, });

		return popAdapterOrderFood;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();

		Print.P("onResume", this.getClass().getName());
		refreshNumAndPriceAndAdapter();

	}

	@Override
	public void onPause() {
		Print.P("onPause", this.getClass().getName());
		super.onPause();

	}

	@Override
	public void onDestroy() {
		confirmDeleteAllDialog.deleteAlertDialog();
		myProgressDialog.deleteProgressDialog();
		Print.P("noDestory", this.getClass().getName());
		super.onDestroy();

	}

}
