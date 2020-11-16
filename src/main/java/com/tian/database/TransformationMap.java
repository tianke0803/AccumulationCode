package com.tian.database;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 常见的集合类之间的转换
 * map list  json
 * 采用alibaba falstjson
 *@tianke12
 */
public class TransformationMap {
    public  static void main(String[] args) {
        /**
         *Map 转 json
         */
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("a", "a");
            put("b", "b");
        }};
        JSONObject json=new JSONObject(map);
        /**
         * map转string
         */
        String s=JSONObject.toJSONString(map);
        /**
         * json 转map
         */
        Map<String,Object> jsonMap=(Map<String,Object>)json;
        /**
         * string 转json
         */
        JSONObject jsonObject=JSONObject.parseObject(s);
    }

}
