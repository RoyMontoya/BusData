package com.example.rmontoya.busdata.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rmontoya.busdata.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;


public class DownloadService extends IntentService {
    private static final String NAME = "DownloadService";
    private static final String IMAGE_URL = "https://goo.gl/S1lpGE";
    public static final String BITMAP = "BITMAP";

    public DownloadService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(MainActivity.RECEIVER);

        AsyncHttpClient client = new SyncHttpClient();
        client.get(IMAGE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BITMAP, bitmap);
                    receiver.send(Activity.RESULT_OK, bundle);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(NAME, statusCode + "error downloading bitmap");
            }
        });
    }

}
