package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lvkang.app.myfoodandroid.library.ILoadingLayout;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.lvkang.app.myfoodandroid.library.PullToRefreshBase.OnRefreshListener;
import com.lvkang.app.myfoodandroid.library.PullToRefreshListView;
import com.lvkang.app.myfoodandroid.adapter.MessageListItemAdapter;
import com.lvkang.app.myfoodandroid.myfood.http.HttpService;
import com.lvkang.app.myfoodandroid.myfood.json.Message;
import com.lvkang.app.myfoodandroid.myfood.json.Messages;
import com.lvkang.app.myfoodandroid.myfood.utils.DeviceId;
import com.lvkang.app.myfoodandroid.myfood.utils.MyToast;
import com.lvkang.myfoodtoandroid.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {
	public static final String POSITION="position";
	private static Context mContext;
	private View mMainView;
	private View footView;
	private View internetFailedView;
	private TextView footTextView;
	private Button sendButton;
	private EditText inputEditText;
	private SendMessageRunnable sendMessageRunnable;
	public static RefreshMessageRunnable refreshMessageRunnable;
	private static final int OFFSET = 20;
	private static final Long maxMessageId = 999999999L;
	private boolean isUpdate = false;
	private ILoadingLayout startLabels;

	private static MyHandler mUiHandler;
	private MessageListItemAdapter messageListItemAdapter;
	private PullToRefreshListView messageListview;
	private static List<Message> messages = new ArrayList<Message>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mUiHandler = new MyHandler();

		sendButton = (Button) mMainView.findViewById(R.id.board_send_button);
		inputEditText = (EditText) mMainView.findViewById(R.id.board_edittext);
		messageListview = (PullToRefreshListView) mMainView
				.findViewById(R.id.board_listview);

		LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		footView = lif.inflate(R.layout.listview_pull_down_refresh_foot,
				messageListview.getRefreshableView(), false);
		messageListview.getRefreshableView().addFooterView(footView);
		internetFailedView = mMainView
				.findViewById(R.id.board_internet_failed_include);

		footTextView = (TextView) footView
				.findViewById(R.id.listview_refresh_foot);
		messageListItemAdapter = new MessageListItemAdapter(mContext, messages);
		messageListview.setAdapter(messageListItemAdapter);
		startLabels = messageListview.getLoadingLayoutProxy(true, false);
		messageListview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new Thread(refreshMessageRunnable).start();
				MainActivity.startRefreshState();

			}
		});
		messageListview
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						updateMessages();
					}
				});
		messageListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// header influence
				position = position - 1;
				Intent intent = new Intent(MainActivity.mContext,
						BoardReplyActivity.class);
				intent.putExtra(POSITION, position);
				startActivity(intent);

			}
		});
		sendButton.setOnClickListener(new ClickListener());
		sendMessageRunnable = new SendMessageRunnable();
		refreshMessageRunnable = new RefreshMessageRunnable();
		new Thread(new RefreshMessageRunnable(true)).start();

		footView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateMessages();
			}
		});

	}

	private void setStartLableTime() {
		String label = DateUtils.formatDateTime(
				mContext.getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		startLabels.setLastUpdatedLabel("更新于:" + label);
	}

	private void updateMessages() {
		if (isUpdate == false) {
			isUpdate = true;
			footTextView.setText(R.string.loading_more);
			if (messages != null && messages.size() > 0) {

				new Thread(new RefreshMessageRunnable(messages.get(
						messages.size() - 1).getMessageId())).start();
			} else {

				new Thread(new RefreshMessageRunnable(maxMessageId - 1))
						.start();

			}
		}
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x123:
				// 发送成功
				new Thread(refreshMessageRunnable).start();
				inputEditText.setText("");
				break;
			case 0x023:
				// 发送失败
				MyToast.Show(mContext,
						getResources().getString(R.string.internet_failed));
				break;
			case 0x145:
				if (msg.obj != null) {
					// 更新成功
					String string = msg.obj.toString();
					try {
						List<Message> newMessages = JSON.parseObject(
								msg.obj.toString(), Messages.class)
								.getMessageList();
						messages.clear();
						messages.addAll(newMessages);

						if (messages.size() > 0) {

							mUiHandler.post(new Runnable() {

								@Override
								public void run() {
									messageListview.getRefreshableView()
											.smoothScrollToPosition(0);

								}
							});
							messageListItemAdapter.notifyDataSetChanged();
						}

						if (newMessages.size() < OFFSET) {

							footTextView.setText(R.string.no_more);
						} else {
							footTextView.setText(R.string.load_more);

						}

						setStartLableTime();

						internetFailedView.setVisibility(View.GONE);

					} catch (Exception e) {
						// 更新失败
						e.printStackTrace();
						footTextView.setText(R.string.loading_more_failed);
						MyToast.Show(
								mContext,
								getResources().getString(
										R.string.internet_failed));

					}
				} else {
					// 更新失败
					footTextView.setText(R.string.loading_more_failed);
					MyToast.Show(mContext,
							getResources().getString(R.string.internet_failed));

				}
				messageListview.onRefreshComplete();
				MainActivity.setRefreshStateOK();
				break;
			case 0x146:
				// 加载成功
				if (msg.obj != null) {
					// 加载成功
					try {
						List<Message> newMessages = new ArrayList<Message>();

						newMessages = JSON.parseObject(msg.obj.toString(),
								Messages.class).getMessageList();
						messages.addAll(newMessages);

						messageListItemAdapter.notifyDataSetChanged();

						if (newMessages.size() < OFFSET) {
							footTextView.setText(R.string.no_more);

						} else {
							footTextView.setText(R.string.load_more);

						}

					} catch (Exception e) {
						// 加载失败

						e.printStackTrace();
						footTextView.setText(R.string.loading_more_failed);
						MyToast.Show(
								mContext,
								getResources().getString(
										R.string.internet_failed));

					}

				} else {
					// 加载失败
					footTextView.setText(R.string.loading_more_failed);
					MyToast.Show(mContext,
							getResources().getString(R.string.internet_failed));

				}

				isUpdate = false;

				break;
			case 0x144:
				if (msg.obj != null) {
					// first refresh succeed

					try {

						messages.clear();
						List<Message> newMessages = JSON.parseObject(
								msg.obj.toString(), Messages.class)
								.getMessageList();
						messages.addAll(newMessages);
						messageListItemAdapter.notifyDataSetChanged();

						if (newMessages.size() < OFFSET) {

							footTextView.setText(R.string.no_more);
						} else {
							footTextView.setText(R.string.load_more);

						}
						internetFailedView.setVisibility(View.GONE);

						setStartLableTime();

					} catch (Exception e) {
						// first refresh failed

						e.printStackTrace();
						footTextView.setText(R.string.loading_more_failed);

					}

				} else {
					// first refresh failed
					footTextView.setText(R.string.loading_more_failed);
				}
				break;
			case 0x456:
				if (msg.obj != null) {
					try {
						messages.clear();
						List<Message> newMessages = JSON.parseObject(
								msg.obj.toString(), Messages.class)
								.getMessageList();
						messages.addAll(newMessages);
						messageListItemAdapter.notifyDataSetChanged();

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

				}
				break;

			default:
				break;
			}
		}

	}

	public static void setMessagePraiseState(final Long messageId,
			final boolean praiseState) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					boolean succeed = HttpService.setMessagePraisestate(
							messageId, praiseState,
							DeviceId.getdeviceIdString(mContext));
					if (succeed) {
//						refreshAllMessage();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();

	}

	public static void refreshAllMessage() {
		new Thread(new RefreshAllMessage(messages.size(), messages.get(0)
				.getMessageId() + 1)).start();
	}

	private class ClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.board_send_button:

				if (inputEditText.getText().toString() != null
						&& inputEditText.getText().toString().length() > 0) {

					new Thread(sendMessageRunnable).start();
				}
				break;

			default:
				break;
			}
		}

	}

	private static class RefreshAllMessage implements Runnable {
		int offset;
		Long lastMessageId;

		RefreshAllMessage(int offset, Long lastMessageId) {
			this.offset = offset;
			this.lastMessageId = lastMessageId;
		}

		@Override
		public void run() {
			String messageString = null;
			try {
				Thread.sleep(MainActivity.threadDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			messageString = HttpService.getBoardMessages(offset, lastMessageId,
					DeviceId.getdeviceIdString(mContext));

			mUiHandler.sendMessage(mUiHandler.obtainMessage(0x456,
					messageString));

		}

	}

	private class RefreshMessageRunnable implements Runnable {
		int offset;
		Long lastMessageId;
		boolean isFirstRefresh = false;

		RefreshMessageRunnable() {
			this(maxMessageId);
		}

		RefreshMessageRunnable(boolean isFirstRefresh) {
			this(maxMessageId);
			this.isFirstRefresh = isFirstRefresh;
		}

		RefreshMessageRunnable(Long messageId) {
			this(OFFSET, messageId);
		}

		RefreshMessageRunnable(int offset, Long messageId) {
			this.lastMessageId = messageId;
			this.offset = offset;
		}

		@Override
		public void run() {
			String messageString = null;
			if (!isFirstRefresh) {
				try {
					Thread.sleep(MainActivity.threadDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			messageString = HttpService.getBoardMessages(offset, lastMessageId,
					DeviceId.getdeviceIdString(mContext));
			if (isFirstRefresh == false) {

				if (lastMessageId < maxMessageId) {
					// 追加

					mUiHandler.sendMessage(mUiHandler.obtainMessage(0x146,
							messageString));

				} else {
					// 更新

					mUiHandler.sendMessage(mUiHandler.obtainMessage(0x145,
							messageString));

				}
			} else {
				// 首次更新

				mUiHandler.sendMessage(mUiHandler.obtainMessage(0x144,
						messageString));

			}

		}

	}

	private class SendMessageRunnable implements Runnable {

		@Override
		public void run() {
			Message message = new Message();
			message.setMessageBody(inputEditText.getText().toString());
			message.setMessageUserName(DeviceId
					.getdeviceIdString(getActivity()));

			String messageString = JSON.toJSONString(message);
			try {
				HttpService.sendBoardMessage(messageString);
				mUiHandler.sendMessage(mUiHandler.obtainMessage(0x123));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				mUiHandler.sendMessage(mUiHandler.obtainMessage(0x023));

				e.printStackTrace();
			}

		}

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mMainView = inflater.inflate(R.layout.board_fragment, container, false);
		return mMainView;

	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mContext = activity.getApplicationContext();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public static List<Message> getMessages() {
		return messages;
	}

	public static void setMessages(List<Message> messages) {
		BoardFragment.messages = messages;
	}

}
