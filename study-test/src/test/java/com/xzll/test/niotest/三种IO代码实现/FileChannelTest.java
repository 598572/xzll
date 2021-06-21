package com.xzll.test.niotest.三种IO代码实现;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 09:10
 * @Description: FileChannel 演示
 * 备注: 本示例关闭资源操作 均是 使用的 try-with-resources语句，确保在语句执行完毕后，每个资源都被自动关闭 。
 */
public class FileChannelTest extends IOCommonTest {


    private static final String SOURCE_FILE = "/Users/admin/hzz_main/xzll/sendfile源文件.txt";
    private static final String TARGET_FILE = "/Users/admin/hzz_main/xzll/sendfile目标文件.txt";


    public void createFileNotExist() {
        Path source = Paths.get(SOURCE_FILE);
        try {
            if (Files.exists(source)) {
                Files.delete(source);
            }
            if (!Files.exists(source))
                Files.createFile(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化 文件 没有就创建 并且将造的数据写入源文件
     */
    public void writeTest() {
        long l1 = System.currentTimeMillis();
        Path source = Paths.get(SOURCE_FILE);
        byte[] bytes = CONTENT.getBytes();
        System.out.println("sendfile writeTest 获取文件用时 :" + (System.currentTimeMillis() - l1) + "  ms  不算入性能之内");
        //拷贝前往源文件里写点数据 fromChannel.write();
        long openBefore = System.currentTimeMillis();
        try (FileChannel fromChannel = FileChannel.open(source, StandardOpenOption.READ,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            System.out.println("sendfile 获取FileChannel用时 :" + (System.currentTimeMillis() - openBefore) + " ms ");
            long l = System.currentTimeMillis();
//            fromChannel.write(ByteBuffer.wrap(bytes,0,bytes.length));//将字节数组包装到缓冲区中 ，并从给定的缓冲区将字节写入此通道。
            fromChannel.write(ByteBuffer.wrap(bytes));//将字节数组包装到缓冲区中 ，并从给定的缓冲区将字节写入此通道。
            System.out.println("sendfile fileChannel写入数据用时: "
                    + (System.currentTimeMillis() - l) + " ms " + "文件大小 " + fromChannel.size() + " byte " +
                    "写入进文件---> " + SOURCE_FILE
            );
        } catch (Exception e) {
            System.out.println("初始化失败");
            e.printStackTrace();
        }

    }

    /**
     * 通过 transferTo() 将 fromChannel 中的数据拷贝到 toChannel
     *
     * @throws Exception
     */
    //@Test
    public void testCopy() throws Exception {
        long l = System.currentTimeMillis();
        try (FileChannel fromChannel = new RandomAccessFile(
                SOURCE_FILE, "rw").getChannel();
             FileChannel toChannel = new RandomAccessFile(
                     TARGET_FILE, "rw").getChannel()) {
            long position = 0L;
            long offset = fromChannel.size();

            System.out.println("拷贝前: 文件大小 : " + toChannel.size());

            fromChannel.transferTo(position, offset, toChannel);
            System.out.println("transferTo 拷贝文件用时: "
                    + (System.currentTimeMillis() - l) + " ms " + "文件大小 " + toChannel.size() + " byte ");
        }
    }


    /**
     * 通过 transferFrom() 将 fromChannel 中的数据拷贝到 toChannel
     *
     * @throws Exception
     */
    @Test
    public void transferFrom() throws Exception {
        long l = System.currentTimeMillis();
        try (FileChannel fromChannel = new RandomAccessFile(
                SOURCE_FILE, "rw").getChannel();
             FileChannel toChannel = new RandomAccessFile(
                     TARGET_FILE, "rw").getChannel()) {
            long position = 0L;
            long offset = fromChannel.size();
            System.out.println("拷贝前: 文件大小 : " + toChannel.size() );
            toChannel.transferFrom(fromChannel, position, offset);
            System.out.println("拷贝后: 文件大小 : " + toChannel.size());

            System.out.println("transferFrom FileChannel 拷贝文件用时:"
                    + (System.currentTimeMillis() - l) + " ms " + "文件大小 " + toChannel.size() + " byte ");
        }
    }

    /**
     * 读取文件 将其转成String 方便查看文件内容 为了不影响性能测试结果 所以不调用该方法了
     *
     * @param channel
     * @return
     * @throws IOException
     */
    private static String readChannel(FileChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(3200);//在非堆 （直接内存）上开辟用户缓存空间
        buffer.clear();

        // 将 channel 读取位置设为 0，也就是文件开始位置
        channel.position(0);
        channel.read(buffer);

        // 再次将文件位置归零
        channel.position(0);

        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return new String(bytes);
    }

}
