package com.xzll.test.niotest.javanio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: hzz
 * @Date: 2022/10/15 20:05:27
 * @Description:
 */
public class BioServer {
	public static void main(String[] args) throws IOException {
		// 创建线程池
		ExecutorService executorService = Executors.newCachedThreadPool();
		// 创建ServerSocket并且监听6666端口
		ServerSocket serverSocket = new ServerSocket(6666);
		while (true) {
			// 监听---一直等待客户端连接
			Socket socket = serverSocket.accept();
			System.out.println("有客户端连接来了，此时已经是三次握手完成状态"+socket.toString());
			try {
				// 获取客户端发送过来的输入流
				InputStream inputStream = socket.getInputStream();
				byte[] bytes = new byte[1024];
				int read = inputStream.read(bytes);
				System.out.println("有数据报来了 read:"+read);
				// 读取发送过来的信息并打印
				if (read != -1) {
					System.out.println(new String(bytes, 0, read));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 断开通讯
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
