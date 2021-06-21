package com.xzll.test.nio;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 07:30
 * @Description: 代码引用 https://blog.csdn.net/zhxdick/article/details/81084672 大佬的博客 感谢!!!!!!!!
 *
 *https://www.cnblogs.com/lsgxeva/p/11619464.html
 *
 *
 *
 *
 *
 */
public class FileCopyTest {
    /**
     * 通过字节流的方式复制文件
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @throws FileNotFoundException 未找到文件异常
     */
    public static void fileCopyNormal(File fromFile, File toFile) throws FileNotFoundException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(fromFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(toFile));
            //用户态缓冲有1kB这么大，不算小了
            byte[] bytes = new byte[1024];
            int i;
            //读取到输入流数据，然后写入到输出流中去，实现复制
            while ((i = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用FileChannel进行文件复制
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     */
    public static void fileCopyWithFileChannel(File fromFile, File toFile) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutput = null;
        try {
            fileInputStream = new FileInputStream(fromFile);
            fileOutputStream = new FileOutputStream(toFile);
            //得到fileInputStream的文件通道
            fileChannelInput = fileInputStream.getChannel();
            //得到fileOutputStream的文件通道
            fileChannelOutput = fileOutputStream.getChannel();
            //将fileChannelInput通道的数据，写入到fileChannelOutput通道
            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileChannelInput != null) {
                    fileChannelInput.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (fileChannelOutput != null) {
                    fileChannelOutput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File fromFile = new File("./FileChannelCopySource.txt");
        File toFile = new File("./FileMmapTest244444444.txt");
        System.out.println("呵呵开始吧");
        //预热
        fileCopyNormal(fromFile, toFile);
        fileCopyWithFileChannel(fromFile, toFile);
        System.out.println("呵呵打");
        //计时
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            fileCopyNormal(fromFile, toFile);
        }
        System.out.println("fileCopyNormal time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            fileCopyWithFileChannel(fromFile, toFile);
        }
        System.out.println("fileCopyWithFileChannel time: " + (System.currentTimeMillis() - start));

        Thread.sleep(5000);
        System.out.println("结束");



    }

}
