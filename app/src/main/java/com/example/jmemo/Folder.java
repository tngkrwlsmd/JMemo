package com.example.jmemo;

import androidx.annotation.NonNull;

public class Folder {
    private int id;
    private final String name;
    private int order;

    public Folder(String name) {
        this.name = name;
    }

    public Folder(int id, String name, int order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getOrder() { return order; }

    public void setOrder(int order) { this.order = order; }

    @NonNull
    @Override
    public String toString() { return name; }
}

