package team.benchem.todoapp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import team.benchem.todoapp.R;
import team.benchem.todoapp.TodoDetailActivityBinding;
import team.benchem.todoapp.model.TodoModel;

public class TodoDetailActivity extends Activity {

    TodoDetailActivityBinding binding;
    TodoModel currItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_tododetail);
        binding = DataBindingUtil.setContentView(this, R.layout.module_activity_tododetail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String itemJson = bundle != null ? bundle.getString("todoItem") : "";
        if(itemJson == null || itemJson.length() == 0){
            currItem = new TodoModel();
        } else {
            currItem = JSON.parseObject(itemJson).toJavaObject(TodoModel.class);
        }
        binding.setViewModel(currItem);
    }

    public void onConfirmClick(View view) {
        if (currItem.getTodo() == null ||
                currItem.getTodo().length() == 0 ||
                currItem.getTodo().trim().length() == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.alertDialog_tips)
                    .setMessage(R.string.module_activity_tododetail_titleIsEmpty)
                    .setNeutralButton(R.string.button_confirm, null)
                    .create();
            alertDialog.show();
            return;
        }

        Intent result = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("todoItem", JSONObject.toJSONString(currItem));
        result.putExtras(bundle);
        setResult(RESULT_OK, result);
        finish();
    }

    public void onCancelClick(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
