package com.lvkang.app.myfoodandroid.myfood.utils;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	public  static void Show(Context context,String messageString){
		
		Toast.makeText(context, messageString,
				Toast.LENGTH_SHORT).show();
	}
	


}
