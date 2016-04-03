package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lvkang.app.myfoodandroid.adapter.ViewPagerAdapter;
import com.lvkang.app.myfoodandroid.myfood.utils.SharePreferenceUtils;
import com.lvkang.myfoodtoandroid.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	private ImageView imageViewGoHome;
	private Handler handler;
	private Activity thisActivity;

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private static final int OFFSET = 3;
	private LayoutInflater inflater;

	// 引导图片资源
	// 底部小店图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = this;
		setContentView(R.layout.welcome_viewpager);
		inflater = getLayoutInflater();
		SharePreferenceUtils.saveIsStarted(getApplicationContext());

		views = new ArrayList<View>();

		View view1 = inflater.inflate(R.layout.welcome_view1, null);
		View view2 = inflater.inflate(R.layout.welcome_view2, null);
		View view3 = inflater.inflate(R.layout.welcome_view3, null);
		handler = new Handler();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		imageViewGoHome = (ImageView) view3
				.findViewById(R.id.welcome_imageview_gohome);
		view3.setOnClickListener(new listener());
		imageViewGoHome.setOnClickListener(new listener());
		vp = (ViewPager) findViewById(R.id.viewpager);
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);

		// 初始化底部小点
		initDots();

	}

	private class listener implements OnClickListener {

		@Override
		public void onClick(View v) {

			startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

			new Thread(new destoryView()).start();

		}

	}

	private class destoryView implements Runnable {

		@Override
		public void run() {

			try {
				Thread.sleep(2000);

				handler.post(new Runnable() {

					@Override
					public void run() {
						for (int i = 0; i < OFFSET; i++) {
							vpAdapter.destroyItem(vp, i, null);

						}
					}
				});
				thisActivity.finish();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		MyApplication.selectedActivity = this.getClass();
		super.onResume();

	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[OFFSET];

		// 循环取得小点图片
		for (int i = 0; i < OFFSET; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	/**
	 * 设置当前的引导页
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= OFFSET) {
			return;
		}

		vp.setCurrentItem(position);
	}

	/**
	 * 这只当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > OFFSET - 1 || currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态

		setCurDot(arg0);
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}

}
