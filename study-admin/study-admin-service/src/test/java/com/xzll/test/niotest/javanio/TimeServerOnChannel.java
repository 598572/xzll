package com.xzll.test.niotest.javanio;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 15:58
 * @Description:
 */
public class TimeServerOnChannel {

	/**
	 * 请求队列
	 */
	private BlockingQueue<SocketChannel> dealRequestQueue = new LinkedBlockingQueue<SocketChannel>();
	/**
	 * 处理请求后返回的future. 循环判断每一个future是否完成，没完成放入
	 */
	private BlockingQueue<Future<SocketChannel>> workingQueue = new LinkedBlockingQueue<Future<SocketChannel>>();
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	{
		//创建个静态块 run任务，用于循环处理队列中的客户端请求即 SocketChannel
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						//task1：迭代当前dealRequestQueue中的SocketChannel，提交到线程池中执行任务，并将其移到workingQueue中
						for (int i = 0; i < dealRequestQueue.size(); i++) {
							SocketChannel socketChannel = dealRequestQueue.poll();
							if (socketChannel != null) {
								Future<SocketChannel> result = executor.submit(new TimeServerHandleTask(socketChannel, executor), socketChannel);
								//处理完后的结果添加进 workingQueue
								workingQueue.put(result);
							}
						}
						//task2：迭代当前workingQueue中的SocketChannel，如果任务执行完成，get出来  打印一下做个记录，如果没完成 ，放回workingQueue队列。
						for (int i = 0; i < workingQueue.size(); i++) {
							Future<SocketChannel> future = workingQueue.poll();
							if (!future.isDone()) {
								//判断 Future 任务是否完成 没完成的话 添加进 workingQueue
								workingQueue.put(future);
								continue;
							}
							SocketChannel channel = null;
							try {
								//获取任务打印log
								channel = future.get();
								System.out.println("SocketChannel 任务完成!");
							} catch (ExecutionException e) {
								//如果future.get()抛出异常，关闭SocketChannel，不再放回dealRequestQueue
								channel.close();
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 模拟channel+线程池 服务器 不停的检测，处理客户端请求
	 *
	 * @throws IOException
	 */
	@Test
	public void timer() throws IOException {
		TimeServerOnChannel timeServer = new TimeServerOnChannel();
		//创建 ServerSocketChannel
		ServerSocketChannel ssc = ServerSocketChannel.open();
		//设置模式为非阻塞
		ssc.configureBlocking(false);
		//绑定端口
		ssc.socket().bind(new InetSocketAddress(8080));
		//不停的处理客户端请求
		while (true) {
			//不停检测是否有请求 有请求就应答，无请求continue;
			SocketChannel socketChannel = ssc.accept();
			if (socketChannel == null) {
				//System.out.println("没有socket 继续循环 ");
				continue;
			} else {
				socketChannel.configureBlocking(false);
				System.out.println("有请求，将socketChannel 添加进 (LinkedBlockingQueue) 队列中去 ");
				timeServer.dealRequestQueue.add(socketChannel);
			}
		}
	}

	//任务处理器
	public static class TimeServerHandleTask implements Runnable {
		SocketChannel socketChannel;
		ExecutorService executorService;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

		//使用构造方法初始化run方法需要的东西
		public TimeServerHandleTask(SocketChannel socketChannel, ExecutorService executorService) {
			this.socketChannel = socketChannel;
			this.executorService = executorService;
		}

		@Override
		public void run() {
			try {
				//从 socketChannel 读取到 byteBuffer
				if (socketChannel.read(byteBuffer) > 0) {
					while (true) {
						//翻转 byteBuffer 以便get出数据
						byteBuffer.flip();
						//如果可读取元素小于 "GET CURRENT TIME"字符长度的话 进行压缩,当第二次循环时候，
						//将会大于 "GET CURRENT TIME"的长度 走到下边的第一个if里 然后像客户端发送BAD_REQUEST
						if (byteBuffer.remaining() < "GET CURRENT TIME".length()) {
							//System.out.println(" byteBuffer.remaining() < \"GET CURRENT TIME\".length() ");
							//将数据压缩到内存块的头部，后续的put操作将从最后一个元素开始
							byteBuffer.compact();
							//读到压缩后的 byteBuffer中
							socketChannel.read(byteBuffer);
							continue;
						}
						//说明 socketChannel已经全部读取到 byteBuffer
						//接着进行读取byteBuffer的数据
						byte[] request = new byte[byteBuffer.remaining()];
						byteBuffer.get(request);
						String requestStr = new String(request);
						byteBuffer.clear();
						System.out.println(" requestStr : " + requestStr);
						if (StringUtils.isNotBlank(requestStr) && !"GET CURRENT TIME".equals(requestStr)) {
							//如果不是约定的内容 那么返回 BAD_REQUEST
							ByteBuffer put = byteBuffer.put("BAD_REQUEST".getBytes());
							put.flip();
							// 注意这里write一定是  put方法返回的新的ByteBuffer
							socketChannel.write(put);
							byteBuffer.clear();
						} else {
							//走到这里 说明是约定的内容，使用write响应给客户端。
							ByteBuffer byteBuffer = this.byteBuffer.put(Calendar.getInstance()
									.getTime().toLocaleString().getBytes());
							byteBuffer.flip();
							socketChannel.write(byteBuffer);
							System.out.println("数据(当前时间) 正常响应给客户端了 内容为: " + byteBuffer.toString());
						}
						//注意这里一定要 clear 否则下次循环时候，数据将会错乱
						byteBuffer.clear();

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("不管响应没响应，都打印一下 好调试: " + "hzz无敌");
			}
		}
	}

}
