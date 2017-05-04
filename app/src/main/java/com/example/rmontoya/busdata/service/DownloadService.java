package com.example.rmontoya.busdata.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rmontoya.busdata.bus.EventBus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;


public class DownloadService extends IntentService {
    private static final String NAME = "DownloadService";
    private static final String IMAGE_URL = "https://goo.gl/S1lpGE";

    public DownloadService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AsyncHttpClient client = new SyncHttpClient();
        client.get(IMAGE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                    EventBus.getInstance().onNext(bitmap);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(NAME, statusCode + "error downloading bitmap");
            }
        });
    }

}
