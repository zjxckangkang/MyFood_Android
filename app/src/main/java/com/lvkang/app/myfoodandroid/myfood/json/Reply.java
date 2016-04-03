package com.lvkang.app.myfoodandroid.myfood.json;

public class Reply {
	public static final String BODY = "replyBody";
	public static final String DATETIME = "replyTime";
	public static final String MESSAGE_ID = "messageId";
	public static final String ID = "replyId";
	public static final String PRAISE = "replyPraise";
	public static final String USER_NEME = "replyUserName";
	public static final String PRAISE_USER_NEME = "praiseUserName";
	public static final String PRAISE_ID = "praiseId";
	public static final String IS_PRAISE = "isPraise";

	private String replyBody;
	private String replyTime;
	private Long messageId;
	private Long replyId;
	private int replyPraise;
	private String replyUserName;
	private boolean isPraise;
	public String getReplyBody() {
		return replyBody;
	}
	public void setReplyBody(String replyBody) {
		this.replyBody = replyBody;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	public int getReplyPraise() {
		return replyPraise;
	}
	public void setReplyPraise(int replyPraise) {
		this.replyPraise = replyPraise;
	}
	public String getReplyUserName() {
		return replyUserName;
	}
	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}
	public boolean isPraise() {
		return isPraise;
	}
	public void setPraise(boolean isPraise) {
		this.isPraise = isPraise;
	}

	

	
}
