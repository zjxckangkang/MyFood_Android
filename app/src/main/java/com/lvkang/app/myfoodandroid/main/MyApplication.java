package com.lvkang.app.myfoodandroid.main;

import android.app.Activity;
import android.app.Application;

import com.lvkang.app.myfoodandroid.image.AsyncImageLoader;

public class MyApplication extends Application {
	public static AsyncImageLoader asyncImageLoader;
	public static Class<? extends Activity> selectedActivity=MainActivity.class;

	@Override
	public void onCreate() {
		super.onCreate();
		asyncImageLoader = new AsyncImageLoader(getApplicationContext());
		
	}

}
