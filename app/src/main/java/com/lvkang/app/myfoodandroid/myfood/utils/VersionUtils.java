package com.lvkang.app.myfoodandroid.myfood.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtils {
	public static int getVersionCode(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		int version = packInfo.versionCode;
		return version;
	}

	public static String getVersionString(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

}
