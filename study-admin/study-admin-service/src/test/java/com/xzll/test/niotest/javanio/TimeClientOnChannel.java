package com.xzll.test.niotest.javanio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 16:25
 * @Description:
 */
public class TimeClientOnChannel {
    //连接超时时间
    static int connectTimeOut = 3000;
    static ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    @Test
    public  void timeClient() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",8080));
        socketChannel.configureBlocking(false);
        long start = System.currentTimeMillis();
        while (!socketChannel.finishConnect()) {
            if (System.currentTimeMillis() - start >= connectTimeOut) {
                throw new RuntimeException("尝试建立连接超过3秒");
            }
        }
        //如果走到这一步，说明连接建立成功
        while (true) {
//            buffer.put("GET CURRENT TIME".getBytes());//当发送的长度小于 GET CURRENT TIME 的length时候 服务端将响应 BAD_REQUEST
//            buffer.flip();
//            socketChannel.write(buffer);
//            buffer.clear();
//            if (socketChannel.read(buffer) > 0) {
//                buffer.flip();
//                byte[] response = new byte[buffer.remaining()];
//                buffer.get(response);
//                System.out.println("我是客户端，服务端响应内容为 : " + new String(response));
//
//            }
//            buffer.clear();
            Thread.sleep(5000);
        }

    }
}
