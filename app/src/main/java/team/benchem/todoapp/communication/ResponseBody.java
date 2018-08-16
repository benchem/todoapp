package team.benchem.todoapp.communication;

import com.alibaba.fastjson.annotation.JSONField;
import team.benchem.todoapp.lang.StateCode;
import team.benchem.todoapp.lang.SystemStateCode;

/**
 * @ClassName ResponseBody
 * @Deseription TODO
 * @Author chenjiabin
 * @Date 2018-08-16 9:26
 * @Version 1.0
 **/
public class ResponseBody {

    Integer stateCode;
    String stateCodeName;
    @JSONField(name = "msg", ordinal = 2)
    String message;
    Object result;

    private ResponseBody(){

    }

    public ResponseBody(StateCode stateCode){
        this.stateCode = stateCode.getCode();
        this.stateCodeName = stateCode.getCodeName();
        this.message = stateCode.getMessage();
    }

    public ResponseBody(Integer stateCode, String message) {
        this.stateCode = stateCode;
        this.message = message;
    }

    public ResponseBody(Object result) {
        this.stateCode = SystemStateCode.OK.getCode();
        this.stateCodeName = SystemStateCode.OK.getCodeName();
        this.message = SystemStateCode.OK.getMessage();
        this.result = result;
    }

    @JSONField(name = "statecode", ordinal = 0)
    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    @JSONField(name = "statename", ordinal = 1)
    public String getStateCodeName() {
        return stateCodeName;
    }

    public void setStateCodeName(String stateCodeName) {
        this.stateCodeName = stateCodeName;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JSONField(name = "result", ordinal = 3)
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
