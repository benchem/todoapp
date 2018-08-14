package team.benchem.todoapp.util;

public class Global {

    private static Global mInstance;

    private Global(){

    }

    public static Global getInstance(){
        if(mInstance == null){
            mInstance = new Global();
        }
        return mInstance;
    }

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
