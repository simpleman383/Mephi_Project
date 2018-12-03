package com.example.simpleman383.tasking;

import android.util.Log;

public class PeriodicTask extends BaseTask {

    public int timeout = 5000;

    public PeriodicTask(IRunnableTaskUnit... units) {
        super(units);
    }

    protected void execute()
    {
        while (!cancellationToken.isCancellationRequested())
        {
            runCommandSequence();
            onPostExecute();

            try {
                taskThread.sleep(timeout);
            } catch (InterruptedException ex) {
                Log.e(this.getClass().getName(), ex.toString());
            }
        }
    }

}