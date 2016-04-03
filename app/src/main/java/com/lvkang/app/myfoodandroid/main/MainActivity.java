package com.lvkang.app.myfoodandroid.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.fragment.IndicatorFragmentActivity;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.myfoodtoandroid.R;

import java.util.List;

public class MainActivity extends IndicatorFragmentActivity {
//	public final static String IP_STRING = "http://zjxckangkang.oicp.net:10000/MyFood/";
//	public final static String IP_STRING = "http://192.168.1.188:8080/MyFood/";
	public final static String IP_STRING = "http://192.168.1.102:8080/MyFood/";
	public final static String EMAIL_ADDR = "mailto:625383327@qq.com";
	public final static int REFRSH_DELAY=500;

	private static ImageButton refreshButton;
	private ImageButton searchButton;
	private ImageButton menuButton;
	public static MyHandler mHandler;
	private int selectedPage;
	private View parentView, contentView;
	private PopupWindow popupWindow;
	private TextView aboutTextView, adviceTextView, introductionTextView;

	private static ProgressBar refreshProgressBar;
	public static Context mContext;
	public static final int MAIN_FRAGMENT = 1;
	public static final int BOARD_FRAGMENT = 0;
	public static final int ORDERED_FRAGMENT = 2;
	public static final int threadDelay = 500;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initPopupWindow();
		mHandler = new MyHandler();
		refreshButton = (ImageButton) findViewById(R.id.title_progressbar_imagebutton);
		refreshButton.setVisibility(View.VISIBLE);
		searchButton = (ImageButton) findViewById(R.id.title_search_imagebutton);
		searchButton.setVisibility(View.VISIBLE);
		menuButton = (ImageButton) findViewById(R.id.title_menu_icon);
		refreshProgressBar = (ProgressBar) findViewById(R.id.title_progressbar);
		refreshButton.setOnClickListener(new myButtonOnClickListener());
		searchButton.setOnClickListener(new myButtonOnClickListener());
		menuButton.setOnClickListener(new myButtonOnClickListener());

	}
 
	private static class RefreshStateOKRunnable implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(REFRSH_DELAY);
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						refreshButton.setVisibility(View.VISIBLE);
						refreshProgressBar.setVisibility(View.GONE);

					}
				});

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class MyHandler extends Handler {

	}

	public static void setRefreshStateOK() {
		new Thread(new RefreshStateOKRunnable()).start();

	}

	public static void startRefresh(int selectedPage) {
		startRefreshState();

		switch (selectedPage) {
		case BOARD_FRAGMENT:
			new Thread(BoardFragment.refreshMessageRunnable).start();
			break;
		case MAIN_FRAGMENT:
			new Thread(MainFragment.getFoodsJsonThread).start();

			break;
		case ORDERED_FRAGMENT:
			new Thread(OrderedFragment.getOrderedFoodListrunnable)

			.start();

			break;

		default:
			break;
		}

	}

	public static void startRefreshState() {
		refreshButton.setVisibility(View.GONE);

		refreshProgressBar.setVisibility(View.VISIBLE);

	}

	public class myButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.title_progressbar_imagebutton:
				startRefresh(selectedPage);

				break;
			case R.id.title_search_imagebutton:
				startActivity(new Intent(MainActivity.this,
						SearchActivity.class));
				break;

			case R.id.title_menu_icon:

				openPopupWindow();
				break;
			case R.id.about:
				startActivity(new Intent(MainActivity.this, AboutActivity.class));

				closePopupWindow();
				break;
			case R.id.advice:
				Intent data = new Intent(Intent.ACTION_SENDTO);
				data.setData(Uri.parse(EMAIL_ADDR));
				data.putExtra(Intent.EXTRA_SUBJECT,
						getResources().getString(R.string.advice_to_myfood_app));
				startActivity(data);
				closePopupWindow();

				break;
			case R.id.introduction:
				closePopupWindow();
				startActivity(new Intent(MainActivity.this,
						WelcomeActivity.class));

				break;

			default:
				break;
			}

		}

	}

	private void closePopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();

		}
	}

	private void initPopupWindow() {

		contentView = getLayoutInflater().inflate(R.layout.menu_popupwindow,
				null);

		parentView = findViewById(R.id.titlebar);

		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	private void openPopupWindow() {

		int[] location = new int[2];
		parentView.getLocationOnScreen(location);
		int[] location2 = new int[2];
		menuButton.getLocationOnScreen(location2);
		popupWindow.setWindowLayoutMode(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		aboutTextView = (TextView) contentView.findViewById(R.id.about);
		adviceTextView = (TextView) contentView.findViewById(R.id.advice);
		introductionTextView = (TextView) contentView
				.findViewById(R.id.introduction);
		aboutTextView.setOnClickListener(new myButtonOnClickListener());
		adviceTextView.setOnClickListener(new myButtonOnClickListener());
		introductionTextView.setOnClickListener(new myButtonOnClickListener());
		popupWindow.showAtLocation(menuButton, Gravity.NO_GRAVITY,
				(int) (location2[0] / 2), location[1] + parentView.getHeight()
						+ 20);

		// popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 200,
		// location[1] - height);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		super.onPageScrollStateChanged(state);
	}

	@Override
	public void onPageSelected(int position) {
		super.onPageSelected(position);
		selectedPage = position;
		switch (selectedPage) {
		case MAIN_FRAGMENT:
			if (searchButton != null) {

				searchButton.setVisibility(View.VISIBLE);
			}

			break;

		default:
			if (searchButton != null) {

				searchButton.setVisibility(View.GONE);
			}

			break;
		}

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (popupWindow!=null) {
			popupWindow=null;
		}
	}

	@Override
	protected void onResume() {
		MyApplication.selectedActivity = this.getClass();

		Print.P("onResume", MyApplication.selectedActivity.getName());
		super.onResume();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

		super.onPageScrolled(position, positionOffset, positionOffsetPixels);

	}

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(BOARD_FRAGMENT, getResources().getString(
				R.string.board_fragment), BoardFragment.class));
		tabs.add(new TabInfo(MAIN_FRAGMENT, getResources().getString(
				R.string.main_fragment), MainFragment.class));
		tabs.add(new TabInfo(ORDERED_FRAGMENT, getResources().getString(
				R.string.ordered_fragment), OrderedFragment.class));

		return MAIN_FRAGMENT;
	}

}
