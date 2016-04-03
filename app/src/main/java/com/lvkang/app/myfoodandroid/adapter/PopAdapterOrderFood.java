package com.lvkang.app.myfoodandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.myfood.json.Food;
import com.lvkang.app.myfoodandroid.myfood.main.MainFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 */
public class PopAdapterOrderFood extends BaseAdapter {
	private class buttonViewHolder {
		TextView foodPrice;
		TextView foodName;
		TextView foodUnitPrice;
		TextView number;
		Button btAdd;
		Button btCut;
		Button btDelete;
	}

	private ArrayList<HashMap<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keystring;
	private int[] valueViewId;
	private buttonViewHolder holder;
	private int listViewItem;

	// 适配一个listview中的item元素
	public PopAdapterOrderFood(Context c,
			ArrayList<HashMap<String, Object>> applist, int resources,
			String[] from, int[] to) {
		this.listViewItem=resources;
		mAppList = applist;
		mContext = c;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keystring = new String[from.length];
		valueViewId = new int[to.length];

		System.arraycopy(from, 0, keystring, 0, from.length);
		System.arraycopy(to, 0, valueViewId, 0, to.length);

	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void removeItem(int position) {
		mAppList.remove(position);
		this.notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	@Override
	// 获取listview中每个item中的控件，及设置每个控件中的元素
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (buttonViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(listViewItem, null);
			holder = new buttonViewHolder();

			holder.number = (TextView) convertView.findViewById(valueViewId[0]);
			holder.foodName = (TextView) convertView
					.findViewById(valueViewId[1]);
			holder.foodUnitPrice = (TextView) convertView
					.findViewById(valueViewId[2]);
			holder.foodPrice = (TextView) convertView
					.findViewById(valueViewId[3]);
			holder.btAdd = (Button) convertView.findViewById(valueViewId[4]);
			holder.btCut = (Button) convertView.findViewById(valueViewId[5]);
			holder.btDelete = (Button) convertView.findViewById(valueViewId[6]);

			

			convertView.setTag(holder);
		}
		HashMap<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			String number = String.valueOf(appInfo.get(keystring[0]));
			String foodname = (String) appInfo.get(keystring[1]);
			String unitPrice = String.valueOf(appInfo.get(keystring[2]));
			String priceNumPrice = Integer.parseInt(number)
					* Float.parseFloat(unitPrice) + "";
			holder.foodName.setText(foodname);
			holder.number.setText(number);
			holder.foodUnitPrice.setText(unitPrice);
			holder.foodPrice.setText(priceNumPrice);

			holder.btDelete.setOnClickListener(new AdapterOnClickListener(
					position));
			holder.btAdd
					.setOnClickListener(new AdapterOnClickListener(position));
			holder.btCut
					.setOnClickListener(new AdapterOnClickListener(position));
		}
		return convertView;
	}

	// 增加edittext控件中的数据
	public void addNum(int position) {
		int num = (Integer) mAppList.get(position).get(keystring[0]);
		num++;

		mAppList.get(position).put(keystring[0], num);
		MainFragment.addNum(1);
		MainFragment.addPrice(Float.parseFloat(mAppList.get(position).get(Food.FOOD_UNIT_PRICE).toString()));


	}

	// 减少edittext中的数据
	public void cutNum(int position) {
		// 获取一个item里面edittext中的数据源
		int num = (Integer) mAppList.get(position).get(keystring[0]);
		if (num > 0) {
			num--;
			MainFragment.addNum(-1);
			MainFragment.addPrice(-1* Float.parseFloat(mAppList.get(position).get(Food.FOOD_UNIT_PRICE).toString()));


		}
		// 将数据源中改变后的数据重新放进数据源中，再加载到item中
		mAppList.get(position).put(keystring[0], num);


	}

	public void deletNum(int position) {
		int num= Integer.parseInt(mAppList.get(position).get(keystring[0]).toString());
		MainFragment.addNum(-1*num);
		MainFragment.addPrice(-1* Float.parseFloat(mAppList.get(position).get(Food.FOOD_UNIT_PRICE).toString())*num);
		mAppList.get(position).put(keystring[0], 0);
		mAppList.remove(position);
		

	}

	// 按钮监听事件，实现view中的监听事件
	class AdapterOnClickListener implements View.OnClickListener {
		private int position;

		AdapterOnClickListener(int pos) {
			position = pos;
		}

		@Override
		// 复写onClick方法用来监听按钮
		public void onClick(View v) {
			int vid = v.getId();
			if (vid == holder.btAdd.getId()) {
				addNum(position);

			} else if (vid == holder.btCut.getId()) {
				cutNum(position);
			} else if (vid == holder.btDelete.getId()) {
				deletNum(position);
			}

			PopAdapterOrderFood.this.notifyDataSetChanged();
			MainFragment.refreshPopOrderFood();
			

		}
	}

}
