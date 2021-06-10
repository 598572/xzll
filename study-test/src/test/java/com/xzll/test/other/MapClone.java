package com.xzll.test.other;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/8 20:10
 * @Description:
 */
public class MapClone {
    public static void main(String[] args) {


//        HashMap<Integer, String> sites = new HashMap<>();
//        sites.put(1, "Google");
//        sites.put(2, "Runoob");
//        sites.put(3, "Taobao");
//        System.out.println("HashMap: " + sites);
//
//        // 复制 sites
//        HashMap<Integer, String> cloneSites = (HashMap<Integer, String>)sites.clone();
//        System.out.println("Cloned HashMap: " + cloneSites);


        List<Map<String,String>> objects = Lists.newArrayList();
        Map<String, String> map = new HashMap<>();
        objects.add(map);

        System.out.println(objects);

        map.put("k1","v1");
        map.put("k2","v2");
        map.put("k3","v3");

        System.out.println(objects);


    }
}
