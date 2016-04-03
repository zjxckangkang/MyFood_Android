package com.lvkang.app.myfoodandroid.myfood.json;

import java.io.Serializable;

public class OrderFood implements Serializable {

	private static final long serialVersionUID = 2L;

	public static final String Order_FOOD_ID = "orderFoodId";
	public static final String Order_FOOD_NAME = "orderFoodName";
	public static final String Order_FOOD_UNIT_PRICE = "orderFoodUnitPrice";
	// public static final String Order_FOOD_REMAIN = "orderFoodRemain";
	// public static final String Order_FOOD_REMARKS = "orderFoodRemarks";
    public static final String Order_FOOD_ATTR = "orderFoodAttr";
	public static final String Order_FOOD_NUM = "orderFoodNum";
	public static final String IS_PRAISE = "isPraise";

	private int orderFoodId;
	private String orderFoodName;
	private float orderFoodUnitPrice;
	// int orderFoodRemain;
	// String orderFoodRemarks;
	private String orderFoodAttr;
	private int orderFoodNum;
	private boolean isPraise;
	
	

	// 使用fastjsony一定要注意要有无参构造函数。
	public OrderFood() {

	}

	OrderFood(int foodId, String foodName, float foodUnitPrice,String foodAttr, int orderFoodNum) {
		 this.orderFoodId = foodId;
		 this.orderFoodName = foodName;
		 this.orderFoodUnitPrice = foodUnitPrice;
		// this.orderFoodRemain = foodRemain;
		// this.orderFoodRemarks = foodRemarks;
		 this.orderFoodAttr = foodAttr;
		this.orderFoodNum = orderFoodNum;
	}

	@Override
	public String toString() {

		// return "foodId: " + orderFoodId + "foodName: " + orderFoodName
		// + "foodUnitPrice: " + orderFoodUnitPrice + "foodRemain: "
		// + orderFoodRemain + "foodRemarks: " + orderFoodRemarks + "foodAttr: "
		// + orderFoodAttr;

		return "orderFoodId: " + orderFoodId + 
				" orderFoodName: "+orderFoodName+
				" orderFoodnum: " + orderFoodNum+
				" orderFoorUnitPrice: "+orderFoodUnitPrice+
				" orderFoodAttr: "+orderFoodAttr;
	}

	public int getOrderFoodId() {
		return orderFoodId;
	}

	public void setOrderFoodId(int orderFoodId) {
		this.orderFoodId = orderFoodId;
	}

	public int getOrderFoodNum() {
		return orderFoodNum;
	}

	public void setOrderFoodNum(int orderFoodNum) {
		this.orderFoodNum = orderFoodNum;
	}

	public String getOrderFoodName() {
		return orderFoodName;
	}

	public void setOrderFoodName(String orderFoodName) {
		this.orderFoodName = orderFoodName;
	}

	public float getOrderFoodUnitPrice() {
		return orderFoodUnitPrice;
	}

	public void setOrderFoodUnitPrice(float orderFoodUnitPrice) {
		this.orderFoodUnitPrice = orderFoodUnitPrice;
	}

	public String getOrderFoodAttr() {
		return orderFoodAttr;
	}

	public void setOrderFoodAttr(String orderFoodAttr) {
		this.orderFoodAttr = orderFoodAttr;
	}

	public boolean isPraise() {
		return isPraise;
	}

	public void setPraise(boolean isPraise) {
		this.isPraise = isPraise;
	}

}
