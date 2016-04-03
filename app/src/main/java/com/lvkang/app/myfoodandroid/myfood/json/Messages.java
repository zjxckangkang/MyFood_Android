package com.lvkang.app.myfoodandroid.myfood.json;

import com.alibaba.fastjson.JSON;
import com.lvkang.app.myfoodandroid.myfood.utils.Print;

import java.util.ArrayList;
import java.util.List;

public class Messages {
	private List<Message> messageList = new ArrayList<Message>();
	private int firstMessageId;
	public static void main(String[] args) {
		List<Messages> mllistList=new ArrayList<Messages>();
		Message message=new Message();
		message.setMessageBody("haha");
		message.setMessageDatetime("xixi");
		message.setMessageId(123L);
		message.setMessageUserName("hehe");
		Messages ml=new Messages();
		
		for (int i = 0; i < 10; i++) {
			ml.getMessageList().add(message);
			
		}
		for (int i = 0; i < 10; i++) {
			mllistList.add(ml);
			
		}
		
		Print.P(JSON.toJSONString(ml));
		
		
		Messages mListfromList=JSON.parseObject(JSON.toJSONString(mllistList), Messages.class);
		
		Print.P(mListfromList.getMessageList().size()+"");
		
		
		
		
		
	}

	public int getFirstMessageId() {
		return firstMessageId;
	}

	public void setFirstMessageId(int firstMessageId) {
		this.firstMessageId = firstMessageId;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

}
