package com.example.simpleman383.monitoring;

import com.example.simpleman383.shell.CommandOutput;
import com.example.simpleman383.shell.ICommandShell;
import com.example.simpleman383.shell.RuntimeShell;
import com.example.simpleman383.tasking.IRunnableTaskUnit;
import com.example.simpleman383.tasking.PeriodicTask;
import com.example.simpleman383.tasking.TaskCompletedListener;

import java.util.ArrayList;
import java.util.List;

public class SystemProcessMonitor {

    public List<ProcessInfo> getCurrentProcesses() {
        return currentProcesses;
    }

    private List<ProcessInfo> currentProcesses;

    private ProcessCrawler crawler;
    private PeriodicTask task;

    public SystemProcessMonitor() {

        crawler = setupProcessCrawler();

        task = new PeriodicTask(new IRunnableTaskUnit() {
            @Override
            public void run() {
                currentProcesses = new ArrayList<>();
                ProcessInfo[] processes = crawler.getCurrentProcesses();

                if (processes != null) {
                    for (ProcessInfo processInfo: processes) {
                        currentProcesses.add(processInfo);
                    }
                }
            }
        });
    }


    protected ProcessCrawler setupProcessCrawler() {
        return new ProcessCrawler() {
            final String OUTPUT_CONFIGURATION = "PID,PPID,NAME,USER";

            @Override
            public ProcessInfo[] getCurrentProcesses() {
                ICommandShell shell = RuntimeShell.getShell();

                CommandOutput output = shell.RunCommand("ps", "-A", "-o", OUTPUT_CONFIGURATION);


                String[] outputLines = output.getStandartOutputLines();
                if (outputLines == null || outputLines != null && outputLines.length <= 1) {
                    return new ProcessInfo[0];
                }

                List<ProcessInfo> infos = new ArrayList<>();
                for (int i = 1; i < outputLines.length; i++) {
                    ProcessInfo info = ProcessInfo.fromPSCommandOutput(outputLines[i], outputLines[0]);
                    if (info != null) {
                        infos.add(info);
                    }
                }
                return infos.toArray(new ProcessInfo[infos.size()]);

                /*PsCommandParser parser = new PsCommandParser();
                if (output != null) {
                    return parser.parse(output.getStandartOutputLines());
                } else {
                    return new ProcessInfo[0];
                }*/
            }
        };

    }

    public void startMonitoring() {
        task.start();
    }


    public void stopMonitoring() {
        task.stop();
    }



}
