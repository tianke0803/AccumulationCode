package com.tian.database;

import java.util.HashMap;
import java.util.Map;

/**
 * 对map中的值进行覆盖
 * @author tianke
 */
public class CoverMapKey {
    public static void main(String [] args){
        final Map<String,Object> map=new HashMap<String, Object>(){{
            put("1","a");
            put("2","b");
            put("3","c");
        }};
        final Map<String,Object> coverMap=new HashMap<String,Object>(){{
            put("1","张三");
            put("test2","李四");
            put("test3","王五");
        }};
        Map<String,Object> resultMap=new HashMap<String, Object>(){{
           putAll(map);
           putAll(coverMap);
        }};
        System.out.println(resultMap);
    }
}
