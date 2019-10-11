EventBus
EventBus is a publish/subscribe event bus for Android and Java.

仿 EventBus ，比 EventBus 更简洁，代码少， 可直接复制代码到项目里面使用

EventBus...


EventBus in 3 steps


Register and unregister your subscriber. For example on Android, activities and fragments should usually register according to their life cycle:

使用方法：


 @Override
 public void onStart() {
     super.onStart();
     // 注册
     EventBus.getDefault().register(this);
 }

  // 订阅
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(MessageEvent event) {/* Do something */};


 @Override
 public void onStop() {
     super.onStop();
     // 取消订阅
     EventBus.getDefault().unregister(this);
 }

Post events:

 // 发送消息
 EventBus.getDefault().post(new MessageEvent());