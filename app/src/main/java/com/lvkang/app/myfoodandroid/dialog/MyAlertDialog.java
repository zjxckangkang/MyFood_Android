package com.lvkang.app.myfoodandroid.dialog;


import android.app.AlertDialog;
import android.content.Context;

public class MyAlertDialog {
	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	public MyAlertDialog(Context context, String message, String title,
			boolean cancelable) {

//		builder = new AlertDialog.Builder(context, R.style.MyAlertDialogCustom);
		builder = new AlertDialog.Builder(context);

		builder.setTitle(title).setMessage(message).setCancelable(cancelable);
		// AlertDialog.Builder builder = new AlertDialog.Builder(new
		// ContextThemeWrapper(this, R.style.AlertDialogCustom));

	}

	public void creatAlertDialog() {
		alertDialog = builder.create();
	}

	public void showAlertDialog() {
		if (alertDialog != null && !alertDialog.isShowing()) {

			alertDialog.show();
		}
	}

	public void dismissAlertDialog() {
		if (alertDialog != null && alertDialog.isShowing()) {

			alertDialog.dismiss();
		}
	}

	public void deleteAlertDialog() {
		dismissAlertDialog();
		alertDialog = null;
	}

	public AlertDialog.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(AlertDialog.Builder builder) {
		this.builder = builder;
	}

	public AlertDialog getAlertDialog() {
		return alertDialog;
	}

	public void setAlertDialog(AlertDialog alertDialog) {
		this.alertDialog = alertDialog;
	}

}
