package com.lvkang.app.myfoodandroid.myfood.http;

import com.lvkang.app.myfoodandroid.myfood.main.MainActivity;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;
import com.lvkang.app.myfoodandroid.myfood.utils.VersionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpService {
	private static final String NEED_UPDATE = "needUpdate";
	private static final String DO_NOT_NEED_UPDATE = "doNotNeedUpdate";

	//
	private static final String PARAMETER_BOARD_MESSAGE = "boardMessage";
	private static final String PARAMETER_FORMAT = "format";
	private static final String PARAMETER_FOOD_ORDER_JSON = "foodOrderJSON";
	private static final String PARAMETER_ORDER_CUSTOMER_NAME = "orderCustomerName";
	private static final String PARAMETER_CLIENT_VERSION_CODE = "clientVersionCode";
	private static final String PARAMETER_OFFSET = "offset";
	private static final String PARAMETER_LAST_ORDER_ID = "lastOrderId";
	private static final String PARAMETER_LAST_MESSAGE_ID = "lastMessageId";
	private static final String PARAMETER_PRAISE_MESSAGE_ID = "messageId";
	private static final String PARAMETER_PRAISE_REPLY_ID = "replyId";
	private static final String PARAMETER_MESSAGE_REPLY_MESSAGE_ID = "messageId";
	private static final String PARAMETER_MESSAGE_REPLY = "reply";
	private static final String PARAMETER_MESSAGE_USER_NAME = "userName";
	private static final String PARAMETER_MESSAGE_PRAISE_USER_NAME = "praiseUserName";
	private static final String PARAMETER_REPLY_PRAISE_USER_NAME = "praiseUserName";
	private static final String PARAMETER_ORDER_FOOD_ID = "orderFoodId";
	private static final String PARAMETER_ORDER_ID = "orderId";
	private static final String PARAMETER_ORDER_FODD_IS_PRAISE = "isPraise";

	private static final String PARAMETER_MESSAGE_IS_PRAISE = "messageIsPraise";
	private static final String PARAMETER_REPLY_IS_PRAISE = "replyIsPraise";

	private static final String FORMAT_FOOD_MENU = "foodMenu";
	private static final String FORMAT_BOARD_MESSAGE = PARAMETER_BOARD_MESSAGE;
	private static final String FORMAT_FOODORDER = "foodOrder";
	private static final String FORMAT_ORDERFOOD_LIST = "orderFoodList";
	private static final String FORMAT_BOARD_MESSAGE_LIST = "boardMessageList";
	private static final String FORMAT_CLIENT_VERSION_CODE = PARAMETER_CLIENT_VERSION_CODE;
	private static final String FORMAT_MESSAGE_REPLY = "messageReplys";
	private static final String FORMAT_MESSAGE_SEND_REPLY = "endMessageReplys";
	private static final String FORMAT_MESSAGE_PRAISE = "messagePraise";
	private static final String FORMAT_REPLY_PRAISE = "replyPraise";
	private static final String FORMAT_ORDER_FOOD_PRAISE = "orderFoodPraise";

	//
	private static String pathString = MainActivity.IP_STRING + "ListServlet";

	// String path =
	// "http://192.168.1.199:8080/MyFood/ListServlet   foodorder=。。。。。。。";

	// POST方法调用网址的openConnetction（）方法，然后connection的输出流中写入参数UTF-8编码的字节流
	public static boolean sendFoodOrderJSON(String JSONString)
			throws IOException {
		String path = pathString;

		Map<String, String> pagramsMap = new HashMap<String, String>();
		pagramsMap.put(PARAMETER_FORMAT, FORMAT_FOODORDER);
		pagramsMap.put(PARAMETER_FOOD_ORDER_JSON, JSONString);

		// return sendPOSTRequest(path, "foodorder="+JSONString);

		return sendPOSTRequest(path, pagramsMap);
	}

	private static boolean sendPOSTRequest(String path,
			Map<String, String> pagramsMap) throws IOException {
		if (pagramsMap == null) {
			return false;
		}
		StringBuilder data = new StringBuilder();

		for (Map.Entry<String, String> entry : pagramsMap.entrySet()) {
			data.append(entry.getKey()).append("=");
			data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			data.append("&");

		}
		data.deleteCharAt(data.length() - 1);

		// Print.P(data.toString());
		byte[] entity = data.toString().getBytes();
		HttpURLConnection connection = (HttpURLConnection) new URL(path)
				.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length",
				String.valueOf(entity.length));
		OutputStream osOutputStream = connection.getOutputStream();

		osOutputStream.write(entity);

		if (connection.getResponseCode() == 200) {
			return true;
		}
		Print.P("sendPostRequest failed");
		return false;
	}

	public static Boolean sendBoardMessage(String boardMessageString)
			throws IOException {
		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_BOARD_MESSAGE);
		pagrameterMap.put(PARAMETER_BOARD_MESSAGE, boardMessageString);

		String path = getGetRequestString(pathString, pagrameterMap);

		URL url = new URL(path);
		Print.P(path);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			return true;
		}

		return false;

	}

	private static String getGetRequestString(String path,
			Map<String, String> pagramsMap) {
		if (pagramsMap == null) {
			return null;
		}
		StringBuilder data = new StringBuilder();
		data.append(path);
		data.append("?");

		for (Map.Entry<String, String> entry : pagramsMap.entrySet()) {

			data.append(entry.getKey()).append("=");

			try {
				data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			data.append("&");

		}
		data.deleteCharAt(data.length() - 1);

		return data.toString();
	}

	public static String getOrderedFoodList(String orderCustomerName,
			int offset, long lastOrderId) throws IOException {
		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_ORDERFOOD_LIST);
		pagrameterMap.put(PARAMETER_ORDER_CUSTOMER_NAME, orderCustomerName);
		pagrameterMap.put(PARAMETER_OFFSET, offset + "");
		pagrameterMap.put(PARAMETER_LAST_ORDER_ID, lastOrderId + "");
		String path = getGetRequestString(pathString, pagrameterMap);
		Print.P("getOrderedFoodList  path", path);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			return parseinStreamToString(inStream);
		}

		return null;

	}

	// String path = "http://192.168.1.199:8080/MyFood/ListServlet?format=json";

	// GET方法中调用网址+？+参数的openConnection（）方法
	// public static List<Food> getFoodList() throws IOException {
	//
	// Map<String, String> pagrameterMap = new HashMap<String, String>();
	//
	// pagrameterMap.put(PARAMETER_FORMAT, FORMAT_FOOD_MENU);
	//
	// String path = getGetRequestString(pathString, pagrameterMap);
	//
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(5000);
	// conn.setRequestMethod("GET");
	// if (conn.getResponseCode() == 200) {
	// InputStream inStream = conn.getInputStream();
	// return parseStringToFoodList(parseinStreamToString(inStream));
	// }
	//
	// return null;
	//
	// }
	public static String getFoodList() {

		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_FOOD_MENU);

		String path = getGetRequestString(pathString, pagrameterMap);
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				return parseinStreamToString(inStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		return null;

	}

	private static String parseinStreamToString(InputStream inStream)
			throws IOException {
		byte[] data = StreamTools.read(inStream);
		String string = new String(data);
		return string;
	}

	public static String getBoardMessages(int offset, Long lastMessageId,
			String UserName) {
		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_BOARD_MESSAGE_LIST);
		pagrameterMap.put(PARAMETER_OFFSET, offset + "");
		pagrameterMap.put(PARAMETER_LAST_MESSAGE_ID, lastMessageId + "");
		pagrameterMap.put(PARAMETER_MESSAGE_USER_NAME, UserName);

		String path = getGetRequestString(pathString, pagrameterMap);

		Print.P("path", path);
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				return parseinStreamToString(inStream);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;

	}

	public static String getMessageReplys(Long messageId, String userName) {
		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_MESSAGE_REPLY);
		pagrameterMap.put(PARAMETER_MESSAGE_REPLY_MESSAGE_ID, messageId + "");
		pagrameterMap.put(PARAMETER_REPLY_PRAISE_USER_NAME, userName);

		String path = getGetRequestString(pathString, pagrameterMap);

		Print.P("path", path);
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				return parseinStreamToString(inStream);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static int sendMessageReplys(String reply) {
		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_MESSAGE_SEND_REPLY);
		pagrameterMap.put(PARAMETER_MESSAGE_REPLY, reply);

		String path = getGetRequestString(pathString, pagrameterMap);

		Print.P("path", path);
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				return 0;
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;

	}

	public static Boolean setMessagePraisestate(Long messageId,
			boolean praiseState, String UserName) throws IOException {

		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_MESSAGE_PRAISE);
		pagrameterMap.put(PARAMETER_MESSAGE_PRAISE_USER_NAME, UserName);
		pagrameterMap.put(PARAMETER_PRAISE_MESSAGE_ID, messageId + "");
		pagrameterMap.put(PARAMETER_MESSAGE_IS_PRAISE, praiseState + "");
		String path = getGetRequestString(pathString, pagrameterMap);
		Print.P("path", path);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {

			return true;
		}

		return false;

	}

	public static Boolean setReplyPraisestate(Long replyId, boolean praiseState,
			String UserName) throws IOException {

		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_REPLY_PRAISE);
		pagrameterMap.put(PARAMETER_REPLY_PRAISE_USER_NAME, UserName);
		pagrameterMap.put(PARAMETER_PRAISE_REPLY_ID, replyId + "");
		pagrameterMap.put(PARAMETER_REPLY_IS_PRAISE, praiseState + "");
		String path = getGetRequestString(pathString, pagrameterMap);
		Print.P("path", path);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {

			return true;
		}

		return false;

	}

	public static Boolean needUpdate() throws IOException {

		int versionCode;
		try {
			versionCode = VersionUtils.getVersionCode(MainActivity.mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			versionCode = 0;
		}
		Map<String, String> pagrameterMap = new HashMap<String, String>();

		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_CLIENT_VERSION_CODE);
		pagrameterMap.put(PARAMETER_CLIENT_VERSION_CODE, versionCode + "");

		String path = getGetRequestString(pathString, pagrameterMap);

		Print.P("path", path);

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			String updateString = parseinStreamToString(inStream);

			if (updateString.trim().equals(NEED_UPDATE)) {
				return true;
			} else {

				return false;

			}
		}

		return false;

	}

	public static Boolean setOrderFoodPraisestate( long orderId,
			int orderFoodId, boolean isPraise) throws IOException {
		Map<String, String> pagrameterMap = new HashMap<String, String>();
		pagrameterMap.put(PARAMETER_FORMAT, FORMAT_ORDER_FOOD_PRAISE);
		pagrameterMap.put(PARAMETER_ORDER_FOOD_ID, orderFoodId + "");
		pagrameterMap.put(PARAMETER_ORDER_ID, orderId + "");
		pagrameterMap.put(PARAMETER_ORDER_FODD_IS_PRAISE, isPraise + "");
		String path = getGetRequestString(pathString, pagrameterMap);
		Print.P("path", path);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			return true;
		}
		return false;

	}
}
