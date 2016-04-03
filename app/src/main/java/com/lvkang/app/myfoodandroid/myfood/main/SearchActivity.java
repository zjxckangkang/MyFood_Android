package com.lvkang.app.myfoodandroid.myfood.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lvkang.app.myfoodandroid.adapter.AdapterFoodNormall;
import com.lvkang.app.myfoodandroid.myfood.json.Food;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.myfoodtoandroid.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends Activity {
	ImageButton buttonBack;
	ListView searchListView;
	AdapterFoodNormall searchAdapterFood;
	EditText searchEditText;
	private View searchInternetFailedInclude;
	private TextView loadfailTextview;
	static ArrayList<HashMap<String, Object>> searchFoodMenuArrayList = new ArrayList<HashMap<String, Object>>();

	private class MyButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_button:

				startActivity(new Intent(SearchActivity.this,
						MainActivity.class));
				break;

			default:
				break;
			}
		}

	}

	 private static boolean containKey(String target, String keys) {
	 if (keys.length() == 0) {
	 return true;
	
	 }
	
	 String keysNoSpace = keys.replace(" ", "");
	 if (target.contains(keysNoSpace)) {
	 return true;
	 }
	 return false;
	 }

//	private static boolean containKey(String target, String keys) {
//		if (keys.length() == 0) {
//			return true;
//
//		}
//
//		String keysNoSpace = keys.replace(" ", "");
//		String[] keyArrayStrings = new String[keysNoSpace.length()];
//		for (int i = 0; i < keysNoSpace.length(); i++) {
//			keyArrayStrings[i] = keysNoSpace.substring(i, i + 1);
//
//			if (target.contains(keyArrayStrings[i])) {
//				return true;
//			}
//
//		}
//
//		return false;
//
//	}

	private void searchFoodMenuArrayList(
			ArrayList<HashMap<String, Object>> foodMenuArrayList, String key) {
		searchFoodMenuArrayList.clear();
		for (int i = 0; i < foodMenuArrayList.size(); i++) {

			if (containKey(foodMenuArrayList.get(i).get(Food.FOOD_NAME)
					.toString(), key)) {

				HashMap<String, Object> map = new HashMap<String, Object>();
				map = foodMenuArrayList.get(i);

				searchFoodMenuArrayList.add(map);

			}

		}

	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

			searchFoodMenuArrayList(MainFragment.foodMenuArrayList,
					searchEditText.getText().toString());
			searchAdapterFood.notifyDataSetChanged();

			Print.P(searchFoodMenuArrayList.size() + "");

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			Print.P("beforeTextChanged");

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			Print.P("onTextChanged");

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		buttonBack = (ImageButton) findViewById(R.id.back_button);
		buttonBack.setOnClickListener(new MyButtonOnClickListener());
		searchListView = (ListView) findViewById(R.id.search_listview);
		searchAdapterFood = MainFragment.setListViewAdapter(
				searchFoodMenuArrayList, SearchActivity.this);
		searchListView.setAdapter(searchAdapterFood);
		searchInternetFailedInclude = findViewById(R.id.search_internet_failed_include);
		loadfailTextview = (TextView) findViewById(R.id.loadfail_textview3);
		searchEditText = (EditText) findViewById(R.id.title_search_edittext);
		searchEditText.addTextChangedListener(textWatcher);
		searchFoodMenuArrayList(MainFragment.foodMenuArrayList, searchEditText
				.getText().toString());
		searchAdapterFood.notifyDataSetChanged();
		loadfailTextview.setText(getResources().getString(
				R.string.back_to_main_pull_to_retry));
		if (searchFoodMenuArrayList.size() > 0) {
			searchInternetFailedInclude.setVisibility(View.GONE);

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		MyApplication.selectedActivity = this.getClass();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
