package com.xzll.test.niotest.javanio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 16:03
 * @Description: 很重要的一个组件
 */
public class SelectorTest {

    /**
     * 创建选择器
     *
     * @throws IOException
     */
    @Test
    public void createSelector() throws IOException {
//        方式一：
        Selector selector = Selector.open();
//        方式二：
        SelectorProvider provider = SelectorProvider.provider();
        Selector abstractSelector = provider.openSelector();
    }

    /**
     * 将ServerSocketChannel 和 SocketChannel 注册到 选择器中
     *
     * @throws IOException
     */
    @Test
    public void register() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress("localhost", 8086));
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        SelectionKey sscSelectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);//注册ServerSocketChannel
        while (true) {
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                continue;
            }
            sc.configureBlocking(false);
            //注册SocketChannel
            SelectionKey scselectionKey = sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			SelectableChannel channel = scselectionKey.channel();
			System.out.println(scselectionKey.toString());
            //...其他操作
        }
    }
}
