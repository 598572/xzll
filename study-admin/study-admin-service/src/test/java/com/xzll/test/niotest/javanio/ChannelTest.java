package com.xzll.test.niotest.javanio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 12:52
 * @Description: channel都是双工通道，但是对于fileChannel来说 ，其接口生名来看 其是双工的 但是如果其创建在 FileInputStream 或者 FileOutputStream 对象上的话，那就是单向的了(只能读或者写)
 * 因为 FileInputStream 只能读， FileOutputStream只能写
 * <p>
 * java.net 的 socket 类也有新的 getChannel( )方法。这些方法虽然能返回一个相应的 socket 通道对象，但它们却并非新通道的来源，RandomAccessFile.getChannel( )方法才是。只有在已经有通道存在的时候，它们才返回与一个 socket 关联的通道；它们永远不会创建新通道。
 */
public class ChannelTest {
    public static final String GREETING = "Hello I must be going.\r\n";

    /*
    这段程序的作用是，在8085端口上接受client的请求，一旦接收到client的请求，会给其回复固定的字符串响应"Hello I must be going."
    也就是说其模拟的是服务端
     */
    @Test
    public void channelAccept() throws IOException, InterruptedException {
        int port = 8085; // port
        ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
        //创建个ServerSocketChannel 通道 用于处理客户端请求
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //绑定ip和端口
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", port));
        //设置非阻塞模式
        ssc.configureBlocking(false);
        //不停的检测连接信息
        while (true) {
            System.out.println("Waiting for connections");
            /*
            accept方法源码方法注释+个人理解并添加了一部分:

            接受与此通道的套接字建立连接。
            (NIO模式下)如果此通道处于非阻塞模式，且没有挂起的连接，则此方法将立即返回 null; (BIO模式下)否则它将无限期地阻塞，直到新连接可用或发生 I/O 错误。
            此方法返回的套接字通道（如果有）将处于阻塞模式，无论此通道的阻塞模式如何。
            此方法执行与 ServerSocket 类的 accept 方法完全相同的安全检查。 也就是说，如果已经安装了安全管理器，那么对于每个新连接，此方法都会验证安全管理器的 checkAccept 方法是否允许连接的远程端点的地址和端口号。
            返回：
            新连接的套接字通道，如果此通道处于非阻塞模式并且没有可用的连接可被接受，则为 null

            //总结: 如果是 SocketChannel.accept的话，会一直阻塞，如果是ServerSocketChannel.accept(); 且设置configureBlocking=false的话 不会阻塞会直接返回null ;

             */
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                // no connections, snooze a while 没有连接，小睡一会儿
                Thread.sleep(2000);
                System.out.println("没有连接，小睡一会儿");
            } else {
                sc.configureBlocking(false);
                ByteBuffer allocate = ByteBuffer.allocateDirect(16 * 1024);
                while (sc.read(allocate) > 0) {
                    allocate.flip();
                    while (buffer.hasRemaining()) {
                        byte b = buffer.get();
                        System.out.println(b);
                    }
                    allocate.clear();
                }

                System.out.println("Incoming connection from: "
                        + sc.socket().getRemoteSocketAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }




//    @Test
//    public void cl(){
//        InetSocketAddress addr = new InetSocketAddress (host, port);
//        SocketChannel sc = SocketChannel.open( );
//        sc.configureBlocking (false);
//        sc.connect (addr);
//        while ( ! sc.finishConnect( )) {
//            doSomethingElse( );
//        }
//        doSomethingWithChannel (sc);
//        sc.close( );
//    }


    /*
    概念解释: 参考 : http://www.tianshouzhi.com/api/tutorials/netty/316

        1.Socket通道详解:

            在通道类中，DatagramChannel 和 SocketChannel 实现定义读和写功能的接口而 ServerSocketChannel不实现。 ServerSocketChannel 负责监听传入的连接和创建新的 SocketChannel 对象，它本身从不传输数据。
            全部 NIO中的socket 通道类（ DatagramChannel、 SocketChannel 和 ServerSocketChannel）在被实例化时都会创建一个对等的BIO中的 socket 对象（ Socket、 ServerSocket和 DatagramSocket）。
            DatagramChannel、 SocketChannel 和 ServerSocketChannel通道类都定义了socket()方法，我们可以通过这个方法获取其关联的socket对象。另外每个Socket、 ServerSocket和 DatagramSocket都定义了getChannel()方法，来获取对应的通道。
            需要注意是，只有通过通道类创建的socket对象，其getChannel方法才能返回对应的通道，如果直接new了socket对象，那么其getChannel方法返回的永远是null。

        2.非阻塞模式解释:
            在通道类中，DatagramChannel 和 SocketChannel 实现定义读和写功能的接口, 而ServerSocketChannel不实现
            通道可以以阻塞（ blocking）或非阻塞（ nonblocking）模式运行。非阻塞模式的通道永远不会让调用的线程休眠。请求的操作要么立即完成，要么返回一个结果表明未进行任何操作。
            这个陈述虽然简单却有着深远的含义。传统 Java socket的阻塞性质曾经是 Java 程序可伸缩性的最重要制约之一。非阻塞 I/O 是许多复杂的、高性能的程序构建的基础。
            回顾我们之前讲解的BIO编程中，不能"以尽可能少的线程，处理尽可能多的client请求"，就是因为通过Socket的getInputStream方法的read方法是阻塞的，一旦没有数据可读，处理线程就会被一直被block住。
            默认情况下，一个通道创建，总是阻塞的，我们可以通过调用configureBlocking(boolean)方法即可，传递参数值为 true 则设为阻塞模式，参数值为 false 值设为非阻塞模式。而 isBlocking()方法来判断某个 socket 通道当前处于哪种模式

        3.下边这个方法是关于channel的一些演示

     */
    @Test
    public void channelCreate() throws IOException {
//        SocketChannel sc = SocketChannel.open();
//
//        sc.connect(new InetSocketAddress("127.0.0.1", 8081));

        //TCP  ServerSocketChannel 负责监听传入的连接和创建新的 SocketChannel 对象，它本身从不传输数据。
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //指定监听端口
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8081));
        ssc.configureBlocking(false);
        boolean blocking = ssc.isBlocking();
        System.out.println(blocking);

        //UDP
//        DatagramChannel dc = DatagramChannel.open( );
//
//        RandomAccessFile raf = new RandomAccessFile ("somefile", "r");
//        FileChannel fc = raf.getChannel( );
//        System.out.println(fc);

        //默认情况下，一个通道创建，总是阻塞的，我们可以通过调用configureBlocking(boolean)方法即可，传递参数值为 true 则设为阻塞模式，参数值为 false 值设为非阻塞模式。而 isBlocking()方法来判断某个 socket 通道当前处于哪种模式
        //sc.configureBlocking(false); //false代表非阻塞，ture代表阻塞模式


    }

}
