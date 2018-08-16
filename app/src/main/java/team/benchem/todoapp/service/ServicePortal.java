package team.benchem.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName ServicePortal
 * @Deseription TODO
 * @Author chenjiabin
 * @Date 2018-08-16 11:55
 * @Version 1.0
 **/
public interface ServicePortal {

    void registerHandler(String handlerKey, MessageHandler handler);

    void unRegisterHandler(String handlerKey);

    interface MessageHandler{
        JSONObject onMessageReceivce(JSONObject messageBody);
    }
}
