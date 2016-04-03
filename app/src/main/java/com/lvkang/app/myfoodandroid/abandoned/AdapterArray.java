package com.lvkang.app.myfoodandroid.abandoned;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.main.MainFragment;
import com.lvkang.myfoodtoandroid.R;

import java.util.List;

public class AdapterArray extends ArrayAdapter<String> {
	Context mContext;
	int resource;
	int textViewResourceId;
	private buttonViewHolder holder;
	List<String> strings;
	List<String> strings2;
	private LayoutInflater mInflater;
	public static int  selectedPositon;
	public static String selectedKind;

	private class buttonViewHolder {
		TextView mTextView;

	}

	public AdapterArray(Context context, int resource,
			int textViewResourceId, List<String> strings,List<String> strings2) {
		super(context, resource, textViewResourceId, strings);
		
		this.mContext = context;
		this.resource = resource;

		this.textViewResourceId = textViewResourceId;
		this.strings = strings;
		this.strings2=strings2;
		selectedPositon=strings2.size();		
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (strings.size()>0) {
			selectedKind=strings.get(0);
			
		}
		

	}

	@Override
	public void setNotifyOnChange(boolean notifyOnChange) {
		super.setNotifyOnChange(notifyOnChange);
	}

	@Override
	public int getCount() {
		return strings.size()+strings2.size();
	}

	@Override
	public int getPosition(String item) {
		return super.getPosition(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView!=null){
			holder=(buttonViewHolder)convertView.getTag();
			
		}else {
			convertView=mInflater.inflate(resource, null);
			holder=new buttonViewHolder();
			holder.mTextView=(TextView) convertView.findViewById(textViewResourceId);
			convertView.setTag(holder);
			
		}
		if (position==selectedPositon) {
			holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.mythem));
			holder.mTextView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		}else {
			holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.gray));

			holder.mTextView.setBackgroundColor(mContext.getResources().getColor(R.color.mygraywhite));

		}
		String str;
		if (position-strings2.size()<0) {
			str=strings2.get(position);
		}else {
			str = strings.get(position - strings2.size());
			
		}
		holder.mTextView.setText(str);
		holder.mTextView.setOnClickListener(new buttonListener(position));
		return convertView;
		
		
		
	}

	private class buttonListener implements View.OnClickListener {

		private int position;

		buttonListener(int position) {
			this.position = position;

		}

		
		@Override
		public void onClick(View v) {
			selectedPositon=position;
		AdapterArray.this.notifyDataSetChanged();
		MainFragment.changeAdapter(position);
		}

	}

}
