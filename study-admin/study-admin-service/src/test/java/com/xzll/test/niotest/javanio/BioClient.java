package com.xzll.test.niotest.javanio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author: hzz
 * @Date: 2022/10/15 20:06:10
 * @Description:
 */
public class BioClient {

	public static void main(String[] args) throws IOException {
		// 创建一个socket，并绑定本地ip及6666端口
		Socket socket = new Socket("127.0.0.1", 6666);
		// 获取output流
		OutputStream outputStream = socket.getOutputStream();
		// 向输出流中写入
		outputStream.write("Hello World!".getBytes());
		outputStream.flush();
		// 关闭连接
		socket.close();
	}

}
