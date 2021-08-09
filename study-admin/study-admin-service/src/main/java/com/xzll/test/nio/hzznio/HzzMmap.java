package com.xzll.test.nio.hzznio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;


/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 07:10
 * @Description:
 */
public class HzzMmap {
    public static void main(String[] args) throws Exception {

        //记录开始时间
        long start = System.currentTimeMillis();
        //通过RandomAccessFile的方式获取文件的Channel，这种方式针对随机读写的文件较为常用，我们用文件一般是随机读写
        RandomAccessFile randomAccessFile = new RandomAccessFile("./FileMmapTest.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        System.out.println("FileChannel初始化时间：" + (System.currentTimeMillis() - start) + "ms");

        //内存映射文件，模式是READ_WRITE，如果文件不存在，就会被创建
        MappedByteBuffer mappedByteBuffer1 = channel.map(FileChannel.MapMode.READ_WRITE, 0, 128 * 1024 * 1024);
        MappedByteBuffer mappedByteBuffer2 = channel.map(FileChannel.MapMode.READ_WRITE, 0, 128 * 1024 * 1024);

        System.out.println("MMAPFile初始化时间：" + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        testFileMMapSequentialRW(mappedByteBuffer1, mappedByteBuffer2);
        System.out.println("MMAPFile顺序读写时间：" + (System.currentTimeMillis() - start) + "ms");


        //文件关闭不影响MMAP写入和读取
        start = System.currentTimeMillis();
        testFileMMapRandomRW(mappedByteBuffer1, mappedByteBuffer2);
        System.out.println("MMAPFile随机读写时间：" + (System.currentTimeMillis() - start) + "ms");


    }


    /**
     * mmap 顺序读写
     *
     * @param mappedByteBuffer1
     * @param mappedByteBuffer2
     * @throws Exception
     */
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

    /**
     * mmap随机读写
     *
     * @param mappedByteBuffer1
     * @param mappedByteBuffer2
     * @throws Exception
     */
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

    /**
     * mmap方式 写入
     *
     * @param from
     * @param to
     * @throws IOException
     */
    public void mmap4zeroCopy(String from, String to) throws IOException {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new RandomAccessFile(from, "r").getChannel();
            destination = new RandomAccessFile(to, "rw").getChannel();

            MappedByteBuffer inMappedBuf =
                    source.map(FileChannel.MapMode.READ_ONLY, 0, source.size());

            destination.write(inMappedBuf);
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
