package com.example.rmontoya.busdata.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rmontoya.busdata.R;
import com.example.rmontoya.busdata.model.BTdevice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<BTdevice> items;

    public DeviceAdapter(List<BTdevice> devices) {
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
        holder.deviceAddressText.setText(items.get(position).getMacAddress());
        holder.deviceRssiText.setText(String.valueOf(items.get(position).getRssi()));
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

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
