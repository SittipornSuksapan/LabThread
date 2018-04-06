package com.chan.revernue.labthread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.chan.revernue.labthread.service.CounterIntentService;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    int counter;
    TextView tvCounter;
    Thread thread;
    Handler handler;

    HandlerThread backgroundHandlerThread;
    Handler backgroundHandler;
    Handler mainHandler;

    SampleAsyncTask sampleAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = (TextView) findViewById(R.id.tvCounter);

       /* thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i=0; i<100; i++) {
                    counter++;
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCounter.setText(counter + "");
                        }
                    });

                }

            }
        });
        thread.start();*/

       //2

      /*  handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tvCounter.setText(msg.arg1 + "");
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i=0; i<100; i++) {
                    counter++;
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        return;
                    }
                    Message msg = new Message();
                    msg.arg1 = counter;
                    handler.sendMessage(msg);


                }

            }
        });
        thread.start();*/


      //3
      /*  handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                counter++;
                tvCounter.setText(counter + "");
                if (counter < 100)
                    sendEmptyMessageDelayed(0, 1000);
            }
        };
        handler.sendEmptyMessageDelayed(0, 1000);*/


      //4

       /* backgroundHandlerThread = new HandlerThread("BackgroundHandlerThread");
        backgroundHandlerThread.start();

        backgroundHandler = new Handler(backgroundHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Message msgMain = new Message();
                msgMain.arg1 = msg.arg1 + 1;
                mainHandler.sendMessage(msgMain);
            }
        };
        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tvCounter.setText(msg.arg1 + "");
                if (msg.arg1 < 100){
                    Message msgBack = new Message();
                    msgBack.arg1 = msg.arg1;
                    backgroundHandler.sendEmptyMessageDelayed(100, 1000);
                }

            }
        };
        Message msgBack = new Message();
        msgBack.arg1 = 0;
        backgroundHandler.sendEmptyMessageDelayed(100, 1000);*/

       //5
//        sampleAsyncTask = new SampleAsyncTask();
//        sampleAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0, 100);

        //6
       // getSupportLoaderManager().initLoader(1, null, this);


        //7

        LocalBroadcastManager.getInstance(MainActivity.this)
                .registerReceiver(counterBroadcastReceiver, new IntentFilter("CounterIntentServiceUpdate"));

        Intent intent = new Intent(MainActivity.this, CounterIntentService.class);
        intent.putExtra("abc", "123");
        startService(intent);


    }

    protected BroadcastReceiver counterBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int counter = intent.getIntExtra("counter", 0);
            tvCounter.setText(counter+ "");

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //thread.interrupt();
        //backgroundHandlerThread.quit();
//        sampleAsyncTask.cancel(true);
        LocalBroadcastManager.getInstance(MainActivity.this)
                .unregisterReceiver(counterBroadcastReceiver);

    }

    @NonNull
    @Override
    public Loader<Object> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == 1){
            return new AdderAsyncTaskLoader(MainActivity.this, 5, 11);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Object> loader, Object data) {
        Log.d("HHH", "onLoadFinished");
        if (loader.getId()==1){
            Integer result = (Integer) data;
            tvCounter.setText(result + "");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Object> loader) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    static class AdderAsyncTaskLoader extends AsyncTaskLoader<Object>{

        int a;
        int b;

        public AdderAsyncTaskLoader(@NonNull Context context, int a, int b) {
            super(context);
            this.a = a;
            this.b = b;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            Log.d("HHH", "onStartLoading");
            forceLoad();
        }

        @Nullable
        @Override
        public Integer loadInBackground() {
            Log.d("HHH", "LoadInBackground");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
            return a + b;
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            Log.d("HHH", "onStopLoading");
        }
    }

    class SampleAsyncTask extends AsyncTask<Integer, Float, Boolean>{

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int start = integers[0];
            int end = integers[1];
            for (int i = start; i < end; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return false;

                }
                publishProgress(i + 0.0f);

            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            float progress = values[0];
            tvCounter.setText(progress + "%");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }
}
