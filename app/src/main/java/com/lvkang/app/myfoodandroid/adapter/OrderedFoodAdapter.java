package com.lvkang.app.myfoodandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.myfood.json.OrderFood;
import com.lvkang.app.myfoodandroid.main.OrderedFragment;
import com.lvkang.myfoodtoandroid.R;

import java.util.ArrayList;
import java.util.List;

public class OrderedFoodAdapter extends BaseAdapter {
	Context mContext;
	private List<OrderFood> orderFoods = new ArrayList<OrderFood>();
	private long orderId;

	private LayoutInflater mInflater;
	private orderFoodHolder orderFoodHolder;
	private int orderFoodListViewItem;

	private class orderFoodHolder {
		TextView foodPrice;
		TextView foodName;
		TextView foodUnitPrice;
		TextView number;
		CheckBox ck;

	}

	// 这里传入的数据是List<OrderFood>
	public OrderedFoodAdapter(Context c, List<OrderFood> orderFoods,
			int orderFoodListViewItem, long orderId) {
		this.mContext = c;
		this.orderFoods = orderFoods;
		this.orderFoodListViewItem = orderFoodListViewItem;
		this.orderId = orderId;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orderFoods.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return orderFoods.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			orderFoodHolder = (orderFoodHolder) convertView.getTag();
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
			orderFoodHolder.ck = (CheckBox) convertView
					.findViewById(R.id.ordered_praise);

			convertView.setTag(orderFoodHolder);
		}

		if (orderFoods != null) {
			int num = orderFoods.get(position).getOrderFoodNum();
			float unitprice = orderFoods.get(position).getOrderFoodUnitPrice();
			boolean isPraise = orderFoods.get(position).isPraise();
			int orderFoodId = orderFoods.get(position).getOrderFoodId();
			float price = num * unitprice;
			orderFoodHolder.foodName.setText(orderFoods.get(position)
					.getOrderFoodName());

			orderFoodHolder.foodPrice.setText(price + "");

			orderFoodHolder.foodUnitPrice.setText(unitprice + "");
			orderFoodHolder.number.setText(num + "");
			orderFoodHolder.ck.setChecked(isPraise);
			orderFoodHolder.ck.setOnClickListener(new CKListener(position,
					orderFoodId));

		}

		return convertView;

	}

	private class CKListener implements CheckBox.OnClickListener {
		int position;
		int orderFoodId;

		CKListener(int position, int orderFoodId) {
			this.position = position;
			this.orderFoodId = orderFoodId;
		}

		@Override
		public void onClick(View v) {
			try {
				if (orderFoods.get(position).isPraise()) {
					orderFoods.get(position).setPraise(false);
					OrderedFragment.setOrderFoodPraiseState(orderFoodId,
							orderId, false);
				} else {
					orderFoods.get(position).setPraise(true);
					OrderedFragment.setOrderFoodPraiseState(orderFoodId,
							orderId, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
