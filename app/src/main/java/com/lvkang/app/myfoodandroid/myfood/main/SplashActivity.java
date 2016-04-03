package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.lvkang.app.myfoodandroid.myfood.utils.SharePreferenceUtils;
import com.lvkang.myfoodtoandroid.R;

public class SplashActivity extends Activity {
	private Handler mHandler;
	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity=this;
		setContentView(R.layout.splash_activity);
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (SharePreferenceUtils.NeedShowWelcomePage(getApplicationContext())) {
					// first start
					startActivity(new Intent(SplashActivity.this,
							WelcomeActivity.class));
					mActivity.finish();
					
				} else {
					// normall start
					startActivity(new Intent(SplashActivity.this,
							MyApplication.selectedActivity));
					mActivity.finish();


				}
			}
		}, 1000);

	}

	@Override
	protected void onResume() {

		super.onResume();
	}

}
