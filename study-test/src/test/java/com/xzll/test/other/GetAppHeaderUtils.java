package com.xzll.test.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/28 17:12
 * @Description:
 */
public class GetAppHeaderUtils {

    /**
     * 获取header数据
     *
     * @param args
     */
    public static void main(String[] args) {

        String str = "";

        JSONObject jsonObject = JSON.parseObject(str);
        List<String> token = Stream.of("token").collect(Collectors.toList());
        jsonObject.entrySet().stream().forEach((x) -> {
            String value = x.getValue().toString();
            value = value.replace("[", "").replace("]", "").replace("\"", "");
            if (token.contains(x.getKey())) {
                System.out.println(x.getKey() + ":" + value);
            }
        });

    }
}
