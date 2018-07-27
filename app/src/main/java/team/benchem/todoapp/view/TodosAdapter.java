package team.benchem.todoapp.view;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import team.benchem.todoapp.R;
import team.benchem.todoapp.TodoRowItemBinding;
import team.benchem.todoapp.model.TodoModel;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.MyViewHolder> {

    private final List<TodoModel> todoList;
    private LayoutInflater layoutInflater;
    private final TodosAdapterListener listener;

    public TodosAdapter(List<TodoModel> todoList, TodosAdapterListener listener) {
        this.todoList = todoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TodoRowItemBinding binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.module_recycle_item_todoitem,
                parent,
                false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final TodoModel bindingModel = todoList.get(position);
        holder.binding.setTodo(bindingModel);
        holder.binding.moduleRecycleItemTodoItemTvTodo.getPaint().setFlags(
                bindingModel.getCompleted() ? Paint.STRIKE_THRU_TEXT_FLAG : Paint.HINTING_OFF
        );
        holder.binding.moduleRecycleItemTodoItemCbComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    CheckBox checkBox = (CheckBox) view;
                    listener.onCheckedItem(bindingModel, checkBox.isChecked());
                }
            }
        });
        holder.binding.moduleRecycleItemTodoItemBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onUpdateItem(bindingModel);
                }
            }
        });
        holder.binding.moduleRecycleItemTodoItemBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteItem(bindingModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList == null ? 0 : todoList.size();
    }

    public void addTodoItem(TodoModel item){
        todoList.add(item);
        notifyItemInserted(todoList.size()-1);
    }

    public void updateTodoItem(TodoModel item){
        for (int i = 0; i < todoList.size(); i++) {
            TodoModel localItem= todoList.get(i);
            if(item.getId().equalsIgnoreCase(localItem.getId())){
                localItem.setTodo(item.getTodo());
                localItem.setCompleted(item.getCompleted());
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeTodoItem(String id){
        for (int i = 0; i < todoList.size(); i++) {
            TodoModel item = todoList.get(i);
            if(id.equalsIgnoreCase(item.getId())){
                todoList.remove(item);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TodoRowItemBinding binding;

        public MyViewHolder(@NonNull final TodoRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface TodosAdapterListener {

        void onCheckedItem(TodoModel todo, Boolean isCompleted);

        void onUpdateItem(TodoModel todo);

        void onDeleteItem(TodoModel todo);
    }

}
