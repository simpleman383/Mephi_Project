package com.example.simpleman383.tasking;

public class SimpleTask extends BaseTask {

    public SimpleTask(IRunnableTaskUnit... units) {
        super(units);
    }

    protected void execute() {
        runCommandSequence();
    }
}