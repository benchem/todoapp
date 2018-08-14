package team.benchem.todoapp.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import team.benchem.todoapp.LoginActivityBinding;
import team.benchem.todoapp.R;
import team.benchem.todoapp.service.SufCustomerService;
import team.benchem.todoapp.service.TodoNoteService;
import team.benchem.todoapp.util.Ajax;
import team.benchem.todoapp.util.Global;

public class LoginActivity extends Activity {

    LoginActivityBinding binding;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String loginName = Global.getInstance().getUserName();
        if(loginName != null && loginName.length() > 0){
            startMainActivity();
            return;
        }

        setContentView(R.layout.module_activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.module_activity_login);
        binding.setLoginName(loginName);
        binding.setPassword("");
    }

    public void onLoginClick(View view){
        String loginName = binding.getLoginName();
        String password = binding.getPassword();
        Global.getInstance().setUserName(loginName);
        startMainActivity();
    }

    private void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRegisterClick(View view) throws MalformedURLException {

//        try {
////            TodoNoteService service = new TodoNoteService();
////            service.queryList(10, 25);
//
//            SufCustomerService customerService = new SufCustomerService();
//            JSONArray list = customerService.queryDomain("13750378429");
//            for(int index=0; index<list.size(); index++){
//                Log.d(TAG, list.get(index).toString());
//            }
//
//        }catch (Exception ex){
//            Log.e(TAG, "onRegisterClick: "+ex.getMessage());
//            ex.printStackTrace();
//        }


        Ajax.setBaseHost("https://ds-suf-uat.xunmu.wang");

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", "13750378429");

        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();

        Ajax.get("/customer/finddomainbymobile",params)
                .subscribe(new Ajax.ResponseHandler() {
                    @Override
                    public void onResponse(String responseStr) {
                        JSONObject result = JSON.parseObject(responseStr);
                        if(result.getInteger("statecode") == 0){
                            JSONArray list = result.getJSONArray("result");
                            for(int index=0; index<list.size(); index++) {
                                Log.d(TAG, list.get(index).toString());
                            }
                        }
                        hud.dismiss();
                    }

                    @Override
                    public void onError(Exception ex) {
                        hud.dismiss();
                    }
                });

    }
}
