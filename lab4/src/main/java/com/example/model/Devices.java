package com.example.model;

import java.util.List;

public class Devices {
    private List<Device> devices;
    private String brand;

    public Devices() {
        super();
    }

    public Devices(List<Device> devices, String brand) {
        this.devices = devices;
        this.brand = brand;
    }

    public List<Device> getDevices() { return devices; }
    public void setDevices(List<Device> devices) { this.devices = devices; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}
