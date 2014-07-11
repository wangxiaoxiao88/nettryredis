package com.wangxx.nettyredis.model;

/**
 * @author wangxx
 *
 */
public class RedisReply {
	
	public static final char STATUS_REPLY_CODE = '+';
	
	public static final char ERROR_REPLY_CODE = '-';
	
	public static final char INTEGER_REPLY_CODE = ':';
	
	public static final char BULK_REPLY_CODE = '$';
	
	public static final char MULTI_BULK_REPLY_CODE = '*';
	
	private final char code;
	
	private final String value;

	public RedisReply(char code, String value){
		this.code = code;
		this.value = value;
	}
	
	public RedisReply(char code, int value){
		this.code = code;
		this.value = String.valueOf(value);
	}
	
	public RedisReply(char code, byte[] value){
		this.code = code;
		this.value = new String(value);
	}

	public char getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

}
