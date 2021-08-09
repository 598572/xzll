package com.xzll.test.niotest.netty.另一个netty的示例基于国外一篇文档;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 18:47
 * @Description: 基于 https://programmer.help/blogs/netty-basic-to-entry-2-netty-core-functions-and-thread-model.html 的netty代码和释义
 */
public class NettyClient {

	public static void main(String[] args) throws Exception {
		//客户端需要一个事件循环线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			//创建客户端启动对象
			//注意客户端使用 Bootstrap 而不是 ServerBootstrap
			Bootstrap bootstrap = new Bootstrap();
			//设置相关参数
			bootstrap.group(group) //设置线程组
					.channel(NioSocketChannel.class) // 使用 NioSocketChannel 作为客户端的通道
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel channel) throws Exception {
							//添加处理器
							channel.pipeline().addLast(new NettyClientHandler());
						}
					});
			System.out.println("netty client start");
			//启动客户端连接服务器
			ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync(); //监控关闭的通道
			channelFuture.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}


	public static class NettyClientHandler extends ChannelInboundHandlerAdapter {

		/**
		 * 该方法在客户端连接服务器时触发 相当于握手
		 *
		 * @param ctx
		 * @throws Exception
		 */
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ByteBuf buf = Unpooled.copiedBuffer("HelloServer", CharsetUtil.UTF_8);
			ctx.writeAndFlush(buf);
		}

		/**
		 * 通道中有读事件时触发，即服务端向客户端发送数据
		 *
		 * @param ctx
		 * @param msg
		 * @throws Exception
		 */
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			System.out.println("Receive the message from the server:" + buf.toString(CharsetUtil.UTF_8));
			System.out.println("Server address: " + ctx.channel().remoteAddress());
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}
	}


}
