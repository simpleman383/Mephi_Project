package com.example.simpleman383.tasking;

public class CancellationToken {
    boolean state = false;

    public boolean isCancellationRequested() {
        return state;
    }

    public void cancel() {
        state = true;
    }
}
