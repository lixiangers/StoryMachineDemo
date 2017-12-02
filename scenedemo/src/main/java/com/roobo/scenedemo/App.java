package com.roobo.scenedemo;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.roobo.core.power.RooboPowerManager;
import com.roobo.core.scene.SceneEventListener;
import com.roobo.core.scene.SceneHelper;
import com.roobo.logcat.Log;

import java.io.Serializable;


/**
 * Created by lixiang on 17-11-30.
 */

public class App extends Application {
    public static final String TAG = "scenedemo_tag";
    private RooboPowerManager.RooboWakeLock mWakeLock;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        mWakeLock = RooboPowerManager.getInstance(getApplicationContext()).newWakeLock("active");

        SceneHelper.initialize(this);
        SceneHelper.setEventListener(new SceneEventListener() {
            @Override
            public void onSwitchIn(int flags) {
                super.onSwitchIn(flags);
                Log.i(TAG, "onSwitchIn");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onSwitchOut() {
                super.onSwitchOut();
                SceneHelper.setContext(null);
                SceneHelper.setService(null);
                Log.i(TAG, "onSwitchOut");
            }

            @Override
            public void onPause() {
                super.onPause();
                releaseWakeLock();
            }

            @Override
            public void onResume() {
                super.onResume();
                acquireWakeLock();
            }

            @Override
            public void onCommand(String action, Bundle params, Serializable suggestion) {
                super.onCommand(action, params, suggestion);
                Log.v(TAG, "onCommand. action: " + action + ", params: " + params + ", suggestion: " + suggestion);
                setContext(params);
                Intent intent = new Intent();
                intent.setAction(MainActivity.ACTION_TEST_MUSIC);
                intent.putExtra(MainActivity.EXTRA_ACTION, action);
                intent.putExtra(MainActivity.EXTRA_SUGGESTION, suggestion);
                sendBroadcast(intent);
            }
        });
    }

    public void setContext(Bundle params) {
        if (params != null && params.containsKey("outputContext")) {
            Bundle outputContext = params.getBundle("outputContext");
            if (outputContext != null) {
                String service = outputContext.getString("service");
                String context = outputContext.getString("context");
                if (!TextUtils.isEmpty(service)) {
                    SceneHelper.setService(service);
                }
                if (!TextUtils.isEmpty(context)) {
                    SceneHelper.setContext(context);
                }
                android.util.Log.e(TAG, "performHint: service " + service + "  context " + context);
            }
        }
    }

    public synchronized void releaseWakeLock() {
        mWakeLock.release();
    }

    private synchronized void acquireWakeLock() {
        mWakeLock.acquire();
    }
}
