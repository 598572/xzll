package com.xzll.test.niotest.netty.另一个netty的示例基于国外一篇文档;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/07/18 18:43
 * @Description: 基于  https://programmer.help/blogs/netty-basic-to-entry-2-netty-core-functions-and-thread-model.html 的netty代码和释义
 */
public class NettyServer {


	public static void main(String[] args) throws Exception {
//		创建两个线程组，bossGroup 和 workerGroup。 NioEventLoop 的子线程数默认为两个 cpu core bossGroup 只处理连接请求。
//		真正与客户端的业务处理将由workerGroup来完成
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//创建服务器端启动对象
			ServerBootstrap bootstrap = new ServerBootstrap();
			//使用链式编程配置参数
			//设置两个线程组
			bootstrap.group(bossGroup, workerGroup)
					//使用 NioServerSocketChannel 作为服务端的通道
					.channel(NioServerSocketChannel.class)
					// 初始化服务器连接队列的大小。服务器按顺序处理客户端连接请求，因此一次只能处理一个客户端连接。
					// 当多个客户端同时到来时，服务器会将无法处理的客户端连接请求放入队列进行处理
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						//创建channel 初始化对象并设置初始化参数
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//在 workerGroup 的 SocketChannel 上设置处理器编解码器
							ch.pipeline().addLast(new NettyServerHandler());
						}
					});
			System.out.println("netty server start. . ");
			//绑定端口并同步，生成ChannelFuture异步对象，通过isDone()等方法判断异步事件的执行状态
			//启动服务器（并绑定端口），bind是异步操作，sync方法是等待异步操作完成
			ChannelFuture cf = bootstrap.bind(9000).sync();
			//用 ChannelFuture 注册monitor来监听我们关心的事件
            /*cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("Listening port 9000 succeeded);
                    } else {
                        System.out.println("Listening port 9000 failed ');
                    }
                }
            });*/
			//监控通道关闭。 closeFuture 是异步操作。
			//通过sync方法等待通道关闭， 会阻塞等待通道关闭
			cf.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static class NettyServerHandler extends ChannelInboundHandlerAdapter {

		/**
		 * 读取客户端发送的数据
		 *
		 * @param ctx 上下文对象，包括通道、管道
		 * @param msg 是客户端发送的数据
		 * @throws Exception
		 */
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("Server read thread " + Thread.currentThread().getName());
			//Channel channel = ctx.channel();
			//ChannelPipeline pipeline = ctx.pipeline(); / / 本质上是双向链接，出站和入站
			//将 msg 转换为 ByteBuffer，类似于 NIO 的 ByteBuffer
			ByteBuf buf = (ByteBuf) msg;
			System.out.println("The message sent by the client is:" + buf.toString(CharsetUtil.UTF_8));
		}

		/**
		 * 数据读取完成的处理方法
		 *
		 * @param ctx
		 * @throws Exception
		 */
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ByteBuf buf = Unpooled.copiedBuffer("HelloClient", CharsetUtil.UTF_8);
			ctx.writeAndFlush(buf);
		}


		/**
		 * 为了处理异常，需要关闭通道
		 *
		 * @param ctx
		 * @param cause
		 * @throws Exception
		 */
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}

	}
}
