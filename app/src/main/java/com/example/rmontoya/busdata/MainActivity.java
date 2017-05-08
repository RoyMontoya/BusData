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