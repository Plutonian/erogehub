package com.goexp.galgame.gui.util;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Objects;
import java.util.function.Supplier;


public class TaskService<V> extends Service<V> {

    private final Supplier<Task<V>> taskSupplier;

    public TaskService(Supplier<Task<V>> taskSupplier) {
        Objects.requireNonNull(taskSupplier);
        this.taskSupplier = taskSupplier;
    }

    @Override
    public Task<V> createTask() {
        return taskSupplier.get();
    }


}
