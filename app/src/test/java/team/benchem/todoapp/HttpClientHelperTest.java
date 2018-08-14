package team.benchem.todoapp;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;

import team.benchem.todoapp.util.HttpClientHelper;

@RunWith(JUnit4.class)
public class HttpClientHelperTest {

    @Test
    public void findlistTest(){

        HashMap<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 25);

        String resultStr = HttpClientHelper.get(
                "http://yapi.lonntec.cn",
                "/mock/87/note/list",
                null,
                params
        );
        JSONObject result = JSON.parseObject(resultStr);
        Assert.assertEquals(0, result.getInteger("statecode").intValue());
    }

}
