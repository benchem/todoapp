package team.benchem.todoapp.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import team.benchem.todoapp.LoginActivityBinding;
import team.benchem.todoapp.R;
import team.benchem.todoapp.util.Global;

public class LoginActivity extends Activity {

    LoginActivityBinding binding;

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

    public void onRegisterClick(View view){

    }
}
