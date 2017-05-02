package com.example.rmontoya.busdata.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class DownloadReceiver extends ResultReceiver {

    private BitmapReceiver receiver;

    public DownloadReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(BitmapReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, data);
        }
    }

}
