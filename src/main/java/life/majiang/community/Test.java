package life.majiang.community;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by 叶志伟 on 2020/8/20.
 */
public class Test {
    public static void main(String[] args) {

        String json = "{\"acc\":\"efg\",\"avb\":\"abc\"}";
        System.out.println(json);
        JSON jsonObject = JSON.parseObject(json);
        System.out.println(jsonObject);
        String json2 = "{\"acc\":\"efg\",\"avb\":\"abc\"}";
        System.out.println(json.equals(jsonObject));
        System.out.println(json.equals(json2));

    }
}
