package com.example.jmemo;

import androidx.annotation.NonNull;

public class Folder {
    private int id;
    private String name;

    public Folder(String name) {
        this.name = name;
    }

    public Folder(int id, String name) {
        this.id = id;
        this.name = name;
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

    @NonNull
    @Override
    public String toString() { return name; }
}

