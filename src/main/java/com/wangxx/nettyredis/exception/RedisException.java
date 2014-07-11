package com.wangxx.nettyredis.exception;

/**
 * @author wangxx
 *
 */
public class RedisException extends Exception{
	
	public RedisException(String message){
		super(message);
	}
	
	public RedisException(String message,Throwable t){
		super(message,t);
	}

}
