package com.lvkang.app.myfoodandroid.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.myfood.json.OrderFood;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFoods;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.myfoodtoandroid.R;

import java.util.ArrayList;
import java.util.List;

public class OrderedFoodListAdapter extends BaseAdapter {
	Context mContext;
	private List<OrderFoods> orderFoodsList = new ArrayList<OrderFoods>();
	// 记录被展开/收起按钮的状态，防止convertview被复用后，展开状态消失（就是滚动页面后再返回，展开状态消失）
	private int[] selectedPosition;

	private LayoutInflater mInflater;
	private orderHolder orderHolder;
	// adapter的布局文件
	private int infoListViewItem;
	// item中adapter的布局文件
	private int orderFoodListViewItem;
	// 每个item的高度，最好换成动态获取每个item的高度
	private static final int itemHeight = 135;

	private class orderHolder {
		TextView timeTextView;
		TextView dateTextView;
		TextView tableIDTextView;
		TextView priceTextView;
		TextView numtextView;
		TextView snTextView;
		TextView remarkstexTextView;
		ListView orderedFoodsListView;
		View footView;
		Button orderedListviewButton;
	}

	public void changeSelectedPosition() {

		int[] newSelectedPosition = new int[orderFoodsList.size()];
		if (newSelectedPosition.length > selectedPosition.length) {

			System.arraycopy(selectedPosition, 0, newSelectedPosition, 0,
					selectedPosition.length);
		} else {
			System.arraycopy(selectedPosition, 0, newSelectedPosition, 0,
					newSelectedPosition.length);

		}
		selectedPosition = newSelectedPosition;
	}

	class lvButtonListener implements View.OnClickListener {
		private int itemNum;
		private ListView listView;
		private RelativeLayout.LayoutParams params;
		private Button button;
		private int position;

		lvButtonListener(int position, int itemNum, ListView listView) {
			this.itemNum = itemNum;
			this.listView = listView;
			this.position = position;
		}

		@Override
		// 复写onClick方法用来监听按钮
		public void onClick(View v) {
			int vid = v.getId();
			button = (Button) v;
			if (vid == orderHolder.orderedListviewButton.getId()) {

				if (selectedPosition[position] == 0) {

					params = (RelativeLayout.LayoutParams) listView
							.getLayoutParams();

					button.setText("点击收起" + (itemNum - 2 + "项"));

					params.height = itemNum * itemHeight;
					selectedPosition[position] = 1;

				} else if (selectedPosition[position] == 1) {
					params = (RelativeLayout.LayoutParams) listView
							.getLayoutParams();
					button.setText("点击展开" + (itemNum - 2 + "项"));

					params.height = 2 * itemHeight;
					selectedPosition[position] = 0;

				}

				listView.setLayoutParams(params);

			}

		}
	}

	public OrderedFoodListAdapter(Context c, List<OrderFoods> orderFoodsList,
			int infoListViewItem, int orderFoodListViewItem) {
		this.mContext = c;
		this.orderFoodsList = orderFoodsList;
		this.infoListViewItem = infoListViewItem;
		this.orderFoodListViewItem = orderFoodListViewItem;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectedPosition = new int[orderFoodsList.size()];

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orderFoodsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return orderFoodsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			orderHolder = (orderHolder) convertView.getTag();

		} else {
			convertView = mInflater.inflate(infoListViewItem, null);
			orderHolder = new orderHolder();
			orderHolder.numtextView = (TextView) convertView
					.findViewById(R.id.num_textview);
			orderHolder.priceTextView = (TextView) convertView
					.findViewById(R.id.price_textview);
			orderHolder.remarkstexTextView = (TextView) convertView
					.findViewById(R.id.remarks_textview);
			orderHolder.snTextView = (TextView) convertView
					.findViewById(R.id.sn_textview);
			orderHolder.tableIDTextView = (TextView) convertView
					.findViewById(R.id.tableid_textview);
			orderHolder.dateTextView = (TextView) convertView
					.findViewById(R.id.date_textview);
			orderHolder.orderedFoodsListView = (ListView) convertView
					.findViewById(R.id.ordered_list_view);
			orderHolder.orderedListviewButton = (Button) convertView
					.findViewById(R.id.ordered_listview_button);
			orderHolder.timeTextView = (TextView) convertView
					.findViewById(R.id.time_textview);
			orderHolder.footView = convertView
					.findViewById(R.id.ordered_list_view_foot_view);
			convertView.setTag(orderHolder);

		}
		if (orderFoodsList != null) {

			int addNum = 0;
			float addPrice = 0;
			int num = 0;
			float price = 0;

			List<OrderFood> orderFoods = orderFoodsList.get(position)
					.getFoodList();
			for (OrderFood orderFood : orderFoods) {

				num = orderFood.getOrderFoodNum();
				price = orderFood.getOrderFoodUnitPrice();

				addNum += num;
				addPrice += num * price;

			}

			orderHolder.numtextView.setText(addNum + "份");

			SpannableStringBuilder builder = new SpannableStringBuilder(
					addPrice + "元");

			// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
			ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext
					.getResources().getColor(R.color.myorange));

