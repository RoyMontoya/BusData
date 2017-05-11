package com.example.rmontoya.busdata.model;

public class BTdevice {

    private String macAddress;
    private short rssi;

    public BTdevice(String macAddress, short rssi){
        this.macAddress = macAddress;
        this.rssi = rssi;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public short getRssi() {
        return rssi;
    }

    public void setRssi(short rssi) {
        this.rssi = rssi;
    }
}
