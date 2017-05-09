package com.example.rmontoya.busdata.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.rmontoya.busdata.bus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DeviceBroadCastReceiver extends BroadcastReceiver {

    private List<BluetoothDevice> deviceList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        deviceList.add(device);
        EventBus.getInstance().onNext(deviceList);
    }
}
