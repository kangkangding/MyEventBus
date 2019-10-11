package com.img.dkk.myeventbus;

import android.util.Log;
import com.img.dkk.eventbus.engine.EventBus;
import com.img.dkk.eventbus.engine.Subscribe;
import com.img.dkk.eventbus.engine.ThreadMode;

/**
 * Created by  dingkangkang on 2019/10/10
 * email：851615943@qq.com
 */
public class MyThread extends Thread{

    public void init(){
        //EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserProtocolEvent(TestEvent event) {

        Log.e("MyThread","接收到消息了");

        Log.e("MyThread","=====Thread===="+Thread.currentThread().getName());
    }

    @Override public void run() {
        EventBus.getDefault().post(new TestEvent());
        Log.e("MyThread","=====发送消息Thread===="+Thread.currentThread().getName());

    }
}
