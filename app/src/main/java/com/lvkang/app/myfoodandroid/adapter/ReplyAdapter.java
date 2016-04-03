package com.lvkang.app.myfoodandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.myfood.json.Reply;
import com.lvkang.app.myfoodandroid.main.BoardReplyActivity;
import com.lvkang.app.myfoodandroid.myfood.utils.DateTimeUtils;
import com.lvkang.myfoodtoandroid.R;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends BaseAdapter {
	Context mContext;
	private List<Reply> replys = new ArrayList<Reply>();

	private LayoutInflater mInflater;
	private orderFoodHolder orderFoodHolder;
	private int itemResources;

	private class orderFoodHolder {
		TextView replyBody;
		TextView replyTime;
		TextView replyPraise;
		TextView replyFloor;
		CheckBox isPraiseBox;

	}

	// 这里传入的数据是List<OrderFood>
	public ReplyAdapter(Context c, List<Reply> replys, int itemResources) {
		this.mContext = c;
		this.replys = replys;
		this.itemResources = itemResources;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return replys.size();
	}

	@Override
	public Object getItem(int position) {
		return replys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			orderFoodHolder = (orderFoodHolder) convertView.getTag();
		} else {

			convertView = mInflater.inflate(itemResources, null);
			orderFoodHolder = new orderFoodHolder();

			orderFoodHolder.replyBody = (TextView) convertView
					.findViewById(R.id.reply_body_textview);
			orderFoodHolder.replyTime = (TextView) convertView
					.findViewById(R.id.reply_date_textview);
			orderFoodHolder.replyPraise = (TextView) convertView
					.findViewById(R.id.reply_praise_textview);
			orderFoodHolder.replyFloor = (TextView) convertView
					.findViewById(R.id.reply_floor_textview);
			orderFoodHolder.isPraiseBox = (CheckBox) convertView
					.findViewById(R.id.reply_praise_checkbox);

			convertView.setTag(orderFoodHolder);
		}

		if (replys != null) {
			try {
				String dateTimeString = DateTimeUtils.cutDate(replys.get(
						position).getReplyTime());
				orderFoodHolder.replyBody.setText(replys.get(position)
						.getReplyBody());

				orderFoodHolder.replyTime.setText(dateTimeString);

				orderFoodHolder.replyPraise.setText(replys.get(position)
						.getReplyPraise() + "");
				orderFoodHolder.replyFloor
						.setText((getCount() - position) + "");
				orderFoodHolder.isPraiseBox.setChecked(replys.get(position)
						.isPraise());
				orderFoodHolder.isPraiseBox.setOnClickListener(new CKListener(
						position));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return convertView;

	}

	private class CKListener implements CheckBox.OnClickListener {
		int position;

		CKListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			try {
				if (replys.get(position).isPraise()) {

					int praiseNum = replys.get(position).getReplyPraise();
					if (praiseNum > 0) {

						replys.get(position).setReplyPraise(praiseNum - 1);
						replys.get(position).setPraise(false);
						BoardReplyActivity.setReplyPraiseState(
								replys.get(position).getReplyId(), false);
					}

				} else {
					replys.get(position).setReplyPraise(
							replys.get(position).getReplyPraise() + 1);

					replys.get(position).setPraise(true);
					BoardReplyActivity.setReplyPraiseState(replys.get(position)
							.getReplyId(), true);

				}
				ReplyAdapter.this.notifyDataSetChanged();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
