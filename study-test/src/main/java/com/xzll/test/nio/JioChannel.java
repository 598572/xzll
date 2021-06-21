package com.xzll.test.nio;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 07:28
 * @Description:
 */
public class JioChannel {

    public static void main(String[] args) {
        JioChannel channel = new JioChannel();
        try {
            if (args.length < 3) {
                System.out.println("usage: JioChannel <source> "+
                        "<destination> <mode>\n");
                return;
            }

            if ("1".equals(args[2])) { //传统方式的复制
                channel.copy(args[0], args[1]);
            } else if ("2".equals(args[2])) { //mmap的方式
                channel.mmap4zeroCopy(args[0], args[1]);
            } else if ("3".equals(args[2])) { //sendfile的方式
                channel.sendfile4zeroCopy(args[0], args[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 传统方式的复制
     *
     * @param from
     * @param to
     * @throws IOException
     */
    public void copy(String from, String to) throws IOException {
        byte[] data = new byte[8 * 1024];
        FileInputStream fis = null;
        FileOutputStream fos = null;
        long bytesToCopy = new File(from).length();
        long bytesCopied = 0;
        try {
            fis = new FileInputStream(from);
            fos = new FileOutputStream(to);

            while (bytesCopied < bytesToCopy) {
                fis.read(data);
                fos.write(data);
                bytesCopied += data.length;
            }
            fos.flush();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * mmap的方式复制
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

    /**
     * sendfile的方式复制文件
     *
     * @param from
     * @param to
     * @throws IOException
     */
    public void sendfile4zeroCopy(String from, String to) throws IOException {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(from).getChannel();
            destination = new FileOutputStream(to).getChannel();
            source.transferTo(0, source.size(), destination);
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
