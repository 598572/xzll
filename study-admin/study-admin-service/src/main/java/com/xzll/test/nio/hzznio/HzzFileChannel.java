package com.xzll.test.nio.hzznio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 22021/6/20 06:28
 * @Description: 1.获取一个FileChannel 3种方式 还有种是 open方式
 * <p>
 * FileInputStream fis = new FileInputStream(FILE_PATH);
 * //通过 FileInputStream 获取管道
 * FileChannel channelWithFileInputStream = fis.getChannel();
 * <p>
 * FileOutputStream fos = new FileOutputStream(FILE_PATH);
 * //通过 FileOutputStream 获取管道
 * FileChannel channelWithFileOutputStream = fis.getChannel();
 * <p>
 * RandomAccessFile raf = new RandomAccessFile(FILE_PATH, RW);
 * //通过 RandomAccessFile 获取管道
 * FileChannel channelWithRandomAccessFile = raf.getChannel();
 */
public class HzzFileChannel {
    private static final String FILE_PATH = "/Users/admin/hzz_main/xzll/FileMmapTest.txt";

    public static final String RW = "rw";//读写模式
    public static final String R = "r";//读模式
    public static final String W = "W";//写模式

    public static void main(String[] args) throws IOException {

        //一  使用FileChannel进行读/写数据

        //通过 RandomAccessFile 获取管道
        RandomAccessFile raf = new RandomAccessFile("/Users/admin/hzz_main/xzll/FileMmapTest4900000005.txt", RW);
        FileChannel readWriteChannel = raf.getChannel();
        //造点数据
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < 1; i++) {
            data.append(UUID.randomUUID() + "FileChannel 零拷贝演示 " + UUID.randomUUID());
        }

        //System.out.println("原数据：" + data);

        //申请内存空间
        ByteBuffer buffer = ByteBuffer.allocateDirect(128000);
        //清空一下
        buffer.clear();
        //往缓冲区(ByteBuffer) 写入造点数据
        buffer.put(data.toString().getBytes());
        //可不用也不影响 暂时不知道其用途
        //buffer.flip();

        //写入数据到通道 channel 其实是写到内核缓冲区
        readWriteChannel.write(buffer);
        //关闭通道
//        readWriteChannel.close();
//        raf.close();

        // 重新打开管道
//        raf = new RandomAccessFile("/Users/admin/hzz_main/xzll/FileMmapTest444.txt", "rw");
//        readWriteChannel = raf.getChannel();

        // 读取刚刚写入的数据
        readWriteChannel.read(buffer);

        // 打印读取出的数据
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println("FileChannel read()方法 读取到的数据：\n" + "   " + new String(bytes));

        readWriteChannel.close();
        raf.close();

        //一  使用FileChannel进行文件copy演示

        RandomAccessFile fromFile = new RandomAccessFile("/Users/admin/hzz_main/xzll/FileMmapTest.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("/Users/admin/hzz_main/xzll/FileMmapTest000019.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();


        // 将 fromFile 文件找那个的数据转移到 toFile 中去
        System.out.println("拷贝前应该是空的: " + readChannel(toChannel));
        fromChannel.transferTo(position, count, toChannel);
        System.out.println("拷贝后的结果 : " + readChannel(toChannel));

        //使用封装好的copy方法进行拷贝数据 复制的  org.apache.rocketmq.common.utils.IOTinyUtils 里边的copyFile方法
        copyFile4ZeroCopy("/Users/admin/hzz_main/xzll/FileMmapTest.txt", "/Users/admin/hzz_main/xzll/FileMmapTest" + UUID.randomUUID() + ".txt");


    }

    /**
     * 读取文件 将其转成String 方便查看文件内容
     *
     * @param channel
     * @return
     * @throws IOException
     */
    private static String readChannel(FileChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(3200);
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


    /**
     * 使用FileChannel进行拷贝文件 底层使用了 零拷贝技术  复制的  org.apache.rocketmq.common.utils.IOTinyUtils 里边的copyFile方法
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void copyFile4ZeroCopy(String source, String target) throws IOException {
        File sf = new File(source);
        if (!sf.exists()) {
            throw new IllegalArgumentException("source file does not exist.");
        }
        File tf = new File(target);
        tf.getParentFile().mkdirs();
        if (!tf.exists() && !tf.createNewFile()) {
            throw new RuntimeException("failed to create target file.");
        }

        FileChannel sc = null;
        FileChannel tc = null;
        try {
            tc = new FileOutputStream(tf).getChannel();
            sc = new FileInputStream(sf).getChannel();
            sc.transferTo(0, sc.size(), tc);
        } finally {
            if (null != sc) {
                sc.close();
            }
            if (null != tc) {
                tc.close();
            }
        }
    }
}
