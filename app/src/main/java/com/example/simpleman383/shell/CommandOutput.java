package com.example.simpleman383.shell;

public interface CommandOutput {
    String getStandartOutputLine();
    String[] getStandartOutputLines();

    String getStandartErrorLine();
    String[] getStandartErrorLines();
}