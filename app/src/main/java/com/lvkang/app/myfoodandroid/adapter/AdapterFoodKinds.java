package com.lvkang.app.myfoodandroid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.image.AsyncImageLoader;
import com.lvkang.app.myfoodandroid.myfood.json.Food;
import com.lvkang.app.myfoodandroid.main.MainActivity;
import com.lvkang.app.myfoodandroid.main.MainFragment;
import com.lvkang.app.myfoodandroid.main.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 */
public class AdapterFoodKinds extends BaseAdapter {
	private class buttonViewHolder {
		ImageView itemIcon;
		TextView foodName;
		TextView foodUnitPrice;
		TextView number;
		TextView foodRemarks;
		TextView foodSales;
		TextView foodKind;
		RelativeLayout kindRL;
		TextView foodIntro;
		Button btAdd;
		Button btCut;
		ProgressBar imageViewProProgressBar;
		TextView praise;

	}

	private ArrayList<HashMap<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keystring;
	private int[] valueViewId;
	private buttonViewHolder holder;
	private int listViewItem;
	private AsyncImageLoader imageLoader;

	// 适配一个listview中的item元素
	public AdapterFoodKinds(Context c,
			ArrayList<HashMap<String, Object>> applist, int resources,
			String[] hashmapKey, int[] item) {
		this.listViewItem = resources;

		mAppList = applist;
		mContext = c;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keystring = new String[hashmapKey.length];
		valueViewId = new int[item.length];
		imageLoader = MyApplication.asyncImageLoader;

		System.arraycopy(hashmapKey, 0, keystring, 0, hashmapKey.length);
		System.arraycopy(item, 0, valueViewId, 0, item.length);

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

	@Override
	// 获取listview中每个item中的控件，及设置每个控件中的元素
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (buttonViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(listViewItem, null);
			holder = new buttonViewHolder();
			holder.itemIcon = (ImageView) convertView
					.findViewById(valueViewId[0]);
			holder.foodName = (TextView) convertView
					.findViewById(valueViewId[1]);
			holder.foodUnitPrice = (TextView) convertView
					.findViewById(valueViewId[2]);
			holder.btAdd = (Button) convertView.findViewById(valueViewId[3]);
			holder.btCut = (Button) convertView.findViewById(valueViewId[4]);
			holder.number = (TextView) convertView.findViewById(valueViewId[5]);
			holder.foodRemarks = (TextView) convertView
					.findViewById(valueViewId[6]);
			holder.foodSales = (TextView) convertView
					.findViewById(valueViewId[7]);
			holder.foodKind = (TextView) convertView
					.findViewById(valueViewId[8]);
			holder.imageViewProProgressBar = (ProgressBar) convertView
					.findViewById(valueViewId[9]);
			holder.foodIntro = (TextView) convertView
					.findViewById(valueViewId[10]);
			holder.praise = (TextView) convertView
					.findViewById(valueViewId[11]);
			holder.kindRL = (RelativeLayout) convertView
					.findViewById(valueViewId[12]);

			convertView.setTag(holder);
		}
		HashMap<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			String aname = (String) appInfo.get(keystring[1]);
			String foodImageUrl = MainActivity.IP_STRING
					+ (String) appInfo.get(keystring[0]);
			String price = String.valueOf(appInfo.get(keystring[2]));
			String number = String.valueOf(appInfo.get(keystring[3]));
			String foodRemarks = (String) (appInfo.get(keystring[4]));
			String foodSale = (appInfo.get(keystring[5]) + "");
			String foodAttr = appInfo.get(keystring[6]).toString();
			holder.foodIntro.setText(appInfo.get(keystring[7]).toString());
			holder.praise.setText(appInfo.get(keystring[8]).toString());

			if (!MainFragment.foodMenuArrayListPositionArrayList.get(position)
					.get(Food.FOOD_ATTR).equals(0)) {
				holder.foodKind.setText(foodAttr);
				holder.foodKind.setVisibility(View.VISIBLE);
				holder.kindRL.setVisibility(View.VISIBLE);

			} else {
				holder.foodKind.setVisibility(View.GONE);
				holder.kindRL.setVisibility(View.GONE);

			}
			holder.foodName.setText(aname);
			// holder.itemIcon.setImageDrawable(holder.itemIcon.getResources().getDrawable(mid));
			holder.number.setText(number);
			holder.foodUnitPrice.setText(price);
			holder.foodRemarks.setText(foodRemarks);
			holder.foodSales.setText(foodSale);
			// asyncImageView(foodJpg, holder.itemIcon);
			holder.itemIcon.setTag(foodImageUrl);
			holder.itemIcon.setImageBitmap(null);

			if (!TextUtils.isEmpty(foodImageUrl)) {

				Bitmap bitmap = imageLoader.loadImage(holder.itemIcon,
						foodImageUrl, holder.imageViewProProgressBar);
				if (bitmap != null) {
					holder.imageViewProProgressBar.setVisibility(View.GONE);
					holder.itemIcon.setImageBitmap(bitmap);
				} else {
					holder.imageViewProProgressBar.setVisibility(View.GONE);

				}

				Log.i("TAG", foodImageUrl);

			} else {
				holder.imageViewProProgressBar.setVisibility(View.GONE);

			}

			// 预设一个图片

			holder.btAdd.setOnClickListener(new lvButtonListener(position));
			holder.btCut.setOnClickListener(new lvButtonListener(position));

		}
		return convertView;
	}

	// 增加edittext控件中的数据
	public void addNum(int position) {
		int num = (Integer) mAppList.get(position).get(keystring[3]);
		num++;
		MainFragment.addNum(1);
		MainFragment.addPrice(Float.parseFloat(mAppList.get(position)
				.get(keystring[2]).toString()));

		mAppList.get(position).put(keystring[3], num);

	}

	// 减少edittext中的数据
	public void cutNum(int position) {
		// 获取一个item里面edittext中的数据源
		int num = (Integer) mAppList.get(position).get(keystring[3]);
		if (num > 0) {
			num--;
			MainFragment.addNum(-1);
			MainFragment.addPrice(-1
					* Float.parseFloat(mAppList.get(position).get(keystring[2])
					.toString()));

		}
		// 将数据源中改变后的数据重新放进数据源中，再加载到item中
		mAppList.get(position).put(keystring[3], num);

	}

	// 按钮监听事件，实现view中的监听事件
	class lvButtonListener implements View.OnClickListener {
		private int position;

		lvButtonListener(int pos) {
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
			}
			AdapterFoodKinds.this.notifyDataSetChanged();

		}

	}
}
