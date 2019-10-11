package com.img.dkk.myeventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.img.dkk.eventbus.engine.EventBus;
import com.img.dkk.eventbus.engine.Subscribe;
import com.img.dkk.eventbus.engine.ThreadMode;

public class Main2Activity extends AppCompatActivity {
    MyThread myThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myThread = new MyThread();
        myThread.init();
        EventBus.getDefault().register(this);
    }

    public void aaaaaa(View view) {

        new Thread(new Runnable() {
            @Override public void run() {
                Log.e("Main2Activity","=====发送消息Thread===="+Thread.currentThread().getName());
                EventBus.getDefault().post(new UserProtocolEvent());
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserProtocolEvent(TestEvent event) {

        Log.e("Main2Activity","接收到消息了");

        Log.e("Main2Activity","=====Thread===="+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserProtocolEventsss(UserProtocolEvent event) {

        Log.e("Main2Activity","接收到消息了");

        Log.e("Main2Activity","=====Thread===="+Thread.currentThread().getName());
    }

    //发送子线程消息
    public void ssssss(View view) {
        myThread.run();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unRegister(this);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
