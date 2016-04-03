package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.dialog.MyAlertDialog;
import com.lvkang.app.myfoodandroid.dialog.MyProgressDialog;
import com.lvkang.app.myfoodandroid.myfood.http.HttpService;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.app.myfoodandroid.myfood.utils.VersionUtils;
import com.lvkang.myfoodtoandroid.R;

import java.io.IOException;
import java.lang.reflect.Field;

public class AboutActivity extends Activity {

	private static final int STATE_UPDATE = 0x123;
	private static final int NEED_UPDATE = 1;
	private static final int DONOT_NEED_UPDATE = 0;
	private static final int CANT_UPDATE = -1;

	private TextView versionTextview, titleTextView;
	private ImageButton backButton;
	private Button checkUpdate;
	private MyAlertDialog needUpdateAlertDialog;
	private MyProgressDialog updateProgressDialog;
	private boolean needUpdate;
	private AboutHandler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		mHandler = new AboutHandler();
		versionTextview = (TextView) findViewById(R.id.about_activity_verision_textview);
		titleTextView = (TextView) findViewById(R.id.titlebar_activity_introduction);
		backButton = (ImageButton) findViewById(R.id.titlebar_back_button);
		checkUpdate = (Button) findViewById(R.id.about_check_update);
		titleTextView.setText(getResources().getString(
				R.string.about_activity_introduction));
		initUpdateProgressDialog();
		String versionString = "";
		try {
			versionString = VersionUtils.getVersionString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		versionTextview.setText(getResources().getString(R.string.version_name)
				+ versionString);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AboutActivity.this, MainActivity.class));
			}
		});
		checkUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateProgressDialog.showProgressDialog();
				new Thread(new checkUpdate()).start();
			}
		});

	}

	private class checkUpdate implements Runnable {

		@Override
		public void run() {

			try {
				Thread.sleep(MainActivity.threadDelay);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {

				needUpdate = HttpService.needUpdate();
				if (needUpdate) {
					mHandler.sendMessage(mHandler.obtainMessage(STATE_UPDATE,
							NEED_UPDATE, 0));
				} else {
					mHandler.sendMessage(mHandler.obtainMessage(STATE_UPDATE,
							DONOT_NEED_UPDATE, 0));

				}
			} catch (IOException e) {
				mHandler.sendMessage(mHandler.obtainMessage(STATE_UPDATE,
						CANT_UPDATE, 0));
				e.printStackTrace();
			}

		}

	}

	private class AboutHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case STATE_UPDATE:
				updateProgressDialog.dismissProgressDialog();

				if (msg.arg1 == NEED_UPDATE) {
					showNeedUpdateAlertDialog(NEED_UPDATE);

				} else if (msg.arg1 == DONOT_NEED_UPDATE) {
					showNeedUpdateAlertDialog(DONOT_NEED_UPDATE);

				} else {
					showNeedUpdateAlertDialog(CANT_UPDATE);

				}

				break;

			default:
				break;
			}
		}

	}

	private void showNeedUpdateAlertDialog(int checkUpdateState) {
		if (checkUpdateState == NEED_UPDATE) {
			needUpdateAlertDialog = new MyAlertDialog(this, getResources()
					.getString(R.string.find_new_version), getResources()
					.getString(R.string.check_for_update), false);
			needUpdateAlertDialog.getBuilder().setPositiveButton(
					getResources().getString(R.string.go_to_update),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Uri uri = Uri.parse(MainActivity.IP_STRING);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);

							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
		} else if (checkUpdateState == DONOT_NEED_UPDATE) {
			needUpdateAlertDialog = new MyAlertDialog(this, getResources()
					.getString(R.string.dont_find_new_version), getResources()
					.getString(R.string.check_for_update), false);
			needUpdateAlertDialog.getBuilder().setPositiveButton(
					getResources().getString(R.string.ensure),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							needUpdateAlertDialog.deleteAlertDialog();

							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});

		} else if (checkUpdateState == CANT_UPDATE) {
			needUpdateAlertDialog = new MyAlertDialog(this, getResources()
					.getString(R.string.check_update_failed), getResources()
					.getString(R.string.check_for_update), false);
			needUpdateAlertDialog.getBuilder().setPositiveButton(
					getResources().getString(R.string.ensure),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							needUpdateAlertDialog.deleteAlertDialog();
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});

		}

		needUpdateAlertDialog.creatAlertDialog();
		needUpdateAlertDialog.showAlertDialog();

	}

	private void initUpdateProgressDialog() {
		updateProgressDialog = new MyProgressDialog(this, getResources()
				.getString(R.string.please_wait), getResources().getString(
				R.string.checking_update), false, false);

	}

	@Override
	protected void onResume() {

		super.onResume();
		MyApplication.selectedActivity = this.getClass();
		Print.P("onResume", MyApplication.selectedActivity.getName());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		updateProgressDialog.deleteProgressDialog();

		super.onDestroy();
	}

}
