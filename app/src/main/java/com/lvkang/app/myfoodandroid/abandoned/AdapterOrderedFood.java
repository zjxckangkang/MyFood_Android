package com.lvkang.app.myfoodandroid.abandoned;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.myfood.json.OrderFood;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFoods;
import com.lvkang.myfoodtoandroid.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterOrderedFood extends BaseAdapter {
	Context mContext;
	private ArrayList<HashMap<String, String>> mAppList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> orderInfo = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> orderFoodInfo = new ArrayList<HashMap<String, String>>();
	private ArrayList<Integer> orderPosition = new ArrayList<Integer>();

	private LayoutInflater mInflater;
	private orderFoodHolder orderFoodHolder;
	private orderHolder orderHolder;
	private int infoListViewItem;
	private int orderFoodListViewItem;

	private MyHandler mHandler;

	private class orderFoodHolder {
		TextView foodPrice;
		TextView foodName;
		TextView foodUnitPrice;
		TextView number;

	}

	private class orderHolder {
		TextView timeTextView;
		TextView tableIDTextView;
		TextView priceTextView;
		TextView numtextView;
		TextView snTextView;
		TextView remarkstexTextView;
		RelativeLayout parentLayout;
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == 0) {

			}

		}

	}

	private void setOrderInfoList() {
		String OrderID = "";

		for (int i = 0; i < mAppList.size(); i++) {
			HashMap<String, String> orderInfoHashMap = new HashMap<String, String>();
			orderInfoHashMap = mAppList.get(i);
			if (mAppList.get(i).get(OrderFoods.ORDER_FOOD_ORDER_ID) != null) {
				OrderID = mAppList.get(i).get(
						OrderFoods.ORDER_FOOD_ORDER_ID);
			}

			if (mAppList.get(i).get(
					OrderFoods.ORDER_FOOD_ORDER_CUSTOMER_NAME) != null) {
				orderInfo.add(orderInfoHashMap);

				orderPosition.add(Integer.valueOf(i));

			} else {
				orderInfoHashMap.put(OrderFoods.ORDER_FOOD_ORDER_ID,
						OrderID);

				orderFoodInfo.add(orderInfoHashMap);
			}

		}
	}

	public AdapterOrderedFood(Context c,
			ArrayList<HashMap<String, String>> orderedFoodList,
			int infoListViewItem, int orderFoodListViewItem) {
		this.mContext = c;
		this.mAppList = orderedFoodList;
		this.infoListViewItem = infoListViewItem;
		this.orderFoodListViewItem = orderFoodListViewItem;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setOrderInfoList();
		mHandler = new MyHandler();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return orderInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class addViewRunnable implements Runnable {
		RelativeLayout mainLayout;
		ArrayList<HashMap<String, String>> orderFoods = new ArrayList<HashMap<String, String>>();

		public addViewRunnable(RelativeLayout mainLayout,
				ArrayList<HashMap<String, String>> orderFoods) {
			this.mainLayout = mainLayout;
			this.orderFoods = orderFoods;
		}

		public void run() {
			// RelativeLayout mainLayout = (RelativeLayout) MainFragment.this
			// .getActivity().findViewById(R.id.ordered_relaticelayout);

			final RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp1.addRule(RelativeLayout.ALIGN_TOP);
			lp1.setMargins(0, 0, 0, 0);

			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View child = mInflater.inflate(R.layout.order_adapter, null);

			for (int i = 0; i < 1; i++) {
				lp1.leftMargin = 10;
				lp1.topMargin = 60 * i;

				mHandler.post(new Runnable() {
					public void run() {
						ViewGroup parent = (ViewGroup) child.getParent();
						if (parent != null) {
							parent.removeAllViews();
						}
						mainLayout.addView(child, lp1);

					}
				});


			}

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		for (int j = 0; j < orderPosition.size(); j++) {

			if (orderPosition.get(j).intValue() == position) {
				int suit = 0;

				try {
					orderHolder = (orderHolder) convertView.getTag();
					suit = 1;
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (suit == 1) {
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
					orderHolder.timeTextView = (TextView) convertView
							.findViewById(R.id.date_textview);
					orderHolder.parentLayout = (RelativeLayout) convertView
							.findViewById(R.id.ordered_listview_item_layout);
					convertView.setTag(orderHolder);

				}
				if (orderInfo != null) {
					orderHolder.numtextView.setText(orderInfo.get(j).get(
							OrderFoods.ORDER_FOOD_ORDER_NUM)
							+ "份");

					SpannableStringBuilder builder = new SpannableStringBuilder(
							orderInfo.get(j).get(
									OrderFoods.ORDER_FOOD_ORDER_PRICE)
									+ "元");

					// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
					ForegroundColorSpan redSpan = new ForegroundColorSpan(
							Color.RED);

					builder.setSpan(redSpan, 0, builder.length() - 1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

					orderHolder.priceTextView.setText(builder);
					String orderedRemarks = orderInfo.get(j).get(
							OrderFoods.ORDER_FOOD_ORDER_REMARKS);

					if (orderedRemarks.length() > 0) {
						orderHolder.remarkstexTextView.setText(orderedRemarks);
					} else {
						orderHolder.remarkstexTextView.setVisibility(View.GONE);
					}

					String orderID = orderInfo.get(j).get(
							OrderFoods.ORDER_FOOD_ORDER_ID);
					orderHolder.snTextView.setText(orderID.substring(
							orderID.length() - 4, orderID.length()));
					orderHolder.tableIDTextView.setText(orderInfo.get(j).get(
							OrderFoods.ORDER_FOOD_ORDER_TABLE_ID)
							+ "号");

					orderHolder.timeTextView
							.setText((orderInfo.get(j)
									.get(OrderFoods.ORDER_FOOD_ORDER_DATE_TIME))
									.substring(2, 18));

					ArrayList<HashMap<String, String>> orderFoods = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < orderFoodInfo.size(); i++) {
						// HashMap<String, String> orderFood=new HashMap<String,
						// String>();
						if (orderFoodInfo.get(i)
								.get(OrderFoods.ORDER_FOOD_ORDER_ID)
								.equals(orderID)) {

							orderFoods.add(orderFoodInfo.get(i));

						}

					}
					
					return convertView;
				}
			}
		}

		int suit2 = 0;
		try {
			orderFoodHolder = (orderFoodHolder) convertView.getTag();

			suit2 = 1;
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (suit2 == 1) {
		} else {
			convertView = mInflater.inflate(orderFoodListViewItem, null);
			orderFoodHolder = new orderFoodHolder();
			orderFoodHolder.foodName = (TextView) convertView
					.findViewById(R.id.ordered_food_name);
			orderFoodHolder.foodPrice = (TextView) convertView
					.findViewById(R.id.ordered_food_price);
			orderFoodHolder.foodUnitPrice = (TextView) convertView
					.findViewById(R.id.ordered_food_unitprice);
			orderFoodHolder.number = (TextView) convertView
					.findViewById(R.id.ordered_food_num);

			convertView.setTag(orderFoodHolder);

		}

		if (orderFoodInfo != null) {
			int num = Integer.parseInt(mAppList.get(position).get(
					OrderFood.Order_FOOD_NUM));
			float unitprice = Float.parseFloat(mAppList.get(position).get(
					OrderFood.Order_FOOD_UNIT_PRICE));

			float price = num * unitprice;
			orderFoodHolder.foodName.setText(mAppList.get(position).get(
					OrderFood.Order_FOOD_NAME)
					+ "");

			orderFoodHolder.foodPrice.setText(price + "");

			orderFoodHolder.foodUnitPrice.setText(unitprice + "");
			orderFoodHolder.number.setText(num + "");

		}

		return convertView;

	}
}
