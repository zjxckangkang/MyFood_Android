package com.lvkang.app.myfoodandroid.myfood.json;

import java.io.Serializable;

public class Food implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FOOD_ID = "foodId";
	public static final String FOOD_NAME = "foodName";
	public static final String FOOD_UNIT_PRICE = "foodUnitPrice";
	public static final String FOOD_REMAIN = "foodRemain";
	public static final String FOOD_REMARKS = "foodRemarks";
	public static final String FOOD_ATTR = "foodAttr";
	public static final String FOOD_JPG = "foodJpg";
	public static final String FOOD_SALES = "foodSales";
	public static final String FOOD_INTRO = "foodIntro";
	public static final String FOOD_NUMBER = "foodNum";
	public static final String FOOD_PRAISE = "foodPraise";

	private int foodSales;
	private int foodId;
	private String foodName;
	private float foodUnitPrice;
	private int foodRemain;
	private String foodRemarks;
	private String foodAttr;
	private String foodJpg;
	private String foodIntro;
	private Long foodPraise;

	public Food() {

	}

	Food(int foodId, String foodName, float foodUnitPrice, int foodRemain,
			String foodRemarks, String foodAttr) {
		this.foodId = foodId;
		this.foodName = foodName;
		this.foodUnitPrice = foodUnitPrice;
		this.foodRemain = foodRemain;
		this.foodRemarks = foodRemarks;
		this.foodAttr = foodAttr;
	}

	public int getFoodSales() {
		return foodSales;
	}

	public void setFoodSales(int foodSales) {
		this.foodSales = foodSales;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public float getFoodUnitPrice() {
		return foodUnitPrice;
	}

	public void setFoodUnitPrice(float foodUnitPrice) {
		this.foodUnitPrice = foodUnitPrice;
	}

	public int getFoodRemain() {
		return foodRemain;
	}

	public void setFoodRemain(int foodRemain) {
		this.foodRemain = foodRemain;
	}

	public String getFoodRemarks() {
		return foodRemarks;
	}

	public void setFoodRemarks(String foodRemarks) {
		this.foodRemarks = foodRemarks;
	}

	public String getFoodAttr() {
		return foodAttr;
	}

	public void setFoodAttr(String foodAttr) {
		this.foodAttr = foodAttr;
	}

	public String getFoodJpg() {
		return foodJpg;
	}

	public void setFoodJpg(String foodJpg) {
		this.foodJpg = foodJpg;
	}

	public String getFoodIntro() {
		return foodIntro;
	}

	public void setFoodIntro(String foodIntro) {
		this.foodIntro = foodIntro;
	}

	public Long getFoodPraise() {
		return foodPraise;
	}

	public void setFoodPraise(Long foodPraise) {
		this.foodPraise = foodPraise;
	}

}
