package com.wangxx.nettyredis;

/**
 * @author wangxx
 *
 */
public class Test {

	public static void main(String[] args) {
		
		String ip = "127.0.0.1";
		int port = 6379;
		
		RedisClient client = new RedisClient(ip,port);
		
		String key = "wangxx";
		String value = "love";
		
		//client.set(key,value);
		
		String ret = client.get(key);
		System.out.println(ret);
		
		
		

	}

}
