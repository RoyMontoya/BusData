package com.example.rmontoya.busdata.model;

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

    @Override
    public boolean equals(Object obj) {

        return obj instanceof BtDevice && ((BtDevice) obj).getMacAddress().equals(this.getMacAddress());
    }
}