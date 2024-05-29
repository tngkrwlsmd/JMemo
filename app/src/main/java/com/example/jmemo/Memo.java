package com.example.jmemo;

import androidx.annotation.NonNull;

public class Memo {
    private int id;
    private String title;
    private String content;
    private int folderId;
    private int order;

    public Memo(String title, String content, int folderId, int order) {
        this.title = title;
        this.content = content;
        this.folderId = folderId;
        this.order = order;
    }

    public Memo(int id, String title, String content, int folderId, int order) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.folderId = folderId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFolderId() { return folderId; }

    public void setFolderId(int folderId) { this.folderId = folderId; }

    public int getOrder() { return order; }

    public void setOrder(int order) { this.order = order; }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}
