package team.benchem.todoapp.service;

import com.alibaba.fastjson.JSONArray;

import team.benchem.todoapp.annotation.RemoteMethodProxy;

public class SufCustomerService {

    @RemoteMethodProxy(serviceKey = "sufcustomerservice", path = "/customer/finddomainbymobile")
    public JSONArray queryDomain(String mobile){
        throw new NullPointerException();
    }

}
