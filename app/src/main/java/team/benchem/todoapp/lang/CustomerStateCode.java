package team.benchem.todoapp.lang;

public class CustomerStateCode implements StateCode {

    Integer code;

    String codeName;

    String message;

    public CustomerStateCode(Integer code, String message) {
        this.code = code;
        this.codeName = "";
        this.message = message;
    }

    public CustomerStateCode(Integer code, String codeName, String message) {
        this.code = code;
        this.codeName = codeName;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getCodeName() {
        return codeName;
    }

    @Override
    public String getMessage() {
        return message;
    }

}