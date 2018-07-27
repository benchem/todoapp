package team.benchem.todoapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.UUID;

import team.benchem.todoapp.BR;

public class TodoModel extends BaseObservable {

    private String id;
    private Boolean completed;
    private String todo;

    public TodoModel() {
        this.id = UUID.randomUUID().toString();
        completed = false;
        todo = "";
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
        notifyPropertyChanged(BR.completed);
    }

    @Bindable
    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
        notifyPropertyChanged(BR.todo);
    }
}
