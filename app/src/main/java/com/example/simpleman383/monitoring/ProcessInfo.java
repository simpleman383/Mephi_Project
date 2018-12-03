package com.example.simpleman383.monitoring;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@interface PSCommandLabel {
    String value();
}

public class ProcessInfo {

    @PSCommandLabel("PID")
    public int pid;

    @PSCommandLabel("PPID")
    public int parentPid;

    @PSCommandLabel("USER")
    public String user;

    @PSCommandLabel("NAME")
    public String name;

    public ProcessInfo() {}

    public ProcessInfo(int pid, int ppid, String user, String name) {
        this.pid = pid;
        this.parentPid = ppid;
        this.name = name;
        this.user = user;
    }


    public static ProcessInfo fromPSCommandOutput(String line, String headerLine) {

        if (headerLine == null || headerLine.isEmpty()) {
            return null;
        }

        if (line == null || line.isEmpty()) {
            return null;
        }

        String[] headerKeys = splitAndClear(headerLine);
        String[] chunks = splitAndClear(line);

        try {
            ProcessInfo instance = ProcessInfo.class.newInstance();

            for (Field field : instance.getClass().getDeclaredFields()) {
                PSCommandLabel label = field.getAnnotation(PSCommandLabel.class);

                if (label != null) {
                    String labelValue = label.value();

                    int position = getKeyPosition(labelValue, headerKeys);
                    String value = position != -1 ? chunks[position] : "";

                    if (field.getType() == int.class) {
                        int v = value.isEmpty() ? 0 : Integer.parseInt(value);
                        field.setInt(instance, v);
                    }

                    if (field.getType() == String.class) {
                        field.set(instance, value);
                    }
                }

            }

            return instance;
        }
        catch (IllegalAccessException ex) {
            return null;
        }
        catch (InstantiationException ex){
            return null;
        }
    }

    private static int getKeyPosition(String key, String[] keys) {
        if (key != null && keys != null) {
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != null && keys[i].equalsIgnoreCase(key)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static String[] splitAndClear(String data) {
        final String divider = "[\\s\\t]{1,}";
        String[] chunks = data.split(divider);

        List<String> filtered = new ArrayList<>();
        for (String s : chunks) {
            if (s != null && !s.isEmpty()) {
                filtered.add(s);
            }
        }
        return filtered.toArray(new String[filtered.size()]);
    }
}
