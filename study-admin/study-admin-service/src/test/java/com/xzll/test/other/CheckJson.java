package com.xzll.test.other;

import cn.hutool.json.JSONUtil;
import com.xzll.test.StudyTestApplication;
import com.xzll.test.StudyTestApplicationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/10 10:54
 * @Description:
 */
public class CheckJson {
    /**
     * 检测是否是json 使用hutool工具类
     *
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        String str = "{}";

        if (JSONUtil.isJson(str)) {
            System.out.println();
        }
        System.out.println(JSONUtil.isJson(str));
    }

    @Test
    public void test2() {
        Integer int2 = 1073741824;
        int i = int2 * 2;
        System.out.println(i);
    }


}
