package com.lvkang.app.myfoodandroid.myfood.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	/**
	 * 读取流中的数据
	 * @param inStream
	 * @return outStream.toByteArray()
	 * @throws IOException
	 */
	public static byte[] read(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		while((len=inStream.read(buffer))!=-1){
			outStream.write(buffer,0,len);
		}
		inStream.close();
		return outStream.toByteArray();
		
	}

}
