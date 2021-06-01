package com.xzll.test.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/1 19:07
 * @Description: 今天遇到个需求 对字符串压缩并且需要解压缩回来 在这里演示几种压缩方式比较
 */
@Slf4j
public class DeflaterUtil {

    /**
     * 压缩
     *
     * @param unzipString
     * @return
     */
    public static String zipString(String unzipString) {
        if (StringUtils.isEmpty(unzipString)) {
            return StringUtils.EMPTY;
        }
        log.info("zipString 压缩前字符串长度:{},内容:{}", unzipString.length(), unzipString);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
        try {
            compressor.setInput(unzipString.getBytes());
            compressor.finish();
            final byte[] buf = new byte[1024];
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                outputStream.write(buf, 0, count);
            }
        } catch (Exception e) {
            log.error("zipString exception:{}", e.getMessage());
            e.printStackTrace();
            return StringUtils.EMPTY;
        } finally {
            compressor.end();
        }
        String zipString = new String(Base64.encodeBase64(outputStream.toByteArray()));
        log.info("zipString 压缩后字符串长度:{},内容:{}", zipString.length(), zipString);
        return zipString;
    }

    /**
     * 解压
     *
     * @param zipString
     * @return
     */
    public static String unzipString(String zipString) {
        if (StringUtils.isEmpty(zipString)) {
            return StringUtils.EMPTY;
        }

        log.info("unzipString 解压前字符串长度:{},内容:{}", zipString.length(), zipString);
        byte[] decode = Base64.decodeBase64(zipString);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(decode);
            final byte[] buf = new byte[1024];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                outputStream.write(buf, 0, count);
            }
        } catch (Exception e) {
            log.error("unzipString exception:{}", e.getMessage());
            e.printStackTrace();
            return StringUtils.EMPTY;
        } finally {
            decompressor.end();
        }
        String unzipString = outputStream.toString();
        log.info("unzipString 解压后字符串长度:{},内容:{}", unzipString.length(), unzipString);
        return unzipString;
    }


    /**
     * 压缩 gzip方式
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = new String(org.apache.commons.codec.binary.Base64.encodeBase64(out.toByteArray()));
        return outStr;
    }

    /**
     * 解压缩 gzip方式
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String uncompress(String str) throws IOException {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(str)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(gis, out);
        return new String(out.toByteArray());
    }

    public static void main(String[] args) throws Exception {

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


}
