package com.phei.netty.protocol.netty.server;

import com.phei.netty.codec.protobuf.MessageProto;
import com.phei.netty.protocol.netty.MsgType;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MyLoginRespHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		MessageProto.Message message = (MessageProto.Message) msg;
		String nodeIndex = ctx.channel().remoteAddress().toString();
		if (message.getType() == MsgType.LOGIN_REQ) {
			System.out.println("login req" + message.getDesc() + "addres:"
					+ nodeIndex);
			ctx.writeAndFlush(sendLoginMsg());
		} else {
			ctx.fireChannelRead(msg);
		}

	}

	private MessageProto.Message sendLoginMsg() {
		MessageProto.Message.Builder builder = MessageProto.Message
				.newBuilder();
		builder.setType(MsgType.LOGIN_RESP);
		builder.setDesc("ok no problem");
		return builder.build();
	}

}
