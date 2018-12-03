package com.example.simpleman383.shell;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class RuntimeShell implements ICommandShell {

    private static RuntimeShell instance;

    public static ICommandShell getShell() {
        if (instance == null) {
            instance = new RuntimeShell();
        }
        return instance;
    }

    private Runtime runtime;

    private RuntimeShell() {
        runtime = Runtime.getRuntime();
    }


    private List<String> readProcessOutput(InputStream stream) {

        List<String> buffer = new ArrayList<>();

        try {
            InputStreamReader inputStream = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(inputStream);

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.add(line);
            }

            reader.close();
        }
        catch (IOException ex) {
            Log.e(this.getClass().getName(), ex.toString());
        }
        finally {
            return buffer;
        }
    }

    private Thread[] getReadThreads(final Process process, final CommandOutputBuilder builder) {
        if (process == null) {
            return new Thread[0];
        }

        Thread stdOutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> stdOut = readProcessOutput(process.getInputStream());
                for (String line: stdOut) {
                    builder.appendOutputLine(line);
                }
            }
        });

        Thread stdErrThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> stdErr = readProcessOutput(process.getErrorStream());
                for (String line: stdErr) {
                    builder.appendErrorLine(line);
                }
            }
        });

        return new Thread[]{ stdOutThread, stdErrThread };
    }


    public CommandOutput RunCommand(String... args) {

        final CommandOutputBuilder cmdOutputBuilder = new CommandOutputBuilder();
        if  (args == null) {
            return cmdOutputBuilder;
        }

        try {
            Process process = runtime.exec(args);

            Thread[] threads = getReadThreads(process, cmdOutputBuilder);

            for (Thread t : threads) {
                t.run();
            }

            process.waitFor();

            return cmdOutputBuilder;
        }
        catch (IOException ex) {
            Log.e(this.getClass().getName(), ex.toString());
            return null;
        }
        catch (InterruptedException ex) {
            Log.e(this.getClass().getName(), ex.toString());
            return null;
        }

    }

}
