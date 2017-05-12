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
import com.example.rmontoya.busdata.receiver.DiscoveryBroadCastReceiver;
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
    private String lastDevice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setViews();
        setupBlueToothRecyclerView();
        setupBroadcastReceivers();
        setupBlueToothDeviceObserver();
        setupDiscoveryFinishedObserver();
    }

    private void setViews() {
        RxView.clicks(loadButton).subscribe(aVoid -> startDownloadService());
        RxView.clicks(bluetoothButton).subscribe(aVoid -> {
            if (!bluetoothAdapter.isDiscovering()) bluetoothAdapter.startDiscovery();
        });
    }

    private void setupBroadcastReceivers() {
        IntentFilter deviceIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter discoveryIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        DeviceBroadCastReceiver deviceBroadCastReceiver = new DeviceBroadCastReceiver();
        DiscoveryBroadCastReceiver discoveryBroadCastReceiver = new DiscoveryBroadCastReceiver();
        this.registerReceiver(deviceBroadCastReceiver, deviceIntentFilter);
        this.registerReceiver(discoveryBroadCastReceiver, discoveryIntentFilter);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void setupBlueToothRecyclerView() {
        setupBlueToothDeviceObserver();
        mainList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceAdapter(deviceList);
        mainList.setAdapter(adapter);
    }

    private void setupBlueToothDeviceObserver() {
        EventBus.getInstance().subscribe(receivedObject -> Observable.just(receivedObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(object -> object instanceof BTdevice)
                .filter(bTdevice -> isNotLastDevice((BTdevice) bTdevice))
                .subscribe(bTDevice -> {
                    BTdevice newDevice = (BTdevice) bTDevice;
                    int index = getDeviceIndex(newDevice);
                    if (index != -1) {
                        deviceList.set(index, newDevice);
                    } else {
                        deviceList.add(newDevice);
                    }
                    adapter.notifyDataSetChanged();
                }));
    }


    private void setupDiscoveryFinishedObserver() {
        EventBus.getInstance().subscribe(receivedObject -> Observable.just(receivedObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(object -> object instanceof String)
                .filter(stringAction -> stringAction.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
                .subscribe(o ->
                        bluetoothAdapter.startDiscovery()));
    }


    private boolean isNotLastDevice(BTdevice bTdevice) {
        if (lastDevice != bTdevice.getMacAddress()) {
            lastDevice = bTdevice.getMacAddress();
            return true;
        }
        return false;
    }

    private int getDeviceIndex(BTdevice bTdevice) {
        int index = -1;
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getMacAddress().equals(bTdevice.getMacAddress())) {
                index = i;
            }
        }
        return index;
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