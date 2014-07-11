package com.wangxx.nettyredis.model;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author wangxx
 *
 */
public class Param {
	
	private static final byte[] CRLF = "\r\n".getBytes();
	
	private ChannelBuffer buffer;
	
	private int count;
	
	public Param(){
		this.buffer = ChannelBuffers.dynamicBuffer();
	}
	
	public void add(String value){
		writeToBuffer(value.getBytes());
	}
	
	public void add(String...values){
		for(String value:values){
			writeToBuffer(value.getBytes());
		}
	}
	
	private void writeToBuffer(byte[] value){
		buffer.writeByte('$');
		int length = value.length;
		buffer.writeBytes(String.valueOf(length).getBytes());
		buffer.writeBytes(CRLF);
		buffer.writeBytes(value);
		buffer.writeBytes(CRLF);
		
		count++;
	}
	
	public int getCount(){
		return count;
	}
	
	public ChannelBuffer getChannelBuffer(){
		return buffer;
	}

}
