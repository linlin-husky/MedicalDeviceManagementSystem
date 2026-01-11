/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.models.device;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author rajath
 */
public class DeviceDirectory {
    private List<Device> deviceList;

    public DeviceDirectory() {
        deviceList = new ArrayList<>();
    }

    public Device addNewDevice() {
        Device newDevice = new Device();
        deviceList.add(newDevice);
        return newDevice;
    }

    public void addDevice(Device device) {
        if (device != null && !deviceList.contains(device)) {
             deviceList.add(device);
        }
    }

    public boolean removeDevice(Device device) {
        return deviceList.remove(device);
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public Device findDeviceById(String deviceID) {
        if (deviceID == null || deviceID.trim().isEmpty()) {
            return null;
        }
        String idToFind = deviceID.trim();
        for (Device device : deviceList) {
            if (device.getDeviceID() != null && device.getDeviceID().equals(idToFind)) {
                return device;
            }
        }
        return null;
    }

    public List<Device> findDevicesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String nameLower = name.trim().toLowerCase();
        return deviceList.stream()
                         .filter(device -> device.getName() != null && device.getName().toLowerCase().contains(nameLower))
                         .collect(Collectors.toList());
    }
    
      public Device searchDeviceByName(String name) {
        for (Device device : deviceList) {
            if (device.getName().equalsIgnoreCase(name)) {
                return device;
            }
        }
        return null;
    }
      
      
    public Device searchDeviceById(String ID) {
        if (ID == null || ID.trim().isEmpty()) return null;
        
        for (Device device : deviceList) {
            if (device.getDeviceID() != null && device.deviceID.equals(ID) ) {
                return device;
            }
        }
        return null;
    }

    public List<Device> findDevicesByType(String type) {
         if (type == null || type.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String typeLower = type.trim().toLowerCase();
         return deviceList.stream()
                          .filter(device -> device.getType() != null && device.getType().toLowerCase().contains(typeLower))
                          .collect(Collectors.toList());
    }
}