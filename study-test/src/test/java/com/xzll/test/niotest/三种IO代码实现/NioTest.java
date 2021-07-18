package com.xzll.test.niotest.三种IO代码实现;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 11:39
 * @Description:
 */
public class NioTest {

    @Test
    public void test() throws IOException {
        SocketChannel channel = SocketChannel.open();
        Selector selector = Selector.open();
        channel.configureBlocking(false);
        SelectionKey key2 = channel.register(selector, SelectionKey.OP_READ);
        while (true) {
            int readyChannels = selector.select();
            if (readyChannels == 0){
                continue;
            }
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();
                if (key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel. 一个连接被一个 ServerSocketChannel 接受。
                    System.out.println("一个连接被一个 ServerSocketChannel 接受。");
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.已与远程服务器建立连接
                    System.out.println("已与远程服务器建立连接");
                } else if (key.isReadable()) {
                    // a channel is ready for reading  已准备好读
                    System.out.println("已准备好读");
                } else if (key.isWritable()) {
                    // a channel is ready for writing  已准备好写
                    System.out.println("已准备好写");
                }
                keyIterator.remove();
            }
        }
    }
}
