package com.wangxx.nettyredis;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.wangxx.nettyredis.exception.RedisException;
import com.wangxx.nettyredis.model.RedisReply;

/**
 * @author wangxx
 * 
 *  redis protocol ,see {#link:http://redis.io/topics/protocol}
 *
 */
public class RedisProtocolDecoder extends FrameDecoder{

	private static final byte CR = (byte)'\r';
    private static final byte LF = (byte)'\n';
    private static final byte NEGATIVE = (byte)'-';
    private static final byte ZERO = (byte)'0';
    
	@Override
	protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel,
			ChannelBuffer buffer) throws Exception {
		
		byte first = buffer.readByte();
		switch(first){
		case RedisReply.STATUS_REPLY_CODE:
			return new RedisReply((char)first,readline(buffer));
		case RedisReply.ERROR_REPLY_CODE:
			return new RedisReply((char)first,readline(buffer));
		case RedisReply.INTEGER_REPLY_CODE:
			return new RedisReply((char)first,readInteger(buffer));
		case RedisReply.BULK_REPLY_CODE:
			return new RedisReply((char)first, readBytes(buffer));
		}

		return null;
	}
	
	private static String readline(ChannelBuffer channelBuffer){
		StringBuilder sb = new StringBuilder();
		
		while(true){
			byte next = channelBuffer.readByte();
			if(next == CR){
				next = channelBuffer.readByte();
				if(next == LF){
					return sb.toString();
				} else {
					sb.append((char)CR);
					sb.append((char)next);
				}
			} else {
				sb.append((char)next);
			}
		}
	}
	
	private static int readInteger(ChannelBuffer channel){
		int sum = 0;
		int sign = 1;
		byte next = channel.readByte();
		if(next == NEGATIVE){
			sign = -1;
			next = channel.readByte();
		}
		
		while(true){
				if(next == CR){
					next = channel.readByte();
					if(next == LF){
						break;
					}
				}
			
				int num = next - ZERO;
				if(num >= 0 && num <= 9){
					sum *= 10;
					sum += num;
				}
				next = channel.readByte();
		}
		
		return sum * sign;
	}
	
	private static byte[] readBytes(ChannelBuffer channel) throws RedisException{
		int size = readInteger(channel);
		if(size <= 0){
			return null;
		}
		byte[] datas = new byte[size];
		channel.readBytes(datas,0,size);
		byte cr = channel.readByte();
		byte lf = channel.readByte();
		if(cr != CR || lf != LF){
			throw new RedisException("buffer end error");
		}
		return datas;
	}

}
