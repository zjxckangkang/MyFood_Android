package com.lvkang.app.myfoodandroid.myfood.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	public static void main(String[] args) {
		System.out.println("main");
	}

	public static String cutDate(String dateString) {
		int[] date = new int[4];
		String resultString=null;

		String string = "2015-06-03 23:59:59.0".substring(0, 19);
		System.out.println(string);
		String pattern = "yyy-MM-dd HH:mm:ss"; // 首先定义时间格式
		SimpleDateFormat format = new SimpleDateFormat(pattern);// 然后创建一个日期格式化类
		Date convertResult = null;
		try {
			convertResult = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date today = new Date();
		long diff = (today.getTime() - convertResult.getTime()) / 1000;
		System.out.println(diff);
		int days = (int) (diff / (60 * 60 * 24));
		diff = diff - (60 * 60 * 24) * days;

		int hours = (int) (diff / (60 * 60));
		diff = diff - hours * (60 * 60);
		int minutes = (int) (diff / (60));
		diff = diff - minutes * 60;
		int seconds = (int) diff;
		System.out.println("" + days + "天" + hours + "小时" + minutes + "分"
				+ seconds + " 秒");
		date[0]=days;
		date[1]=hours;
		date[2]=minutes;
		date[3]=seconds;
		
		if (date[0] > 0) {
			resultString = date[0] + "天";
			resultString+="前";

		} else if (date[1]>0) {
			resultString = date[1] + "小时";
			resultString+="前";


		}else if (date[2]>0) {
			resultString=date[2]+"分钟";
			resultString+="前";

		}else {
			resultString="刚刚";
		}
		System.out.println(date.toString());
		return resultString;
	}

}
