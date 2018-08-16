package team.benchem.todoapp.communication.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Ice.Current;
import team.benchem.communication._JsonServicePortalDisp;
import team.benchem.todoapp.communication.RequestBody;
import team.benchem.todoapp.communication.ResponseBody;
import team.benchem.todoapp.lang.SystemStateCode;
import team.benchem.todoapp.service.ServicePortal;

/**
 * @ClassName JsonServicePortalImpl
 * @Deseription TODO
 * @Author chenjiabin
 * @Date 2018-08-16 9:45
 * @Version 1.0
 **/
public class JsonServicePortalImpl extends _JsonServicePortalDisp implements ServicePortal {

    private final static Map<String, ServicePortal.MessageHandler> messageHandlers = new HashMap<>();

    @Override
    public void registerHandler(String handlerKey, MessageHandler handler) {
        messageHandlers.put(handlerKey, handler);
    }

    @Override
    public void unRegisterHandler(String handlerKey) {
        if(messageHandlers.containsKey(handlerKey)){
            messageHandlers.remove(handlerKey);
        }
    }

    @Override
    public String invoke(String requestBody, Current current) {

        RequestBody request = JSON.parseObject(requestBody).toJavaObject(RequestBody.class);
        if(!messageHandlers.containsKey(request.getHandlerName())){
            ResponseBody response = new ResponseBody(SystemStateCode.HANDLER_NOT_REGISTER);
            return JSON.toJSONString(response);
        }

        try{
            JSONObject resBody = messageHandlers.get(request.getHandlerName())
                    .onMessageReceive(request.getMessageBody());
            ResponseBody response = new ResponseBody(resBody);
            return JSON.toJSONString(response);
        }catch (Exception ex){
            ResponseBody response = new ResponseBody(SystemStateCode.SYSTEM_ERROR);
            response.setMessage(ex.getMessage());
            return JSON.toJSONString(response);
        }
    }

}
