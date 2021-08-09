package com.xzll.test.other;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 07:51
 * @Description: 对字符串的截取 等操作
 */
public class StringDemo {
    /**
     * String相关操作根据需要进行编码
     */
    @Test
    public void strOperate() {
        String str = "https://baidu.com/github?activityId=490";
        //根据需要截取就完事 还有URI工具类啊等等的都可以干这个事情 多的很那
//        int index = StringUtils.ordinalIndexOf(str, "=", 1);//返回第(ordinal)几个相等的searchStr的位置(index)
//        System.out.println("url: " + str.substring(0, index));
//        String substring = str.substring(index + 1, str.length());
//        System.out.println("first param : " + substring);

        if (str.indexOf("?") > 0) {
            String landingPage = str.substring(0, str.indexOf("?"));
            System.out.println(landingPage);
        }
    }
}
