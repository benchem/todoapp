package team.benchem.todoapp.service.impl;

import com.alibaba.fastjson.JSONObject;

import team.benchem.todoapp.service.ServicePortal;

public class TodoMessageHandler implements ServicePortal.MessageHandler {

    @Override
    public JSONObject onMessageReceive(JSONObject messageBody) {
        return null;
    }
}
