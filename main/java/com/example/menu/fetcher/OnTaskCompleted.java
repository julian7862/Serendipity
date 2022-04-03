package com.example.menu.fetcher;

public interface OnTaskCompleted<s extends Boolean> {
    void onTaskCompleted(Boolean s);
}
