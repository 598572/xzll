package com.xzll.test.other;

import com.xzll.common.util.DeflaterUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/7 18:36
 * @Description:
 */
@Slf4j
public class StrZipUnZip {


    @Test
    public void test() throws IOException {
        String str = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append(UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString());
        }
        str = sb.toString();
        //使用GZIPOutputStream
        long l = System.currentTimeMillis();
        log.info("-------------使用 GZIP算法测试 开始-------------");
        String zipString = DeflaterUtil.compress(str);
        log.info("原长度:{},压缩后长度:{}", str.length(), zipString.length());
        String result = DeflaterUtil.uncompress(zipString);
        log.info("解压后长度:{}", result.length());
        log.info("原长度与解压后长度一样吗:{}", str.length() == result.length());
        DecimalFormat df = new DecimalFormat("0.00");
        log.info("-------------使用 GZIP算法测试 结束 用时:{} ms------------- 压缩率:{} ", System.currentTimeMillis() - l, df.format((float) zipString.length() / str.length()));

        System.out.println();

        //使用 Deflater算法 似乎更优些呢
        long l2 = System.currentTimeMillis();
        log.info("-------------使用 Deflater算法测试 开始-------------");
        String zipStringWithDeflater = DeflaterUtil.zipString(str);
        log.info("原长度:{},压缩后长度:{}", str.length(), zipStringWithDeflater.length());
        String resultWithDeflater = DeflaterUtil.unzipString(zipStringWithDeflater);
        log.info("解压后长度:{}", resultWithDeflater.length());
        log.info("原长度与解压后长度一样吗:{}", str.length() == resultWithDeflater.length());
        log.info("-------------使用 Deflater算法测试 结束 用时:{} ms------------- 压缩率:{} ", System.currentTimeMillis() - l2, df.format((float) zipStringWithDeflater.length() / str.length()));

    }

    /**
     * rocketmq 压缩消息的实现 在UtilAll类中
     *
     * @param src
     * @param level
     * @return
     * @throws IOException
     */
    public static byte[] compress(final byte[] src, final int level) throws IOException {
        byte[] result = src;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(src.length);
        java.util.zip.Deflater defeater = new java.util.zip.Deflater(level);
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, defeater);
        try {
            deflaterOutputStream.write(src);
            deflaterOutputStream.finish();
            deflaterOutputStream.close();
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            defeater.end();
            throw e;
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignored) {
            }

            defeater.end();
        }

        return result;
    }

    /**
     * rocketMQ解压缩实现
     *
     * @param src
     * @return
     * @throws IOException
     */
    public static byte[] uncompress(final byte[] src) throws IOException {
        byte[] result = src;
        byte[] uncompressData = new byte[src.length];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(src);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(src.length);

        try {
            while (true) {
                int len = inflaterInputStream.read(uncompressData, 0, uncompressData.length);
                if (len <= 0) {
                    break;
                }
                byteArrayOutputStream.write(uncompressData, 0, len);
            }
            byteArrayOutputStream.flush();
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                log.error("Failed to close the stream", e);
            }
            try {
                inflaterInputStream.close();
            } catch (IOException e) {
                log.error("Failed to close the stream", e);
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                log.error("Failed to close the stream", e);
            }
        }

        return result;
    }


}
