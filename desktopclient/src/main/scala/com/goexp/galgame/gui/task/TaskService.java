package com.goexp.galgame.gui.task;

import javafx.beans.value.WeakChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Objects;
import java.util.function.Supplier;


public class TaskService<V> extends Service<V> {

    private final Supplier<Task<V>> taskSupplier;

    public TaskService(Supplier<Task<V>> taskSupplier) {
        Objects.requireNonNull(taskSupplier);
        this.taskSupplier = taskSupplier;

        this.exceptionProperty().addListener(new WeakChangeListener<>((observable, oldValue, newValue) -> {
            if (newValue != null)
                newValue.printStackTrace();
        }));

    }

    @Override
    public Task<V> createTask() {
        return taskSupplier.get();
    }


}
