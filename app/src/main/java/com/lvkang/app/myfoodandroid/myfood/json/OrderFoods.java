package com.lvkang.app.myfoodandroid.myfood.json;

import java.util.ArrayList;
import java.util.List;

public class OrderFoods {
	public final static String ORDER_FOOD_ORDER_ID = "orderId";
	public final static String ORDER_FOOD_ORDER_TABLE_ID = "orderTableId";
	public final static String ORDER_FOOD_ORDER_DATE_TIME = "orderDateTime";
	public final static String ORDER_FOOD_ORDER_CUSTOMER_NAME = "orderCustomerName";

	public final static String ORDER_FOOD_ORDER_REMARKS = "orderRemarks";

	public final static String ORDER_FOOD_ORDER_NUM = "orderNum";
	public final static String ORDER_FOOD_ORDER_PRICE = "orderPrice";

	private long orderId;
	private int orderTableId = -1;
	private String orderDateTime = "";
	private String orderCustomerName = "unkown";
	private String orderRemarks = null;
	private List<OrderFood> foodList = new ArrayList<OrderFood>();

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(int orderTableId) {
		this.orderTableId = orderTableId;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getOrderCustomerName() {
		return orderCustomerName;
	}

	public void setOrderCustomerName(String orderCustomerName) {
		this.orderCustomerName = orderCustomerName;
	}

	public String getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	public List<OrderFood> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<OrderFood> foodList) {
		this.foodList = foodList;
	}

}
