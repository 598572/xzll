package com.xzll.test.other;

import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Test;

import java.io.IOException;

/**
 * @Auther: Huangzhuangzhuang
 * @Date:
 * @Description: 使用 org.apache.commons.validator.routines.UrlValidator 对url进行校验
 */
public class CheckUrl {
    /**
     * 检测是否是url
     *
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        String params = "http://www.baidu.com/";
        //支持的协议
        String[] sc = new String[]{"http", "https", "ftp", "xxxxx"};
        UrlValidator urlValidator = new UrlValidator(sc);
        //必须有=号和？号（这里有点粗犷了）不过校验url是没问题的
        if (!(urlValidator.isValid(params) && params.contains("?") && params.contains("="))) {
            System.out.println("无效");
        } else {
            System.out.println("有效");
        }
    }

}
