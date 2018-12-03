package com.example.simpleman383.monitoring;

import java.util.ArrayList;
import java.util.List;

public class PsCommandParser {

    String[] config;

    public ProcessInfo[] parse(String[] lines) {
        List<ProcessInfo> processInfoList = new ArrayList<>();

        if (lines == null || lines != null && lines.length <= 1) {
            return new ProcessInfo[0];
        }

        config = splitAndClear(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            ProcessInfo info = getInfoFromLine(lines[i]);
            if (info != null) {
                processInfoList.add(info);
            }
        }

        return processInfoList.toArray(new ProcessInfo[processInfoList.size()]);
    }

    private ProcessInfo getInfoFromLine(String line) {
        String sPid = fetchDataFromLine(line, "pid");
        int pid = sPid.isEmpty() ?  0 : Integer.parseInt(sPid);

        String sPpid = fetchDataFromLine(line, "ppid");
        int ppid = sPpid.isEmpty() ? 0 : Integer.parseInt(sPpid);

        String user = fetchDataFromLine(line, "user");
        String name = fetchDataFromLine(line, "name");

        return new ProcessInfo(pid, ppid, user, name);
    }

    private String fetchDataFromLine(String line, String key) {
        String[] chunks = splitAndClear(line);
        int position = getKeyPosition(key);

        return position != -1 ? chunks[position] : "";
    }

    private int getKeyPosition(String key) {
        for (int i = 0; i < config.length; i++) {
            if (config[i] != null && config[i].equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    private String[] splitAndClear(String data) {
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
