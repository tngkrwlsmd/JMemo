package com.example.jmemo;

import androidx.annotation.NonNull;

public class Memo {
    private int id;
    private String title;
    private String content;
    private int folderId;

    public Memo(String title, String content, int folderId) {
        this.title = title;
        this.content = content;
        this.folderId = folderId;
    }
    public Memo(int id, String title, String content, int folderId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.folderId = folderId;
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

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}
