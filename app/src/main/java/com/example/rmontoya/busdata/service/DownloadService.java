package com.example.rmontoya.busdata.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class DownloadService extends IntentService {
    private static final String NAME = "DownloadService";

    public DownloadService(){
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
