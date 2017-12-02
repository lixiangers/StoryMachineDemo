package com.roobo.scenedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.roobo.service.TtsService;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ACTION = "action";
    public static final String EXTRA_SUGGESTION = "suggestion";
    public static final String ACTION_TEST_MUSIC = "ACTION_TEST_MUSIC";
    private TextView tvAction;
    private TextView tvData;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAction = (TextView) findViewById(R.id.tv_action);
        tvData = (TextView) findViewById(R.id.tv_data);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                parseIntent(intent);
            }
        };
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ACTION_TEST_MUSIC);

        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    private void parseIntent(Intent intent) {
        if (null != intent) {
            String action = intent.getStringExtra(EXTRA_ACTION);
            TtsService.playText(getApplication(), "好的", null);
            Serializable suggestion = intent.getSerializableExtra(EXTRA_SUGGESTION);
            tvAction.setText(action);
            if (suggestion != null)
                tvData.setText(suggestion.toString());
        }
    }
}
