package com.phei.netty.protocol.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

public class MyReqHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;

		String recieved = getMessage(buf);
		System.out.println("服务器接收到消息：" + recieved);
		try {
			ctx.writeAndFlush(getSendByteBuf("你好，客户端"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String getMessage(ByteBuf buf) {
		byte[] con = new byte[buf.readableBytes()];
		buf.readBytes(con);
		try {
			return new String(con, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ByteBuf getSendByteBuf(String message)
			throws UnsupportedEncodingException {

		byte[] req = message.getBytes("UTF-8");
		ByteBuf pingMessage = Unpooled.buffer();
		pingMessage.writeBytes(req);

		return pingMessage;
	}
}
