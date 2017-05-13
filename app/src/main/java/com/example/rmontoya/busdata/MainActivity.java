package com.example.rmontoya.busdata;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.example.rmontoya.busdata.adapter.DeviceAdapter;
import com.example.rmontoya.busdata.adapter.ImageAdapter;
import com.example.rmontoya.busdata.bus.EventBus;
import com.example.rmontoya.busdata.model.BtDevice;
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
    private List<BtDevice> deviceList = new ArrayList<>();
    private DeviceAdapter adapter;
    private String lastDevice = "";
    private int REQUEST_PERMISSION = 0;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkAppPermission();
    }

    private void checkAppPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setupActivityComponents();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH},
                    REQUEST_PERMISSION);
        }
    }

    private void setupActivityComponents() {
        setupViews();
        setupBlueToothRecyclerView();
        setupBroadcastReceivers();
        setupBlueToothDeviceObserver();
        setupDiscoveryFinishedObserver();
    }

    private void setupViews() {
        RxView.clicks(loadButton).subscribe(aVoid -> startDownloadService());
        RxView.clicks(bluetoothButton).subscribe(aVoid -> {
            if (!bluetoothAdapter.isDiscovering() && isPhoneBlueToothOn()) {
                bluetoothAdapter.startDiscovery();
            }
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

    private boolean isPhoneBlueToothOn() {
        if (!bluetoothAdapter.isEnabled()) {
            startBlueToothIntent();
            return false;
        }
        return true;
    }

    private void startBlueToothIntent() {
        Intent blueToothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(blueToothIntent, REQUEST_ENABLE_BLUETOOTH);
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
                .filter(object -> object instanceof BtDevice)
                .filter(bTdevice -> isNotLastDevice((BtDevice) bTdevice))
                .subscribe(bTDevice -> {
                    BtDevice newDevice = (BtDevice) bTDevice;
                    int index = BtDevice.getDeviceIndex(newDevice, deviceList);
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
                .subscribe(o -> {
                    if (isPhoneBlueToothOn()) bluetoothAdapter.startDiscovery();
                }));
    }

    private boolean isNotLastDevice(BtDevice btDevice) {
        if (!lastDevice.equals(btDevice.getMacAddress())) {
            lastDevice = btDevice.getMacAddress();
            return true;
        }
        return false;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkAppPermission();
        } else {
            Toast.makeText(this, R.string.useless_app, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_ENABLE_BLUETOOTH && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.startDiscovery();
        }
    }

}