package com.example.rmontoya.busdata.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rmontoya.busdata.R;
import com.example.rmontoya.busdata.bus.EventBus;
import com.example.rmontoya.busdata.model.BtDevice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<BtDevice> items;

    public DeviceAdapter(List<BtDevice> devices) {
        this.items = devices;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.device_row, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.deviceAddressText.setText(holder.device.getMacAddress());
        holder.deviceRssiText.setText(String.valueOf(holder.device.getRssi()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_address)
        TextView deviceAddressText;
        @BindView(R.id.device_rssi)
        TextView deviceRssiText;
        BtDevice device;

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            device = items.get(items.size() - 1);
            EventBus.getInstance().subscribe(receivedObject -> Observable.just(receivedObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(object -> object instanceof BtDevice)
                    .filter(bTdevice -> ((BtDevice)bTdevice).getMacAddress().equals(device.getMacAddress()))
                    .subscribe(bTDevice -> {
                        device = (BtDevice) bTDevice;
                        updateRssi();
                    }));
        }

        private void updateRssi() {
            deviceRssiText.setText(String.valueOf(device.getRssi()));
        }

    }

}