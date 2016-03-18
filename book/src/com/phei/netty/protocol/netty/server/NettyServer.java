/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phei.netty.protocol.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.io.IOException;

import com.phei.netty.codec.protobuf.MessageProto;
import com.phei.netty.protocol.netty.NettyConstant;
import com.phei.netty.protocol.netty.codec.NettyMessageDecoder;
import com.phei.netty.protocol.netty.codec.NettyMessageEncoder;

/**
 * @author Lilinfeng
 * @date 2014年3月15日
 * @version 1.0
 */
public class NettyServer {

	public void bind() throws Exception {
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch)
							throws IOException {
						ch.pipeline().addLast(
								new ProtobufVarint32FrameDecoder());
						ch.pipeline().addLast(
								new ProtobufDecoder(MessageProto.Message
										.getDefaultInstance()));
						ch.pipeline().addLast(
								new ProtobufVarint32LengthFieldPrepender());
						ch.pipeline().addLast(new ProtobufEncoder());
						ch.pipeline().addLast(new MyLoginRespHandler());
						// ch.pipeline().addLast(
						// new NettyMessageDecoder(1024 * 1024, 4, 4));
						// ch.pipeline().addLast(new NettyMessageEncoder());
						//
						// ch.pipeline().addLast(
						// "decoder",
						// new ObjectDecoder(ClassResolvers
						// .cacheDisabled(this.getClass()
						// .getClassLoader())));
						// ch.pipeline().addLast("encoder", new
						// ObjectEncoder());
						// ch.pipeline().addLast("readTimeoutHandler",
						// new ReadTimeoutHandler(50));
						// ch.pipeline().addLast("LoginAuthHandler",
						// new LoginAuthRespHandler());
						// ch.pipeline().addLast("HeartBeatHandler",
						// new HeartBeatRespHandler());
						// ch.pipeline().addLast(new MyReqHandler());
					}
				});

		// 绑定端口，同步等待成功
		// b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
		b.bind(NettyConstant.PORT).sync();
		System.out.println("Netty server start ok : "
				+ (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
	}

	public static void main(String[] args) throws Exception {
		new NettyServer().bind();
	}
}
