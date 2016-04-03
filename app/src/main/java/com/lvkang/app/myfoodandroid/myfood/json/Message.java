package com.lvkang.app.myfoodandroid.myfood.json;



public class Message {
	

	public static final String MEESAGE_ID="messageId";
	public static final String MEESAGE_BODY="messageBody";
	public static final String MEESAGE_DATETIME="messageDatetime";
	public static final String MEESAGE_USERNAME="messageUserName";
	public static final String MEESAGE_REPLY="messageReply";
	public static final String MEESAGE_PRAISE="messagePraise";
	public static final String MEESAGE_IS_PRAISE="isPraise";
	public static final String MEESAGE_PRAISE_USER_NAME="praiseUserName";
	
	
	
	
	
	
	private Long messageId;
	private String messageBody;
	private String messageDatetime;
	private String messageUserName;
	private Long messageReply;
	private int messagePraise;
	private boolean messageIsPraise;
	
	public static void main(String[] args) {
		Message message=new Message();
		message.setMessageBody("haha");
		message.setMessageDatetime("xixi");
		message.setMessageId(123L);
		message.setMessageUserName("hehe");
		
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getMessageDatetime() {
		return messageDatetime;
	}

	public void setMessageDatetime(String messageDatetime) {
		this.messageDatetime = messageDatetime;
	}

	public String getMessageUserName() {
		return messageUserName;
	}

	public void setMessageUserName(String messageUserName) {
		this.messageUserName = messageUserName;
	}

	public Long getMessageReply() {
		return messageReply;
	}

	public void setMessageReply(Long messageReply) {
		this.messageReply = messageReply;
	}

	public int getMessagePraise() {
		return messagePraise;
	}

	public void setMessagePraise(int messagePraise) {
		this.messagePraise = messagePraise;
	}

	public boolean isMessageIsPraise() {
		return messageIsPraise;
	}

	public void setMessageIsPraise(boolean messageIsPraise) {
		this.messageIsPraise = messageIsPraise;
	}

	


	
}
