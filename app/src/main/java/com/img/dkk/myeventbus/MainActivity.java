package com.img.dkk.myeventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.img.dkk.eventbus.engine.EventBus;
import com.img.dkk.eventbus.engine.Subscribe;
import com.img.dkk.eventbus.engine.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
    }

    public void hhhhhh(View view) {
        //EventBus.getDefault().post(new UserProtocolEvent());

        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserProtocolEvent(TestEvent event) {

        Log.e("MainActivity","接收到消息了");

        Log.e("MainActivity","=====Thread===="+Thread.currentThread().getName());


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
