package com.wangxx.nettyredis;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.wangxx.nettyredis.model.Command;
import com.wangxx.nettyredis.model.RedisReply;

/**
 * @author wangxx
 *
 */
public class RedisHandler extends SimpleChannelHandler{
	
	private BlockingQueue<Command> queue = new LinkedBlockingQueue<Command>();
	
	 @Override
	 public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws java.lang.Exception {
		 
		 Command command = (Command)e.getMessage();
		 Channels.write(ctx, e.getFuture(), command.buffer());
		 queue.put(command);
		 
	 }
	 
	 @Override
	 public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws java.lang.Exception {
		 
		 RedisReply reply = (RedisReply)e.getMessage();
		 Command command = queue.take();
		 command.setReply(reply);
	 }

}
