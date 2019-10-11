package com.img.dkk.eventbus.engine;

import android.os.Looper;
import java.lang.reflect.Method;

/**
 * Created by  dingkangkang on 2019/10/9
 * email：851615943@qq.com
 */
public class SubscriberMethod {

    final Method method;
    final ThreadMode threadMode;
    final Class<?> eventType;
    final int priority;
    final boolean sticky;
    /** Used for efficient comparison */
    String methodString;
    Looper looper;
    Object subscriber;

    public SubscriberMethod(Method method, Class<?> eventType, ThreadMode threadMode, int priority, boolean sticky,Looper looper,Object subscriber) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.priority = priority;
        this.sticky = sticky;
        this.looper = looper;
        this.subscriber = subscriber;
    }
}
