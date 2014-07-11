package com.wangxx.nettyredis;

import static org.jboss.netty.buffer.ChannelBuffers.copiedBuffer;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.wangxx.nettyredis.model.Command;
import com.wangxx.nettyredis.model.Param;
import com.wangxx.nettyredis.model.RedisCommandType;


/**
 * @author wangxx
 *
 */
public class RedisProtocolClient {
	
	public static final Charset US_ASCII = Charset.forName("US-ASCII");
	
	private final String ip;
	
	private final int port;
	
	private final ClientBootstrap bootstrap;
	
	private Channel channel;
	
	public RedisProtocolClient(String ip,int port){
		
		this.ip = ip;
		this.port = port;
		
		//1.
		ChannelFactory factory =  
	               new NioClientSocketChannelFactory(  
	                     Executors.newCachedThreadPool(),  
	                       Executors.newCachedThreadPool());  
		//2.
		bootstrap = new ClientBootstrap(factory);  
		
		//3.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {  
            public ChannelPipeline getPipeline() {  
                  return Channels.pipeline(new RedisProtocolDecoder(),new RedisHandler());
            }  
          });  
		
	}
	
	public void set(String key,String value){
		
		if(channel == null || !channel.isConnected()){
			connect();
		}
		
		Param param = new Param();
		param.add(key,value);
		Command command = new Command(RedisCommandType.SET,param);
		
		channel.write(command);
		
		return ;
	}
	
	public Command get(String key){
		
		if(channel == null || !channel.isConnected()){
			connect();
		}
		
		Param param = new Param();
		param.add(key);
		Command command = new Command(RedisCommandType.GET,param);
		
		channel.write(command);
		
		return command;
	}
	
	private void connect(){
		
		 ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
		 
		 this.channel = future.awaitUninterruptibly().getChannel();
	}

}
