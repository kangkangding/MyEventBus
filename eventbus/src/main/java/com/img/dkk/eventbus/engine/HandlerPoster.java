package com.img.dkk.eventbus.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by  dingkangkang on 2019/10/9
 * email：851615943@qq.com
 */
public class HandlerPoster extends Handler {

    private EventBus eventBus;
    private SubscriberMethod subscriber;
    private Object event;

    public HandlerPoster(Looper looper) {
        super(looper);
    }

    public void enqueue(EventBus eventBus, SubscriberMethod subscriber, Object event) {
        this.eventBus = eventBus;
        this.subscriber = subscriber;
        this.event = event;
        sendMessage(obtainMessage());
    }

    @Override public void handleMessage(Message msg) {
        eventBus.invoke(subscriber,event);
    }


}
