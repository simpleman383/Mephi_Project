package com.example.simpleman383.shell;

import java.util.ArrayList;
import java.util.List;

public class CommandOutputBuilder implements CommandOutput {

    private List<String> standartOutputBuffer;
    private List<String> standartErrorBuffer;

    public CommandOutputBuilder() {
        standartOutputBuffer = new ArrayList<>();
        standartErrorBuffer = new ArrayList<>();
    }

    public void appendOutputLine(String line) {
        standartOutputBuffer.add(line);
    }

    public void appendErrorLine(String line) {
        standartErrorBuffer.add(line);
    }

    @Override
    public String getStandartErrorLine() {
        StringBuilder builder = new StringBuilder();

        for (String line: standartErrorBuffer) {
            builder.append(line);
        }

        return builder.toString();
    }

    @Override
    public String getStandartOutputLine() {
        StringBuilder builder = new StringBuilder();

        for (String line: standartOutputBuffer) {
            builder.append(line);
        }

        return builder.toString();
    }

    @Override
    public String[] getStandartOutputLines() {

        return standartOutputBuffer.toArray(new String[standartOutputBuffer.size()]);
    }

    @Override
    public String[] getStandartErrorLines() {
        return standartErrorBuffer.toArray(new String[standartErrorBuffer.size()]);
    }
}
