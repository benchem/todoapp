package team.benchem.todoapp.service;

import org.json.JSONArray;

import team.benchem.todoapp.annotation.RemoteMethodProxy;

public class TodoNoteService {

    @RemoteMethodProxy(serviceKey = "sufuserservice", path = "/sufuser/list")
    public JSONArray queryList(Integer page, Integer site){
        throw new NullPointerException();
    }

}
