package com.example.simpleman383.monitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.simpleman383.shell.CommandOutput;
import com.example.simpleman383.shell.ICommandShell;
import com.example.simpleman383.shell.RuntimeShell;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.test_output);
        Button button = findViewById(R.id.test_button);

        final ICommandShell shell = RuntimeShell.getShell();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemProcessMonitor monitor = new SystemProcessMonitor();

                monitor.startMonitoring();
            }
        });




    }
}
