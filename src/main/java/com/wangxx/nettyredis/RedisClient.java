package com.wangxx.nettyredis;

import java.util.concurrent.TimeUnit;

import com.wangxx.nettyredis.exception.RedisException;
import com.wangxx.nettyredis.model.Command;
import com.wangxx.nettyredis.model.RedisReply;

/**
 * @author wangxx
 *
 * main class
 */
public class RedisClient {
	
	private final String ip;
	
	private final int port;
	
	private final int timeout;
	
	private final TimeUnit timeUnit;
	
	private final RedisProtocolClient client;
	
	public RedisClient(String ip,int port){
		this.ip = ip;
		this.port = port;
		this.client = new RedisProtocolClient(ip,port);
		
		this.timeout = 10;
		this.timeUnit = TimeUnit.SECONDS;
	}
	
	public void set(String key,String value) throws RedisException{
		client.set(key, value);
	}
	
	public String get(String key){
		
		Command command = client.get(key);
		
		try {
			boolean ret = command.await(timeout, timeUnit);
			if(ret){
				RedisReply reply = command.getReply();
				return reply.getValue();
			}
			
		} catch (InterruptedException e) {
		}
		
		return "";
	}
}
