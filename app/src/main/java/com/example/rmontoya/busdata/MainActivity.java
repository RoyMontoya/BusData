package com.example.rmontoya.busdata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.rmontoya.busdata.service.BitmapReceiver;
import com.example.rmontoya.busdata.service.DownloadReceiver;
import com.example.rmontoya.busdata.service.DownloadService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BitmapReceiver{

    @BindView(R.id.main_image)
    ImageView mainImage;
    private DownloadReceiver downloadReceiver;
    public static final String RECEIVER = "RECEIVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupReceiver();
        startDownloadService();
    }

    private void setupReceiver() {
        downloadReceiver = new DownloadReceiver(new Handler());
        downloadReceiver.setReceiver(this);
    }

    private void startDownloadService() {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(RECEIVER, downloadReceiver);
        startService(intent);
    }



    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        if(resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.get(DownloadService.BITMAP);
            mainImage.setImageBitmap(bitmap);
        }
    }
}
