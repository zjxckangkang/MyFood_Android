package com.lvkang.app.myfoodandroid.myfood.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtils {
	public static String getMenuInfo(Context context) {
		SharedPreferences pref = context.getSharedPreferences("user_login", 0);
		return pref.getString("menu_info", "");
	}

	public static boolean saveMenuInfo(Context context, String info) {
		SharedPreferences pref = context.getSharedPreferences("user_login", 0);
		return pref.edit().putString("menu_info", info).commit();
	}

	public static boolean saveIsStarted(Context context) {
		SharedPreferences pref = context
				.getSharedPreferences("isFirstStart", 0);
		return pref.edit().putBoolean("isFirstStart", false).commit()
				&& saveIsStartedVersion(context);
	}

	public static boolean saveIsStartedVersion(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				"isFirstStartVersion", 0);
		try {
			return pref
					.edit()
					.putInt("isFirstStartVersion",
							VersionUtils.getVersionCode(context)).commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static int getSavedVersion(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				"isFirstStartVersion", 0);
		return pref.getInt("isFirstStartVersion", 1);

	}

	public static boolean isFirstStarted(Context context) {
		SharedPreferences pref = context
				.getSharedPreferences("isFirstStart", 0);
		return pref.getBoolean("isFirstStart", true);
		// return true;
	}

	public static boolean NeedShowWelcomePage(Context context) {

		try {
			if (getSavedVersion(context) != VersionUtils.getVersionCode(context)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return isFirstStarted(context);
	}
}
