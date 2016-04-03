package com.lvkang.app.myfoodandroid.myfood.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class DeviceId {

	public static String getdeviceIdString(Context mContext) {
		String deviceId="default";
		try{
			TelephonyManager tm = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);

			final String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
					mContext.getContentResolver(),
					android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			 deviceId = deviceUuid.toString();
		}catch (Exception ex){

			ex.printStackTrace();
		}


		return deviceId;

	}

}
