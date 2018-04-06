package com.chan.revernue.labthread.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class CounterIntentService extends IntentService {
    public CounterIntentService(){
        super("");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CounterIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        for (int i = 0; i < 100; i++) {
            Log.d("IntentService", "i = " + i);
            Intent brodcastIntent = new Intent("CounterIntentServiceUpdate");
            brodcastIntent.putExtra("counter", i);
            LocalBroadcastManager.getInstance(CounterIntentService.this)
                .sendBroadcast(brodcastIntent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
