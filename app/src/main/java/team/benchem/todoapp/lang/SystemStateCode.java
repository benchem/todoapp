package team.benchem.todoapp.lang;

public enum  SystemStateCode implements  StateCode {
    OK                          (0,     ""),
    SYSTEM_ERROR                (-1,    "系统繁忙，请稍候再试"),
    AUTH_ERROR                  (-2,    "无权访问"),
    Invalid_Token               (-100,  "无效客户端令牌"),
    UNKNOW_REMOTE_REQUESTTYPE   (-101,  "未知远程调用类型"),
    APIGATEWAY_NOTRESPONSE      (-102,  "API网关访问超时"),
    SERVICECENTER_NOTRESPONSE   (-103,  "微服务中心访问超时"),
    REMOTECALLING               (-104,  "远程调用中"),
    ;

    private final Integer stateCode;
    private final String message;

    SystemStateCode(Integer stateCode, String message){
        this.stateCode = stateCode;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return stateCode;
    }

    @Override
    public String getCodeName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return message;
    }

}