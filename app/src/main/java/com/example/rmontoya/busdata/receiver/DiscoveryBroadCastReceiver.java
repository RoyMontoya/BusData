package com.example.rmontoya.busdata.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.rmontoya.busdata.bus.EventBus;

public class DiscoveryBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        EventBus.getInstance().onNext(intent.getAction());
    }

}
