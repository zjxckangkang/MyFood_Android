package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lvkang.app.myfoodandroid.adapter.AdapterOrderFood;
import com.lvkang.app.myfoodandroid.dialog.MyAlertDialog;
import com.lvkang.app.myfoodandroid.dialog.MyProgressDialog;
import com.lvkang.app.myfoodandroid.myfood.http.HttpService;
import com.lvkang.app.myfoodandroid.myfood.json.Food;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFood;
import com.lvkang.app.myfoodandroid.myfood.json.OrderFoods;
import com.lvkang.app.myfoodandroid.myfood.utils.DeviceId;
import com.lvkang.app.myfoodandroid.myfood.utils.MyToast;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.myfoodtoandroid.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ShopActivity extends Activity {
	static Button buttonShopBasket, buttonShop;
	ImageButton buttonBack;

	static TextView textViewShopPrice;

	static TextView textViewShopnum;
	private TextView titlebar_activity_introduction;

	EditText editTextShopTableId, editTextShopremarks;
	ListView listViewOrderFood;

	public static boolean ensurePostTag = false;
	static MyHandler shopHandler = new MyHandler();

	static ArrayList<HashMap<String, Object>> orderFoodMenuArrayList;

	static ShopActivity shopActivity;
	PostJsonThread postJsonThread;

	AdapterOrderFood adapterOrderFood;

	MyAlertDialog failureAlertDialog;
	MyAlertDialog completeAlertDialog;
	MyProgressDialog myprogressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		shopActivity = this;

		Print.P("onCreat", this.getClass().getName());
		setContentView(R.layout.shop_activity);
		buttonBack = (ImageButton) findViewById(R.id.titlebar_back_button);
		buttonShopBasket = (Button) findViewById(R.id.shopbasket);
		buttonShop = (Button) findViewById(R.id.shopstart);
		listViewOrderFood = (ListView) findViewById(R.id.listview_shop);
		textViewShopnum = (TextView) findViewById(R.id.shopnum);
		textViewShopPrice = (TextView) findViewById(R.id.shopprice);
		titlebar_activity_introduction = (TextView) findViewById(R.id.titlebar_activity_introduction);
		titlebar_activity_introduction.setText(getResources().getString(
				R.string.shop_activity_introduction));
		editTextShopTableId = (EditText) findViewById(R.id.edittext_shop_table_id);
		editTextShopremarks = (EditText) findViewById(R.id.edittext_shop_remarks);

		buttonShopBasket.setText("返回");
		buttonShop.setText("确定购买");
		buttonBack.setOnClickListener(new shopOnClickListener());

		buttonShopBasket.setOnClickListener(new shopOnClickListener());
		buttonShop.setOnClickListener(new shopOnClickListener());

		orderFoodMenuArrayList = MainFragment.findOrderFood();
		adapterOrderFood = setListViewAdapter(this);
		listViewOrderFood.setAdapter(adapterOrderFood);

		refreshOrderFoodTextView();

		initCompleteAlert();
		initFailureAlert();
		initProgressDialog();

	}

	public static void refreshOrderFoodTextView() {
		int numAdd = 0;
		float priceAdd = 0;

		int num = 0;
		for (int i = 0; i < orderFoodMenuArrayList.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map = orderFoodMenuArrayList.get(i);
			num = Integer.parseInt(map.get(Food.FOOD_NUMBER).toString());

			numAdd += num;

			priceAdd += num
					* Float.parseFloat(map.get(Food.FOOD_UNIT_PRICE).toString());

		}
		textViewShopnum.setText(numAdd + "");
		textViewShopPrice.setText(priceAdd + "");
		if (numAdd > 0) {
			setButtonDrawable(true);

		} else {

			setButtonDrawable(false);
		}
	}

	private static void setButtonDrawable(boolean hasOrdered) {
		if (hasOrdered) {
			buttonShop.setBackgroundResource(R.drawable.button);
		} else {
			buttonShop.setBackgroundResource(R.drawable.button_sub);

		}
	}

	private static AdapterOrderFood setListViewAdapter(Context context) {

		AdapterOrderFood adapterOrderFood = new AdapterOrderFood(context,
				orderFoodMenuArrayList, R.layout.order_adapter,
				new String[] { Food.FOOD_NUMBER, Food.FOOD_NAME,
						Food.FOOD_UNIT_PRICE }, new int[] {
						R.id.order_food_num, R.id.order_food_name,
						R.id.order_food_unitprice, R.id.order_food_price,
						R.id.num_add, R.id.num_cut, R.id.num_delete, });

		return adapterOrderFood;

	}

	private class shopOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.shopbasket:
			case R.id.titlebar_back_button:

				Intent intent = new Intent(ShopActivity.this,
						MainActivity.class);
				startActivity(intent);

				break;
			case R.id.shopstart:

				if (orderFoodMenuArrayList.size() > 0) {

					startShop();
				}

				break;

			default:
				break;
			}
		}

	}

	public class PostJsonThread extends Thread {
		String orderFoodJSONString = "";

		public PostJsonThread(String JSONString) {
			this.orderFoodJSONString = JSONString;
		}

		public void run() {

			try {
				ensurePostTag = HttpService
						.sendFoodOrderJSON(orderFoodJSONString);

			} catch (IOException e) {

				e.printStackTrace();
				ensurePostTag = false;
				Print.P("PostJSONThread IOE Exception");
			}
			shopHandler.post(new Runnable() {

				@Override
				public void run() {
					if (ensurePostTag == true) {
						myprogressDialog.dismissProgressDialog();
						completeAlertDialog.showAlertDialog();

					} else {
						myprogressDialog.dismissProgressDialog();
						failureAlertDialog.showAlertDialog();
					}

				}
			});

		}
	}

	public static String getOrderFoodObjectStringFromOrderFoodList(
			ArrayList<HashMap<String, Object>> orderFoodArrayListMap,
			int tableId, String remarks) {

		OrderFoods orderfoodJSONObject = new OrderFoods();

		for (int i = 0; i < orderFoodArrayListMap.size(); i++) {

			OrderFood orderFood = new OrderFood();

			HashMap<String, Object> a = orderFoodArrayListMap.get(i);

			if (a.get(Food.FOOD_NUMBER) != null
					&& Integer.parseInt(a.get(Food.FOOD_NUMBER).toString()) > 0) {

				orderFood.setOrderFoodId(Integer.parseInt(a.get(Food.FOOD_ID)
						.toString()));
				orderFood.setOrderFoodNum(Integer.parseInt(a.get(
						Food.FOOD_NUMBER).toString()));
				orderFood.setOrderFoodName(a.get(Food.FOOD_NAME).toString());

				orderfoodJSONObject.getFoodList().add(orderFood);
			}

		}

		orderfoodJSONObject.setOrderCustomerName(DeviceId
				.getdeviceIdString(shopActivity));
		orderfoodJSONObject.setOrderTableId(tableId);

		orderfoodJSONObject.setOrderRemarks(remarks);

		String orderFoodJSONString = JSON.toJSONString(orderfoodJSONObject,
				SerializerFeature.UseSingleQuotes);

		return orderFoodJSONString;
	}

	private void startShop() {

		String tableIdString = editTextShopTableId.getText().toString();
		if (tableIdString == null || tableIdString.length() == 0) {
			MyToast.Show(shopActivity, "请输入餐桌号");

		} else {
			int tableId = Integer.parseInt(tableIdString);
			if (tableId >= 0) {

				String remarks = editTextShopremarks.getText().toString();

				myprogressDialog.showProgressDialog();
				postJsonThread = new PostJsonThread(
						getOrderFoodObjectStringFromOrderFoodList(
								orderFoodMenuArrayList, tableId, remarks));

				postJsonThread.start();

			} else {
				MyToast.Show(shopActivity, "请输入正确的餐桌号");

			}
		}

	}

	private void initCompleteAlert() {
		completeAlertDialog = new MyAlertDialog(shopActivity, "请在我的订单中查看。",
				"下单成功！", false);

		completeAlertDialog.getBuilder().setPositiveButton("完成",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						for (int i = 0; i < orderFoodMenuArrayList.size(); i++) {
							orderFoodMenuArrayList.get(i).put(Food.FOOD_NUMBER,
									0);
						}

						completeAlertDialog.dismissAlertDialog();
						refreshOrderFoodTextView();
						adapterOrderFood.notifyDataSetChanged();

						Intent intent = new Intent(ShopActivity.this,
								MainActivity.class);
						startActivity(intent);

						new Thread(MainFragment.getFoodsJsonThread).start();

						new Thread(OrderedFragment.getOrderedFoodListrunnable)
								.start();

					}
				});
		completeAlertDialog.creatAlertDialog();
	}

	private void initFailureAlert() {
		failureAlertDialog = new MyAlertDialog(ShopActivity.this, "请检查网络连接！",
				"下单失败！", false);
		failureAlertDialog.getBuilder()
				.setPositiveButton("重试", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startShop();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						myprogressDialog.dismissProgressDialog();
					}
				});
		failureAlertDialog.creatAlertDialog();

	}

	private void initProgressDialog() {
		myprogressDialog = new MyProgressDialog(ShopActivity.this, "请稍等...",
				"下单中", false, false);
	}

	static class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	}

	@Override
	protected void onResume() {
		MyApplication.selectedActivity = this.getClass();

		super.onResume();

		Print.P("onResume", this.getClass().getName());
	}

	@Override
	protected void onPause() {
		super.onPause();

		Print.P("onPause", this.getClass().getName());
	}

	@Override
	protected void onDestroy() {

		// 必须另对话框为null 否则会报android.view.WindowManager$BadTokenException: Unable
		// to add window -- token android.os.BinderProxy@42bade90 is not valid;
		// is your activity running?
		failureAlertDialog.deleteAlertDialog();
		completeAlertDialog.deleteAlertDialog();
		myprogressDialog.deleteProgressDialog();

		Print.P("noDestory", this.getClass().getName());
		super.onDestroy();
	}

}
