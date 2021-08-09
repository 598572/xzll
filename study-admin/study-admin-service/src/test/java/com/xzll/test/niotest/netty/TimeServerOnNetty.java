package com.xzll.test.niotest.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 15:58
 * @Description: 基于 netty 构建的server服务器
 */
public class TimeServerOnNetty {

	private static final int port = 8080;

	public void run() throws Exception {
		//bossGroup 用于接受客户端连接，bossGroup在接受到客户端连接之后，将连接交给workerGroup来进行处理。
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)这是netty中表示服务端的类，用于接受客户端连接，对应于java.nio包中的ServerSocketChannel。
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							//拆包粘包相关的
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
							ch.pipeline().addLast(new StringDecoder());
							//添加服务端处理器
							ch.pipeline().addLast(new TimeServerHandler());
						}
					});
			//绑定端口
			ChannelFuture f = b.bind(port).sync(); // (5)
			System.out.println("TimeServer Started on 8080...");
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new TimeServerOnNetty().run();
	}

	public class TimeServerHandler extends ChannelInboundHandlerAdapter {
		/**
		 * 处理客户端请求
		 *
		 * @param ctx
		 * @param msg
		 * @throws Exception
		 */
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { // 1
			String request = (String) msg; //2
			System.out.println("客户端请求: "+request);
			String response = null;
			if ("QUERY TIME ORDER".equals(request)) { // 3
				response = new Date(System.currentTimeMillis()).toString();
			} else {
				response = "BAD REQUEST";
			}
			response = response + System.getProperty("line.separator"); // 4
			ByteBuf resp = Unpooled.copiedBuffer(response.getBytes()); // 5
			ctx.writeAndFlush(resp); // 6
		}
	}
}
