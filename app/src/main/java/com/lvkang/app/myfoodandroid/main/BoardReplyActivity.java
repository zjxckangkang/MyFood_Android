package com.lvkang.app.myfoodandroid.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lvkang.app.myfoodandroid.adapter.ReplyAdapter;
import com.lvkang.app.myfoodandroid.myfood.http.HttpService;
import com.lvkang.app.myfoodandroid.myfood.json.Message;
import com.lvkang.app.myfoodandroid.myfood.json.Reply;
import com.lvkang.app.myfoodandroid.myfood.json.Replys;
import com.lvkang.app.myfoodandroid.myfood.utils.DateTimeUtils;
import com.lvkang.app.myfoodandroid.myfood.utils.DeviceId;
import com.lvkang.app.myfoodandroid.myfood.utils.MyToast;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.myfoodtoandroid.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardReplyActivity extends Activity {
	private static List<Reply> replys = new ArrayList<Reply>();
	private static MyHandler mHandler;
	private static Context mContext;
	private ReplyAdapter replyAdapter;
	private ListView replyListview;
	private ImageButton backButton;
	private Button sendButton;
	private EditText sendEditText;
	private TextView messageBody;
	private TextView messageDateTime;
	private TextView titleTextView;
	private TextView replyTextView;
	private TextView praiseTextView;
	private CheckBox isPraise;
	private View message_head_view_line;
	private Activity thisActivity;
	private String messageDateTimeString;
	private String messageBodyString;
	private static Long messageId;
	private int position;
	private Message message;
	private int dividerHeight;

	private static final int SEND_REPLY_STATE = 0x123;
	private static final int GET_REPLY_STATE = 0x121;
	private static final int SEND_REPLY_SUCCEED = 0;
	private static final int SEND_REPLY_FAILED = -1;
	private static final int GET_REPLY_SUCCEED_MOVE_TO_FIRST = 0;
	private static final int GET_REPLY_SUCCEED_DONT_MOVE_TO_FIRST = 2;

	private static final int GET_REPLY_FAILED = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_activity);
		mContext = this;
		thisActivity = this;
		LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View headerView = lif.inflate(R.layout.message_list_item,
				replyListview, false);
		sendButton = (Button) findViewById(R.id.board_send_button);
		sendButton.setText("评论");
		messageBody = (TextView) headerView
				.findViewById(R.id.message_body_textview);
		messageDateTime = (TextView) headerView
				.findViewById(R.id.message_date_textview);
		sendEditText = (EditText) findViewById(R.id.board_edittext);
		replyListview = (ListView) findViewById(R.id.reply_listview);
		backButton = (ImageButton) findViewById(R.id.titlebar_back_button);
		titleTextView = (TextView) findViewById(R.id.titlebar_activity_introduction);
		titleTextView.setText(getResources().getString(
				R.string.reply_activity_introduction));
		message_head_view_line = headerView
				.findViewById(R.id.message_head_view_line);
		message_head_view_line.setVisibility(View.GONE);
		replyTextView = (TextView) headerView
				.findViewById(R.id.message_reply_textview);
		praiseTextView = (TextView) headerView
				.findViewById(R.id.message_praise_textview);
		isPraise = (CheckBox) headerView.findViewById(R.id.message_is_praise);
		View message_list_item_foot_viewView = headerView
				.findViewById(R.id.message_foot_view);
		message_list_item_foot_viewView.setVisibility(View.VISIBLE);
		replys.clear();

		replyAdapter = new ReplyAdapter(mContext, replys, R.layout.reply_item);

		replyListview.setAdapter(replyAdapter);
		dividerHeight = replyListview.getDividerHeight();
		replyListview.setDividerHeight(-1);

		replyListview.addHeaderView(headerView);

		Intent intent = getIntent();
		mHandler = new MyHandler();
		Bundle bundle = intent.getExtras();
		position = bundle.getInt(BoardFragment.POSITION);

		message = BoardFragment.getMessages().get(position);
		messageId = message.getMessageId();
		messageBodyString = message.getMessageBody();
		messageDateTimeString = message.getMessageDatetime();
		messageBody.setText(messageBodyString);
		messageDateTime.setText(DateTimeUtils.cutDate(messageDateTimeString));

		replyTextView.setText(message.getMessageReply() + "");
		praiseTextView.setText(message.getMessagePraise() + "");
		isPraise.setChecked(message.isMessageIsPraise());
		isPraise.setOnClickListener(new CKListener());

		new Thread(new GetMessageReply()).start();
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(BoardReplyActivity.this,
						MainActivity.class));
				BoardFragment.refreshAllMessage();
				thisActivity.finish();

			}
		});
		sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String replyString = sendEditText.getText().toString();
				if (replyString == null || replyString.isEmpty()) {

				} else {
					new Thread(new SendMessageReplys(replyString)).start();
				}

			}
		});

	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case SEND_REPLY_STATE:
				if (msg.arg1 == SEND_REPLY_SUCCEED) {
					sendEditText.setText("");
					message.setMessageReply(message.getMessageReply() + 1);
					replyTextView.setText(message.getMessageReply() + "");
					new Thread(new GetMessageReply()).start();
				} else {

				}

				break;
			case GET_REPLY_STATE:
				if (msg.arg1 == GET_REPLY_SUCCEED_MOVE_TO_FIRST) {

					mHandler.post(new Runnable() {

						@Override
						public void run() {
							replyListview.smoothScrollToPosition(0);
						}
					});
					replyAdapter.notifyDataSetChanged();
					if (replys.size() > 0) {
						replyListview.setDividerHeight(dividerHeight);

					}

				} else if (msg.arg1 == GET_REPLY_SUCCEED_DONT_MOVE_TO_FIRST) {
					replyAdapter.notifyDataSetChanged();
					if (replys.size() > 0) {
						replyListview.setDividerHeight(dividerHeight);

					}

				} else {

					MyToast.Show(mContext,
							getResources().getString(R.string.internet_failed));
				}

			default:
				break;
			}
		}

	}

	private class CKListener implements CheckBox.OnClickListener {
		@Override
		public void onClick(View v) {
			int praiseNum = message.getMessagePraise();

			if (message.isMessageIsPraise()) {
				message.setMessageIsPraise(false);

				message.setMessagePraise(praiseNum - 1);
				praiseTextView.setText(praiseNum - 1 + "");
				BoardFragment.setMessagePraiseState(message.getMessageId(),
						false);

			} else {
				message.setMessageIsPraise(true);
				message.setMessagePraise(praiseNum + 1);

				praiseTextView.setText(praiseNum + 1 + "");

				BoardFragment.setMessagePraiseState(message.getMessageId(),
						true);
			}

		}
	}

	private class SendMessageReplys implements Runnable {
		private String replyBody;

		SendMessageReplys(String replyBody) {
			this.replyBody = replyBody;

		}

		@Override
		public void run() {
			Reply reply = new Reply();
			reply.setMessageId(messageId);
			reply.setReplyBody(replyBody);
			reply.setReplyUserName(DeviceId.getdeviceIdString(mContext));
			if (HttpService.sendMessageReplys(JSON.toJSONString(reply)) == 0) {
				mHandler.sendMessage(mHandler.obtainMessage(SEND_REPLY_STATE,
						SEND_REPLY_SUCCEED, 0));
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(SEND_REPLY_STATE,
						SEND_REPLY_FAILED, 0));
			}
			;

		}

	}

	private static class GetMessageDontMoveToFirst implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(MainActivity.threadDelay);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String replysString = null;
			replysString = HttpService.getMessageReplys(messageId,
					DeviceId.getdeviceIdString(mContext));
			replys.clear();
			try {

				replys.addAll(JSON.parseObject(replysString, Replys.class)
						.getReplysList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (replysString != null) {
				mHandler.sendMessage(mHandler.obtainMessage(GET_REPLY_STATE,
						GET_REPLY_SUCCEED_DONT_MOVE_TO_FIRST, 0));

			} else {
				mHandler.sendMessage(mHandler.obtainMessage(GET_REPLY_STATE,
						GET_REPLY_FAILED, 0));

			}
		}
	}

	private class GetMessageReply implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(MainActivity.threadDelay);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String replysString = null;
			replysString = HttpService.getMessageReplys(messageId,
					DeviceId.getdeviceIdString(mContext));
			replys.clear();
			try {

				replys.addAll(JSON.parseObject(replysString, Replys.class)
						.getReplysList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (replysString != null) {
				mHandler.sendMessage(mHandler.obtainMessage(GET_REPLY_STATE,
						GET_REPLY_SUCCEED_MOVE_TO_FIRST, 0));

			} else {
				mHandler.sendMessage(mHandler.obtainMessage(GET_REPLY_STATE,
						GET_REPLY_FAILED, 0));

			}
		}
	}

	public static void setReplyPraiseState(final Long replyId,
			final boolean praiseState) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					boolean succeed = HttpService.setReplyPraisestate(replyId,
							praiseState, DeviceId.getdeviceIdString(mContext));
					if (succeed) {

						// new Thread(new GetMessageDontMoveToFirst()).start();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();

	}

	@Override
	protected void onResume() {

		MyApplication.selectedActivity = this.getClass();

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Print.P(this.getClass().getName().toString() + "onDestroy");
		super.onDestroy();
	}

}
