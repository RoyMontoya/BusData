package com.example.rmontoya.busdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.rmontoya.busdata.adapter.BusAdapter;
import com.example.rmontoya.busdata.service.DownloadService;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.load_button)
    Button loadButton;
    @BindView(R.id.main_list)
    RecyclerView mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setViews();

    }

    private void setViews() {
        setUpRecyclerView();
        RxView.clicks(loadButton).subscribe(aVoid -> startDownloadService());
    }

    private void setUpRecyclerView() {
        mainList.setLayoutManager(new LinearLayoutManager(this));
        mainList.setAdapter(new BusAdapter());
    }


    private void startDownloadService() {
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

}


//    private void setupObserver() {
//        Observer<Object> mainImageObserver = new Observer<Object>() {
//            @Override
//            public void onCompleted() {
//                Log.d("Observer", "Called onComplete");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d("Observer", e.getMessage());
//            }
//
//            @Override
//            public void onNext(Object notificationObject) {
//                Observable.just(notificationObject)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .filter(o -> o instanceof Bitmap)
//                        .subscribe(o -> mainImage.setImageBitmap((Bitmap) o));
//            }
//        };
//        EventBus.getInstance().subscribe(mainImageObserver);
//    }