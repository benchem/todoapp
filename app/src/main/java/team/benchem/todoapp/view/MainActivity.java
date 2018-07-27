package team.benchem.todoapp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team.benchem.todoapp.MainActivityBinding;
import team.benchem.todoapp.R;
import team.benchem.todoapp.model.TodoModel;

/**
 * https://www.androidhive.info/android-databinding-in-recyclerview-profile-screen/
 */
public class MainActivity extends AppCompatActivity implements TodosAdapter.TodosAdapterListener {

    MainActivityBinding binding;
    TodosAdapter todosAdapter;
    RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    final int CREATE_TODO_REQUESTCODE = 100;
    final int UPDATE_TODO_REQUESTCODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.module_activity_main);

        todosAdapter = new TodosAdapter(getTods(), this);
        recyclerView = binding.moduleActivityMainRecyclerView;
        recyclerView.setAdapter(todosAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private List<TodoModel> getTods(){
        //remove call load todoItems
        TodoModel item = new TodoModel();
        item.setTodo("Hello World!");
        List<TodoModel> ds = new ArrayList<>();
        ds.add(item);
        return ds;
    }

    public void onAddItem(View view){
        Intent intent = new Intent(this, TodoDetailActivity.class);
        startActivityForResult(intent, CREATE_TODO_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == CREATE_TODO_REQUESTCODE) {
            String modelJson = data.getExtras().getString("todoItem");
            TodoModel item = JSONObject.parseObject(modelJson).toJavaObject(TodoModel.class);
            todosAdapter.addTodoItem(item);
            Toast.makeText(this, R.string.module_activity_main_addSuccess, Toast.LENGTH_SHORT).show();
        } else if (requestCode == UPDATE_TODO_REQUESTCODE) {
            String modelJson = data.getExtras().getString("todoItem");
            TodoModel item = JSONObject.parseObject(modelJson).toJavaObject(TodoModel.class);
            todosAdapter.updateTodoItem(item);
            Toast.makeText(this, R.string.module_activity_main_updateSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedItem(TodoModel todo, Boolean isCompleted) {
        Log.d(TAG, String.format("onCheckedItem: %s -> %s", todo.getTodo(), isCompleted));
        todosAdapter.updateTodoItem(todo);
    }

    @Override
    public void onUpdateItem(TodoModel todo) {
        Intent intent = new Intent(this, TodoDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("todoItem", JSON.toJSONString(todo));
        intent.putExtras(bundle);
        startActivityForResult(intent, UPDATE_TODO_REQUESTCODE);
    }

    @Override
    public void onDeleteItem(final TodoModel todo) {
        Log.d(TAG, String.format("onDeleteItem: %s", todo.getTodo()));

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alertDialog_tips)
                .setMessage(String.format(getText(R.string.module_activity_main_removeMessage).toString(), todo.getTodo()))
                .setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //remove call delete item
                        todosAdapter.removeTodoItem(todo.getId());
                        Toast.makeText(MainActivity.this, R.string.module_activity_main_removeSuccess, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //do nothing
                    }
                })
                .create();
        alertDialog.show();
    }
}
