package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.Device;
import com.example.model.Devices;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @GetMapping
    public List<Devices> getDevices() {
        List<Devices> devices = new ArrayList<>();

        // Process inventory list for Brand: Alpha
        Device alpha1 = new Device("Alpha 1", "Touch phone");
        Device alpha2 = new Device("Alpha 2", "Note");
        List<Device> alphaDevices = new ArrayList<>();
        alphaDevices.add(alpha1);
        alphaDevices.add(alpha2);

        // Process inventory list for Brand: Beta
        Device beta1 = new Device("Beta 1", "PDA");
        Device beta2 = new Device("Beta 2", "Note pad");
        List<Device> betaDevices = new ArrayList<>();
        betaDevices.add(beta1);
        betaDevices.add(beta2);

        // Add structural sets to root container
        devices.add(new Devices(alphaDevices, "Alpha"));
        devices.add(new Devices(betaDevices, "Beta"));

        return devices;
    }
}
