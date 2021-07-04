package com.xzll.test.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/27 14:54
 * @Description: 获取进程信息
 */
public class GetPID {

    @Test
    public void fetPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        System.out.println("RuntimeMXBean: +" + JSON.toJSONString(runtime));
        String name = runtime.getName(); // format: "pid@hostname"
        Integer pid = null;
        try {
            pid = Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            pid = -1;
        }
        System.out.println("当前进程的ID为: " + pid);
    }


}
