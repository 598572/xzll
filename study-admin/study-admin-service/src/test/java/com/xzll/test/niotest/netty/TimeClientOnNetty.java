package com.xzll.test.niotest.netty;

import io.netty.bootstrap.Bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 08:25
 * @Description: netty 客户端
 */
public class TimeClientOnNetty {
	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 8080;
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//表示netty客户端
			Bootstrap b = new Bootstrap(); // (1)
			b.group(workerGroup); // (2)
			b.channel(NioSocketChannel.class); // (3)
			b.handler(new ChannelInitializer<SocketChannel>() {// (4)
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new TimeClientHandler());
				}
			});
			// Start the client.
			ChannelFuture f = b.connect(host, port).sync(); // (5)
			//等到连接关闭。
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	public static class TimeClientHandler extends ChannelInboundHandlerAdapter {
		private byte[] req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();

		/**
		 * 挥手
		 *
		 * @param ctx
		 */
		@Override
		public void channelActive(ChannelHandlerContext ctx) {//1
			ByteBuf message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}

		/**
		 * 接收服务端返回的数据
		 * @param ctx
		 * @param msg
		 * @throws Exception
		 */
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			String body = (String) msg;
			System.out.println("Now is:" + body);
		}
	}





}
