package com.xzll.test.niotest.三种IO代码实现;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 08:48
 * @Description: 传统IO拷贝文件演示
 */
public class NormalIOTest extends IOCommonTest {

    public static final String NORMAL_TARGET = "/Users/admin/hzz_main/xzll/传统IO拷贝目标文件.txt";
    private static String NORMAL_SOURCE = "/Users/admin/hzz_main/xzll/传统IO源文件.txt";


    public void createFileNotExist() {
        Path source = Paths.get(NORMAL_SOURCE);
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
     * 传统IO写入造的数据到文件
     *
     * @throws IOException
     */
    public void writeTest() {
        long l1 = System.currentTimeMillis();
        Path path = Paths.get(NORMAL_SOURCE);
        byte[] bytes = CONTENT.getBytes();
        System.out.println("normal  writeTest 获取文件用时 :" + (System.currentTimeMillis() - l1) + "  ms  不算入性能之内");
        long l = System.currentTimeMillis();
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("NormalIOWrite 写入数据用时: "
                + (System.currentTimeMillis() - l) + " ms " + "文件大小 " + path.toFile().length() + " byte " +
                "写入进文件---> " + NORMAL_SOURCE
        );

    }

    /**
     * 测试传统IO 拷贝文件 用时
     *
     * @throws FileNotFoundException
     */
    //@Test
    public void testCopy() throws Exception {
        File fromFile = new File(NORMAL_SOURCE);
        File toFile = null;
        try {
            toFile = new File(NORMAL_TARGET);
            if (!toFile.getParentFile().exists()) {
                boolean result = toFile.getParentFile().mkdirs();
                if (!result) {
                    System.out.println("创建失败目标文件失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long l = System.currentTimeMillis();
        System.out.println("拷贝前: 文件大小 :" + toFile.length() );
        fileCopyNormal(fromFile, toFile);
        System.out.println("传统IO 拷贝用时: " + (System.currentTimeMillis() - l) + " ms" + " 文件大小:" + FileUtils.sizeOf(toFile) + " byte ");
    }

    /**
     * 传统IO拷贝文件
     *
     * @param fromFile
     * @param toFile
     * @throws FileNotFoundException
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
}