			builder.setSpan(redSpan, 0, builder.length() - 1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			orderHolder.priceTextView.setText(builder);
			String orderedRemarks =orderFoodsList.get(position).getOrderRemarks();

			if (orderedRemarks.length() > 0) {
				orderHolder.remarkstexTextView.setText("备注："+orderedRemarks);

				orderHolder.remarkstexTextView.setVisibility(View.VISIBLE);
			} else {

				orderHolder.remarkstexTextView.setVisibility(View.GONE);
			}

			String orderID = orderFoodsList.get(position).getOrderId() + "";
			long orderIdNum= Long.parseLong(orderID);
			orderHolder.snTextView.setText(orderID.substring(
					orderID.length() - 4, orderID.length()));
			orderHolder.tableIDTextView.setText(orderFoodsList.get(position)
					.getOrderTableId() + "号");
			String dateTimeString = (orderFoodsList.get(position)
					.getOrderDateTime());
			

			orderHolder.dateTextView.setText(dateTimeString.substring(2, 10));

			orderHolder.timeTextView.setText(dateTimeString.substring(11, 19));

			if (orderFoods != null && orderFoods.size() > 0) {

				OrderedFoodAdapter orderedFoodAdapter = new OrderedFoodAdapter(
						mContext, orderFoods, orderFoodListViewItem,orderIdNum);
				orderHolder.orderedFoodsListView.setAdapter(orderedFoodAdapter);

				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) orderHolder.orderedFoodsListView
						.getLayoutParams();
				// selectedPosition[position] == 0表示不是展开状态
				if (selectedPosition.length > 0) {
					if (selectedPosition[position] == 0) {

						switch (orderFoods.size()) {
						case 1:
							params.height = itemHeight;
							orderHolder.orderedListviewButton
									.setVisibility(View.GONE);
							orderHolder.footView.setVisibility(View.GONE);

							break;
						case 2:
							params.height = 2 * itemHeight;
							orderHolder.orderedListviewButton
									.setVisibility(View.GONE);
							orderHolder.footView.setVisibility(View.GONE);

							break;

						default:
							params.height = 2 * itemHeight;
							orderHolder.orderedListviewButton
									.setVisibility(View.VISIBLE);
							orderHolder.footView.setVisibility(View.VISIBLE);

							orderHolder.orderedListviewButton.setText("点击展开"
									+ (orderFoods.size() - 2) + "项");
							orderHolder.orderedListviewButton
									.setOnClickListener(new lvButtonListener(
											position, orderFoods.size(),
											orderHolder.orderedFoodsListView));

							break;
						}
						// selectedPosition[position] == 0表示是展开状态

					} else if (selectedPosition[position] == 1) {
						switch (orderFoods.size()) {
						case 1:
							params.height = itemHeight;
							orderHolder.orderedListviewButton
									.setVisibility(View.GONE);
							orderHolder.footView.setVisibility(View.GONE);

							break;
						case 2:
							params.height = 2 * itemHeight;
							orderHolder.orderedListviewButton
									.setVisibility(View.GONE);
							orderHolder.footView.setVisibility(View.GONE);

							break;

						default:
							params.height = orderFoods.size() * itemHeight;
							orderHolder.orderedListviewButton
									.setVisibility(View.VISIBLE);
							orderHolder.footView.setVisibility(View.VISIBLE);

							orderHolder.orderedListviewButton.setText("点击收起"
									+ (orderFoods.size() - 2) + "项");
							orderHolder.orderedListviewButton
									.setOnClickListener(new lvButtonListener(
											position, orderFoods.size(),
											orderHolder.orderedFoodsListView));

							break;
						}

					}
				}

				orderHolder.orderedFoodsListView.setLayoutParams(params);
				Print.P(orderHolder.orderedFoodsListView.getHeight() + "");
			}

		}

		return convertView;

	}

}
