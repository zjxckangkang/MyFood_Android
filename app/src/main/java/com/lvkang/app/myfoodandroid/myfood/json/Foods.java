package com.lvkang.app.myfoodandroid.myfood.json;

import java.util.ArrayList;
import java.util.List;

public class Foods {
	private int id = 0;

	public Foods() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Food> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<Food> foodList) {
		this.foodList = foodList;
	}

	private List<Food> foodList = new ArrayList<Food>();

}
