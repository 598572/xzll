package com.xzll.test.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 07:50
 * @Description:
 *
 * 今天看RocketMq 消息刷盘机制  使用了mmap 和 FileChannel 故学习一下
 *
 * 参考： https://blog.csdn.net/alex_xfboy/article/details/90174840
 * 参考: https://zhuanlan.zhihu.com/p/258934554
 * 我参考的大佬参考了: https://www.linuxjournal.com/article/6345  哈哈
 * 关于 Direct IO  参考  https://github.com/598572/jaydio  据说挺牛逼？ 咱也不敢问 （听说被林纳斯喷了 说这个IO 垃圾 ）
 * 感谢大佬
 *
 *
 * strace在linux下用来跟踪某个进程的系统调用
 * 在mac下，对应的命令是：dtruss
 *
 * 查看FileMmapTest java 进程对系统的调用     sudo dtruss -c java com/xzll/test/nio/FileMmapTest.java
 */
public class FileChannelAndMmapTest {

    public static void main(String[] args) throws Exception {
        //记录开始时间
        long start = System.currentTimeMillis();
        //通过RandomAccessFile的方式获取文件的Channel，这种方式针对随机读写的文件较为常用，我们用文件一般是随机读写
        RandomAccessFile randomAccessFile = new RandomAccessFile("./FileChannelCopySource.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        System.out.println("FileChannel初始化时间：" + (System.currentTimeMillis() - start) + "ms");

        //内存映射文件，模式是READ_WRITE，如果文件不存在，就会被创建
        MappedByteBuffer mappedByteBuffer1 = channel.map(FileChannel.MapMode.READ_WRITE, 0, 128 * 1024 * 1024);
        MappedByteBuffer mappedByteBuffer2 = channel.map(FileChannel.MapMode.READ_WRITE, 0, 128 * 1024 * 1024);

        System.out.println("MMAPFile初始化时间：" + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        testFileChannelSequentialRW(channel);
        System.out.println("FileChannel顺序读写时间：" + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        testFileMMapSequentialRW(mappedByteBuffer1, mappedByteBuffer2);
        System.out.println("MMAPFile顺序读写时间：" + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        try {
            testFileChannelRandomRW(channel);
            System.out.println("FileChannel随机读写时间：" + (System.currentTimeMillis() - start) + "ms");
        } finally {
            randomAccessFile.close();
        }

        //文件关闭不影响MMAP写入和读取
        start = System.currentTimeMillis();
        testFileMMapRandomRW(mappedByteBuffer1, mappedByteBuffer2);
        System.out.println("MMAPFile随机读写时间：" + (System.currentTimeMillis() - start) + "ms");
//        Thread.sleep(500000);
    }


    public static void testFileChannelSequentialRW(FileChannel fileChannel) throws Exception {
        byte[] bytes = "测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1".getBytes();
        byte[] to = new byte[bytes.length];
        //分配直接内存，减少复制
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
        //顺序写入
        for (int i = 0; i < 100000; i++) {
            byteBuffer.put(bytes);
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.flip();
        }

        fileChannel.position(0);
        //顺序读取
        for (int i = 0; i < 100000; i++) {
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            byteBuffer.get(to);
            byteBuffer.flip();
        }
    }

    public static void testFileMMapSequentialRW(MappedByteBuffer mappedByteBuffer1, MappedByteBuffer mappedByteBuffer2) throws Exception {
        byte[] bytes = "测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2".getBytes();
        byte[] to = new byte[bytes.length];

        //顺序写入
        for (int i = 0; i < 100000; i++) {
            mappedByteBuffer1.put(bytes);
        }
        //顺序读取
        for (int i = 0; i < 100000; i++) {
            mappedByteBuffer2.get(to);
        }
    }

    public static void testFileChannelRandomRW(FileChannel fileChannel) throws Exception {
        try {
            byte[] bytes = "测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1测试字符串1".getBytes();
            byte[] to = new byte[bytes.length];
            //分配直接内存，减少复制
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
            //随机写入
            for (int i = 0; i < 100000; i++) {
                byteBuffer.put(bytes);
                byteBuffer.flip();
                fileChannel.position(new Random(i).nextInt(bytes.length * 100000));
                fileChannel.write(byteBuffer);
                byteBuffer.flip();
            }
            //随机读取
            for (int i = 0; i < 100000; i++) {
                fileChannel.position(new Random(i).nextInt(bytes.length * 100000));
                fileChannel.read(byteBuffer);
                byteBuffer.flip();
                byteBuffer.get(to);
                byteBuffer.flip();
            }
        } finally {
            fileChannel.close();
        }
    }

    public static void testFileMMapRandomRW(MappedByteBuffer mappedByteBuffer1, MappedByteBuffer mappedByteBuffer2) throws Exception {
        byte[] bytes = "测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2测试字符串2".getBytes();
        byte[] to = new byte[bytes.length];

        //随机写入
        for (int i = 0; i < 100000; i++) {
            mappedByteBuffer1.position(new Random(i).nextInt(bytes.length * 100000));
            mappedByteBuffer1.put(bytes);
        }
        //随机读取
        for (int i = 0; i < 100000; i++) {
            mappedByteBuffer2.position(new Random(i).nextInt(bytes.length * 100000));
            mappedByteBuffer2.get(to);
        }
    }
}
