package com.example.rmontoya.busdata;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.rmontoya.busdata.adapter.DeviceAdapter;
import com.example.rmontoya.busdata.adapter.ImageAdapter;
import com.example.rmontoya.busdata.bus.EventBus;
import com.example.rmontoya.busdata.model.BTdevice;
import com.example.rmontoya.busdata.receiver.DeviceBroadCastReceiver;
import com.example.rmontoya.busdata.service.DownloadService;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.load_button)
    Button loadButton;
    @BindView(R.id.bluetooth_button)
    Button bluetoothButton;
    @BindView(R.id.main_list)
    RecyclerView mainList;
    private BluetoothAdapter bluetoothAdapter;
    private List<BTdevice> deviceList = new ArrayList<>();
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setViews();
    }

    private void setViews() {
        RxView.clicks(loadButton).subscribe(aVoid -> startDownloadService());
        RxView.clicks(bluetoothButton).subscribe(aVoid -> {
            startBlueToothScan();
        });
    }

    private void startBlueToothScan() {
        setupBlueToothDeviceObserver();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        DeviceBroadCastReceiver broadCastReceiver = new DeviceBroadCastReceiver();
        this.registerReceiver(broadCastReceiver, intentFilter);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothAdapter.startDiscovery();
    }

    private void setupBlueToothRecyclerView() {
        setupBlueToothDeviceObserver();
        mainList.setLayoutManager(new LinearLayoutManager(this));
        mainList.setAdapter(adapter);

    }

    private void setupBlueToothDeviceObserver() {
        EventBus.getInstance().subscribe(o -> Observable.just(o)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(object -> object instanceof BTdevice)
                .subscribe(blueToothDevice -> {
                    deviceList.add((BTdevice) blueToothDevice);
                    notifyAdapter();
                }));
    }


    private void notifyAdapter() {
        if (adapter == null) {
            adapter = new DeviceAdapter(deviceList);
            setupBlueToothRecyclerView();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void setUpImageRecyclerView() {
        mainList.setLayoutManager(new LinearLayoutManager(this));
        mainList.setAdapter(new ImageAdapter());
    }

    private void startDownloadService() {
        setUpImageRecyclerView();
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

}