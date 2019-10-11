package com.img.dkk.eventbus.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by  dingkangkang on 2019/10/9
 * email：851615943@qq.com
 */
public class EventBus {

    private static EventBus eventBus;

    private Map<Class<?>,List<SubscriberMethod>> subscriberMethodEventAll  = new HashMap<>(); // key 是 event
    private List<Object> subscribeList = new ArrayList<>(); // 注册的对象
    public static EventBus getDefault(){
        if(eventBus == null){
            synchronized (EventBus.class){
                if(eventBus == null){
                    eventBus = new EventBus();
                }
            }

        }
        return eventBus;
    }

    //注册
    public void register(Object subScriber){

        Class<?> aClass = subScriber.getClass();

        try {

            if(!subscribeList.contains(aClass)){
                subscribeList.add(aClass);
                getSubscriberMethod(subScriber,aClass);
                Log.e("EventBus:::" + aClass,"注册成功");
            }else {
                Log.e("EventBus:::" + aClass,"不能重复注册");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取消注册
    public void unRegister(Object subScriber){

        for (List<SubscriberMethod> subscriberMethodList : subscriberMethodEventAll.values()) {
            int size = subscriberMethodList.size();
            for (int i = 0; i < size; i++) {
                if(subscriberMethodList.get(i).subscriber == subScriber){
                    subscriberMethodList.remove(i);
                    i--;
                    size--;
                    if(subscribeList.contains(subScriber.getClass())){
                        subscribeList.remove(subScriber.getClass());
                    }
                }
            }
        }
        clearEventType();
    }

    //清除为空的event
    private void clearEventType() {
        Iterator<Map.Entry<Class<?>, List<SubscriberMethod>>>
            iterator = subscriberMethodEventAll.entrySet().iterator();
        while (iterator.hasNext()){
            if(iterator.next().getValue() == null || iterator.next().getValue().size() == 0){
                subscriberMethodEventAll.remove(iterator.next().getKey());
            }

        }
    }

    public void post(Object event){
        if(subscriberMethodEventAll.containsKey(event.getClass())){
            postToSubscribe(subscriberMethodEventAll.get(event.getClass()),event);
        }
    }

    private void postToSubscribe(List<SubscriberMethod> subscriberMethods, final Object event) {

        for (int i = 0;i<subscriberMethods.size();i++){
            try {

                final SubscriberMethod subscriberMethod = subscriberMethods.get(i);

                switch (subscriberMethod.threadMode){
                    case MAIN:
                        if(Looper.getMainLooper() == Looper.myLooper()){
                            subscriberMethod.method.invoke(subscriberMethods.get(i).subscriber,event);
                        }else {
                            HandlerPoster handlerPoster = new HandlerPoster(Looper.getMainLooper());
                            handlerPoster.enqueue(EventBus.getDefault(),subscriberMethods.get(i),event);

                        }
                        break;
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取 订阅的方法
     * @param aClass
     * @return
     */
    private HashMap<Object,List<SubscriberMethod>> getSubscriberMethod(Object subScriber,Class<?> aClass) throws Exception {
        Method[] methods ;

        HashMap<Object,List<SubscriberMethod>> subscriberMethodEvent = new HashMap<>();
        try {
            methods = aClass.getDeclaredMethods();
        }catch (Throwable th){
            methods = aClass.getMethods();
        }
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if ((modifiers & Modifier.PUBLIC) != 0) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                    if (subscribeAnnotation != null) {
                        Class<?> eventType = parameterTypes[0];
                        ThreadMode threadMode = subscribeAnnotation.threadMode();

                        SubscriberMethod subscriberMethod = new SubscriberMethod(method, eventType, threadMode,
                            subscribeAnnotation.priority(), subscribeAnnotation.sticky(),Looper.myLooper(),subScriber);

                        List<SubscriberMethod> subscriberMethodList = new ArrayList<>();

                        if(!subscriberMethodList.contains(subscriberMethod)){
                            subscriberMethodList.add(subscriberMethod);

                            if(subscriberMethodEventAll.containsKey(eventType)){

                                subscriberMethodList.addAll(subscriberMethodEventAll.get(eventType));

                                subscriberMethodEventAll.put(eventType,subscriberMethodList);
                            }else {
                                subscriberMethodEventAll.put(eventType,subscriberMethodList);
                            }

                        }

                    }
                } else if (method.isAnnotationPresent(Subscribe.class)) {
                    String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                    throw new Exception("@Subscribe method " + methodName +
                        "must have exactly 1 parameter but has " + parameterTypes.length);
                }
            } else if (method.isAnnotationPresent(Subscribe.class)) {
                String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                throw new Exception(methodName +
                    " is a illegal @Subscribe method: must be public, non-static, and non-abstract");
            }
        }

        return subscriberMethodEvent;
    }

    public void invoke(SubscriberMethod subscriberMethod,Object event) {

        try {
            subscriberMethod.method.invoke(subscriberMethod.subscriber,event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
