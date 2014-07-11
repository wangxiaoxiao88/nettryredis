package com.wangxx.nettyredis.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;


/**
 * @author wangxx
 *
 */
public class Command {
	
	private static final byte[] CRLF = "\r\n".getBytes();
	
	private RedisCommandType type;
	
	private CountDownLatch latch;
	
	private Param param;
	
	private ChannelBuffer buffer;
	
	private RedisReply reply;
	
	public Command(RedisCommandType type,Param param){
		this.type = type;
		this.param = param;
		this.latch = new CountDownLatch(1);
		this.buffer = ChannelBuffers.dynamicBuffer();
		
		this.encode();
	}
	
	private void encode(){
		buffer.writeByte('*');
		buffer.writeBytes(getBytes(param.getCount()+1));
		buffer.writeBytes(CRLF);
		buffer.writeByte('$');
		buffer.writeBytes(getBytes(type.value.length));
		buffer.writeBytes(CRLF);
		buffer.writeBytes(type.value);
		buffer.writeBytes(CRLF);
		if(param != null){
			buffer.writeBytes(param.getChannelBuffer());
		}
		
	}
	
	private byte[] getBytes(int value){
		return String.valueOf(value).getBytes();
	}
	
	public ChannelBuffer buffer(){
		return buffer;
	}
	
	public boolean await(long timeout,TimeUnit unit) throws InterruptedException{
		try{
			return latch.await(timeout, unit);
		} catch (InterruptedException e) {
			throw e;
		}
	}
	
	public void complete(){
		latch.countDown();
	}

	public RedisReply getReply() {
		return reply;
	}

	public void setReply(RedisReply reply) {
		this.reply = reply;
		this.complete();
	}

}
