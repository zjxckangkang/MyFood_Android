package com.lvkang.app.myfoodandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.main.MainFragment;
import com.lvkang.myfoodtoandroid.R;

import java.util.List;

public class AdapterKind extends BaseAdapter {

	Context mContext;
	int resource;
	int textViewResourceId;
	int viewResourcesId;
	int rightViewId;
	private ViewHolder holder;
	List<String> kindsList;
	List<String> attrArrayList;
	private LayoutInflater mInflater;
	public static int selectedPositon;
	public static String selectedKind;

	private class ViewHolder {
		TextView mTextView;
		View simpleView;
		View rightView;

	}

	public AdapterKind(Context context, int resource, int textViewResourceId,
			int viewResourcesId, int rightViewId, List<String> kindsList,
			List<String> attrArrayList) {

		this.mContext = context;
		this.resource = resource;
		this.rightViewId = rightViewId;
		this.textViewResourceId = textViewResourceId;
		this.viewResourcesId = viewResourcesId;
		this.kindsList = kindsList;
		this.attrArrayList = attrArrayList;
		selectedPositon = attrArrayList.size();
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (kindsList.size() > 0) {
			selectedKind = kindsList.get(0);

		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();

		} else {
			convertView = mInflater.inflate(resource, null);
			holder = new ViewHolder();
			holder.mTextView = (TextView) convertView
					.findViewById(textViewResourceId);
			holder.simpleView = (View) convertView
					.findViewById(viewResourcesId);

			holder.rightView = (View) convertView.findViewById(rightViewId);

			convertView.setTag(holder);

		}

		if (position == selectedPositon) {

			holder.simpleView.setBackgroundColor(mContext.getResources()
					.getColor(R.color.mythem));

			holder.mTextView.setTextColor(mContext.getResources().getColor(
					R.color.mythem));
			holder.mTextView.setBackgroundColor(mContext.getResources()
					.getColor(R.color.white));
			holder.rightView.setBackgroundColor(mContext.getResources()
					.getColor(R.color.white));
		} else {
			holder.simpleView.setBackgroundColor(mContext.getResources()
					.getColor(R.color.wechat_conversation_backgroud));

			holder.mTextView.setTextColor(mContext.getResources().getColor(
					R.color.gray));
			holder.rightView.setBackgroundColor(mContext.getResources()
					.getColor(R.color.wechat_line_gray));

			holder.mTextView.setBackgroundColor(mContext.getResources()
					.getColor(R.color.wechat_conversation_backgroud));

		}
		String str=getString(position);
		holder.mTextView.setText(str);
		holder.mTextView.setOnClickListener(new buttonListener(position));
		return convertView;

	}
	private String getString(int position){
		String str="";
		if (position - attrArrayList.size() < 0) {
			str = attrArrayList.get(position);
		} else {
			str = kindsList.get(position - attrArrayList.size());

		}
		return str;
	}

	private class buttonListener implements View.OnClickListener {

		private int position;

		buttonListener(int position) {
			this.position = position;

		}

		@Override
		public void onClick(View v) {
			selectedPositon = position;
			AdapterKind.this.notifyDataSetChanged();
			MainFragment.setKindHintText(getString(position));
			MainFragment.changeAdapter(position);
			
		}

	}

	@Override
	public int getCount() {
		return kindsList.size() + attrArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
