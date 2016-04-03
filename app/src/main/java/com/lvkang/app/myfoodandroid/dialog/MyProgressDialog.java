package com.lvkang.app.myfoodandroid.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {
	ProgressDialog progressDialog;
	
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public MyProgressDialog(Context context,String message,String title,boolean cancelTouchOutside,boolean cancelble) {

		progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage(message);
		progressDialog.setTitle(title);
		progressDialog.setCanceledOnTouchOutside(cancelTouchOutside);
		progressDialog.setCancelable(cancelble);

	}
	public void deleteProgressDialog(){
		dismissProgressDialog();
		progressDialog = null;
	}
	public void dismissProgressDialog(){
		if (progressDialog != null
				&& progressDialog.isShowing()) {

			progressDialog.dismiss();
		}
	}

	public void showProgressDialog(){
		if (progressDialog != null
				&&! progressDialog.isShowing()) {

			progressDialog.show();
		}
	}
	
	

}
