package com.goexp.galgame.data.piplline.core;

public class Message<T> {

    public int code;

    public T entity;

    public Message() {
    }

    public Message(int code, T entity) {
        this.code = code;
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "Message{" +
                "code=" + code +
                '}';
    }
}
