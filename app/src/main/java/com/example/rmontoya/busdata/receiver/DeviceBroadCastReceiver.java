package com.example.rmontoya.busdata.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.rmontoya.busdata.bus.EventBus;
import com.example.rmontoya.busdata.model.BtDevice;

public class DeviceBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        short RSSI = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
        BtDevice btDevice = new BtDevice(device.getAddress(), RSSI);
        EventBus.getInstance().onNext(btDevice);
    }

}