package com.lvkang.app.myfoodandroid.myfood.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapUtils {

	/**
	 * 获得添加边框了的Bitmap
	 * 
	 * @param bm
	 *            原始图片Bitmap
	 * @param smallW
	 *            一条边框宽度
	 * @param smallH
	 *            一条边框高度
	 * @param color
	 *            边框颜色值
	 * @return Bitmap 添加边框了的Bitmap
	 */
	public static Bitmap bitmapCombine(Bitmap bm, int smallW, int smallH, int color) {
		// 防止空指针异常
		if (bm == null) {
			return null;
		}

		// 原图片的宽高
		final int bigW = bm.getWidth();
		final int bigH = bm.getHeight();

		// 重新定义大小
		int newW = bigW + smallW * 2;
		int newH = bigH + smallH * 2;

		// 绘原图
		Bitmap newBitmap = Bitmap.createBitmap(newW, newH, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		Paint p = new Paint();
		p.setColor(color);
		canvas.drawRect(new Rect(0, 0, newW, newH), p);

		// 绘边框
		canvas.drawBitmap(bm, (newW - bigW - 2 * smallW) / 2 + smallW, (newH
				- bigH - 2 * smallH)
				/ 2 + smallH, null);

		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		return newBitmap;
	}

}
