package team.benchem.todoapp.communication;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @ClassName RequestBody
 * @Deseription TODO
 * @Author chenjiabin
 * @Date 2018-08-16 9:26
 * @Version 1.0
 **/
public class RequestBody {

    @JSONField(name="handler")
    String handlerName;

    @JSONField(name="message")
    JSONObject messageBody;

    public RequestBody(String handlerName, JSONObject messageBody) {
        this.handlerName = handlerName;
        this.messageBody = messageBody;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public JSONObject getMessageBody() {
        return messageBody;
    }
}
