package com.xzll.test.niotest.javanio;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 15:58
 * @Description: 基于选择器和channel构建的server服务器
 */
public class TimeServerOnChannelAndSelector {

	/**
	 * 提交任务的线程池
	 */
	private static ExecutorService executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));

	/**
	 * 使用selector和channel模拟服务器
	 *
	 * @throws IOException
	 */
	@Test
	public void serverOnSelectorAndChannel() throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(8080));
		ssc.configureBlocking(false);
		Selector selector = Selector.open();
		// ssc.validOps() 表示某channel支持的操作集合
		//这里将ServerSocketChannel 注册到选择器上 用于和客户端建立连接
		ssc.register(selector, ssc.validOps());//阻塞操作
		System.out.println("将ServerSocketChannel 注册到选择器上: 注册到选择器上的keys:  " + selector.selectedKeys());

		while (true) {
			//超时时间为1000ms(当超过这个时间还没有通道注册到选择器的话，将会返回0)
			/*
			select方法源码注释翻译:

			选择一组键，并且键对应的通道已准备好进行 IO（在这里的话 其实只有ServerSocketChannel注册上去了 IO操作其实就是指的accept()） 操作。此方法执行时候将会阻塞。
			此方法至少在选择一个通道后,或者当前线程被中断或者给定的超时期限到期后才返回，以先到者为准。此方法不提供实时保证：它通过调用 Object.wait(long) 方法来安排超时。
			参数：timeout - 如果为正，则在等待通道准备就绪时候，最多 或多或少地阻塞超时timeout毫秒(这个超时时间其实是有出入的 具体什么原因 和object的wait(long timeout))方法有关系；
			如果为零，则无限期阻塞；不能为负。
			返回：其就绪操作集合(channel)已更新的键数量，
			可能抛出：IOException – 如果发生 IO 错误 . ClosedSelectorException – 如果此选择器已关闭 . IllegalArgumentException – 如果超时参数的值为负
			 */
			int acceptCount = selector.select(1000);//阻塞操作 超过1000ms后还没有channel注册上来的话将会返回0 、 非阻塞操作可以使用 selectNow();
			if (acceptCount == 0) {
				continue;
			}
			//注册到选择器上的channel 后 会返回SelectionKey 根据selectionKey.isAcceptable() 或者
			//selectionKey.isReadable() ... 等类似方法 判断某Key对应的操作类型 ,并取出对应的channel进行请求处理
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

			while (keyIterator.hasNext()) {
				SelectionKey selectionKey = keyIterator.next();
				//System.out.println("选择器都有哪些key? : " + selectionKey.toString());
				//判断选择器是否有效
				if (selectionKey.isValid()) {
					//表示ServerSocketChannel
					//处理连接，网络应答的 ServerSocketChannel
					if (selectionKey.isAcceptable()) {
						System.out.println("selectionKey.isAcceptable() is true ");
						//进行网络处理
						ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
						SocketChannel socketChannel = server.accept();
						socketChannel.configureBlocking(false);
						//注册SocketChannel到选择器
						socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						System.out.println("get socketChannel with ServerSocketChannel and register to selector , " +
								"用于处理客户端请求的(连接建立后 会有n多个请求) 连接建立通过ServerSocketChannel， 处理客户端请求以及响应 通过SocketChannel ;");
					}

					//表示 SocketChannel
					if (selectionKey.isReadable()) {
						System.out.println("selectionKey.isReadable() is true ");
						//处理客户端发起的读请求并响应回客户端
						executor.submit(new TimeServerTask(selectionKey));
					}
					//响应完成后 移除，防止重复处理客户端请求
					keyIterator.remove();
				}
			}
		}
	}

	//处理器
	public class TimeServerTask implements Runnable {
		private SelectionKey selectionKey;

		public TimeServerTask(SelectionKey selectionKey) {
			this.selectionKey = selectionKey;
		}

		@Override
		public void run() {
			SocketChannel channel = (SocketChannel) selectionKey.channel();
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
			try {
				int count = 0;
				//读数据到bbytebuffer
				while ((count = channel.read(byteBuffer)) > 0) {
					byteBuffer.flip();
					byte[] request = new byte[byteBuffer.remaining()];
					byteBuffer.get(request);
					String requestStr = new String(request);
					System.out.println("requestStr : " + requestStr);
					byteBuffer.clear();
					if (StringUtils.isNotBlank(requestStr) && !"GET CURRENT TIME".equals(requestStr)) {
						byteBuffer.put("BAD_REQUEST".getBytes());
						byteBuffer.flip();//注意这个一定要 flip否则内容为空 读不到
						channel.write(byteBuffer);
					} else {
						byteBuffer.put(Calendar.getInstance().getTime().toLocaleString().getBytes());
						byteBuffer.flip();
						channel.write(byteBuffer);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				selectionKey.cancel();
			}
		}
	}
}
