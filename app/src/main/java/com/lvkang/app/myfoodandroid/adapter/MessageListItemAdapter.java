package com.lvkang.app.myfoodandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.myfood.json.Message;
import com.lvkang.app.myfoodandroid.myfood.main.BoardFragment;
import com.lvkang.app.myfoodandroid.myfood.utils.DateTimeUtils;
import com.lvkang.myfoodtoandroid.R;

import java.util.ArrayList;
import java.util.List;

public class MessageListItemAdapter extends BaseAdapter {
	private List<Message> messageList = new ArrayList<Message>();
	private Context mContext;
	private Holder holder;
	private LayoutInflater mInflater;

	private class Holder {
		private TextView dateTextView;
		private TextView replyTextView;
		private TextView messageTextView;
		private TextView praiseTextView;
		private CheckBox isPraiseCheckBox;

	}

	public MessageListItemAdapter(Context mContext, List<Message> messages) {
		this.mContext = mContext;
		this.messageList = messages;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (Holder) convertView.getTag();

		} else {
			convertView = mInflater.inflate(R.layout.message_list_item, null);
			holder = new Holder();
			View message_head_view = convertView
					.findViewById(R.id.message_head_view);
			message_head_view.setVisibility(View.VISIBLE);
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.message_date_textview);
			holder.messageTextView = (TextView) convertView
					.findViewById(R.id.message_body_textview);
			holder.replyTextView = (TextView) convertView
					.findViewById(R.id.message_reply_textview);
			holder.praiseTextView = (TextView) convertView
					.findViewById(R.id.message_praise_textview);
			holder.isPraiseCheckBox = (CheckBox) convertView
					.findViewById(R.id.message_is_praise);
			convertView.setTag(holder);

		}
		if (messageList != null) {

			String dateTimeString = messageList.get(position)
					.getMessageDatetime();
			String dateString = DateTimeUtils.cutDate(dateTimeString);
			// String timeString = dateTimeString.substring(11, 19);
			holder.replyTextView.setText(messageList.get(position)
					.getMessageReply() + "");
			holder.praiseTextView.setText(messageList.get(position)
					.getMessagePraise() + "");
			holder.isPraiseCheckBox.setChecked(messageList.get(position)
					.isMessageIsPraise());
			holder.dateTextView.setText(dateString);
			holder.messageTextView.setText(messageList.get(position)
					.getMessageBody());
			holder.isPraiseCheckBox
					.setOnClickListener(new CKListener(position));
			return convertView;
		}

		return null;
	}

	private class CKListener implements CheckBox.OnClickListener {
		int position;

		CKListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {

			try {
				if (messageList.size() > position) {

					if (messageList.get(position).isMessageIsPraise()) {
						int praiseNum = messageList.get(position)
								.getMessagePraise();
						if (praiseNum > 0) {
							messageList.get(position).setMessagePraise(
									praiseNum - 1);

							messageList.get(position).setMessageIsPraise(false);
							BoardFragment.setMessagePraiseState(messageList
									.get(position).getMessageId(), false);

						}

					} else {
						messageList.get(position)
								.setMessagePraise(
										messageList.get(position)
												.getMessagePraise() + 1);

						messageList.get(position).setMessageIsPraise(true);

						BoardFragment.setMessagePraiseState(
								messageList.get(position).getMessageId(), true);

					}
					MessageListItemAdapter.this.notifyDataSetChanged();
				}

			} catch (Exception e) {
			}

		}
	}

}
