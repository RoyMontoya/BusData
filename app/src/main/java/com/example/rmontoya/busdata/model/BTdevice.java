package com.example.rmontoya.busdata.model;

public class BTdevice {

    private String macAddress;
    private short rssi;

    public BTdevice(String macAddress, short rssi) {
        this.macAddress = macAddress;
        this.rssi = rssi;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public short getRssi() {
        return rssi;
    }

}
