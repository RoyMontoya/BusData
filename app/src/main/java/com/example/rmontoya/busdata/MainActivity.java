package com.example.rmontoya.busdata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.rmontoya.busdata.bus.EventBus;
import com.example.rmontoya.busdata.service.DownloadService;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_image)
    ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startDownloadService();
    }

    private void setupObserver() {
        Observer<Object> mainImageObserver = new Observer<Object>() {
            @Override
            public void onCompleted() {
                Log.d("Observer", "Called onComplete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Observer", e.getMessage());
            }

            @Override
            public void onNext(Object notificationObject) {
                Observable.just(notificationObject)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(o -> o instanceof Bitmap)
                        .subscribe(o -> mainImage.setImageBitmap((Bitmap) o));
            }
        };
        EventBus.getInstance().subscribe(mainImageObserver);
    }

    private void startDownloadService() {
        setupObserver();
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

}
