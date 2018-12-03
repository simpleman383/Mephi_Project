package com.example.simpleman383.tasking;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTask
{
    private List<IRunnableTaskUnit> runSequence = new ArrayList<>();
    protected Thread taskThread;
    protected CancellationToken cancellationToken;
    public TaskCompletedListener taskCompletedListener;

    private BaseTask()
    {
        taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runTask();
            }
        });
        cancellationToken = new CancellationToken();
    }

    protected BaseTask(IRunnableTaskUnit... units)
    {
        this();
        addUnits(null, units);
    }

    public void addUnits(IRunnableTaskUnit unit, IRunnableTaskUnit... units)
    {
        if (unit != null)
            runSequence.add(unit);

        if (units != null) {
            for (IRunnableTaskUnit u: units) {
                runSequence.add(u);
            }
        }
    }

    protected void onPreExecute() {}
    protected abstract void execute();
    protected void onPostExecute() {}

    protected void runCommandSequence()
    {
        for (int i = 0; i < runSequence.size(); i++)
        {
            if (cancellationToken.isCancellationRequested())
                break;
            else
                runSequence.get(i).run();
        }
    }

    private void runTask()
    {
        onPreExecute();
        execute();

        if (cancellationToken.isCancellationRequested())
        {
            return;
        }

        onPostExecute();

        if (taskCompletedListener != null)
            taskCompletedListener.onTaskCompleted();
    }

    public void start()
    {
        taskThread.run();
    }

    protected void onCancel() {}
    protected void cancel()
    {
        if (taskThread.isAlive())
        {
            cancellationToken.cancel();
        }
    }

    public void stop()
    {
        onCancel();
        cancel();
    }

}