package com.example.rmontoya.busdata.model;

import java.util.List;

public class BtDevice {

    private String macAddress;
    private short rssi;

    public BtDevice(String macAddress, short rssi) {
        this.macAddress = macAddress;
        this.rssi = rssi;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public short getRssi() {
        return rssi;
    }

    public static int getDeviceIndex(BtDevice btDevice, List<BtDevice> deviceList) {
        int index = -1;
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getMacAddress().equals(btDevice.getMacAddress())) {
                index = i;
            }
        }
        return index;
    }

}