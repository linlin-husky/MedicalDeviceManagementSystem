/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.models.device;

import Business.Enterprise.HospitalEnterprise;
import Business.models.room.Room;
import java.util.Objects;

/**
 *
 * @author rajath
 */
public class Device {
    
//    private String stringID;
//    private Integer deviceID;
    
    static int count = 1;
    String deviceID;

    private String name;
    private String type;
    private String manufacturer;
    private String acquisitionDate;
    private String lastMaintenanceDate;
    private Room currentLocation;

    
    private boolean isPortable;
    boolean isAvailable;
    
    private double price;  
    int availability;
    int yearofManufacturing;
    
    
    private int quantity = 1;
    private HospitalEnterprise owningHospital;
    private String deviceStatus;

    private boolean isComponent = false;
    private String parentDeviceID;
    private String componentType;
    private boolean isCoreComponent;

    private HospitalEnterprise rentedToHospital;
    private boolean rentable;

    

//    private static long idCounter = 100;

    public Device() {
        this.deviceStatus = "Available";
        this.isPortable = false;
        this.rentable = false;
        this.isComponent = false;
        this.isCoreComponent = false;
        this.quantity = 1;
        this.price = 0.0;
        this.isAvailable = false;
        this.deviceID = "DEV-" + String.format("%04d", count++);
    }
    
    public boolean getIsPortable() {
        return isPortable;
    }

    public void setIsPortable(boolean isPortable) {
        this.isPortable = isPortable;
    }
    
      public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public int getYearofManufacturing() {
        return yearofManufacturing;
    }

    public void setYearofManufacturing(int yearofManufacturing) {
        this.yearofManufacturing = yearofManufacturing;
    }
      public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
   
 
    
    
//    public String getDeviceID() { return deviceID; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getManufacturer() { return manufacturer; }
    public String getAcquisitionDate() { return acquisitionDate; }
    public String getLastMaintenanceDate() { return lastMaintenanceDate; }
    public Room getCurrentLocation() { return currentLocation; }
    public boolean isPortable() { return isPortable; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public HospitalEnterprise getOwningHospital() { return owningHospital; }
    public String getDeviceStatus() { return deviceStatus; }
    public boolean isComponent() { return isComponent; }
    public String getParentDeviceID() { return parentDeviceID; }
    public String getComponentType() { return componentType; }
    public boolean isCoreComponent() { return isCoreComponent; }
    public HospitalEnterprise getRentedToHospital() { return rentedToHospital; }
    public boolean isRentable() { return rentable; }

//    public void setDeviceID(String deviceID) { this.deviceID = deviceID; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setAcquisitionDate(String acquisitionDate) { this.acquisitionDate = acquisitionDate; }
    public void setLastMaintenanceDate(String lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }
    public void setCurrentLocation(Room currentLocation) { this.currentLocation = currentLocation; }
    public void setPortable(boolean isPortable) { this.isPortable = isPortable; }
    public void setQuantity(int quantity) { this.quantity = Math.max(0, quantity); }
    public void setPrice(double price) { this.price = Math.max(0.0, price); }
    public void setOwningHospital(HospitalEnterprise owningHospital) { this.owningHospital = owningHospital; }
    public void setDeviceStatus(String deviceStatus) { this.deviceStatus = deviceStatus; }
    public void setComponent(boolean component) { isComponent = component; }
    public void setParentDeviceID(String parentDeviceID) { this.parentDeviceID = parentDeviceID; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    public void setCoreComponent(boolean coreComponent) { isCoreComponent = coreComponent; }
    public void setRentedToHospital(HospitalEnterprise rentedToHospital) { this.rentedToHospital = rentedToHospital; }
    public void setRentable(boolean rentable) { this.rentable = rentable; }
     public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }


    public boolean isAvailableForRent() {
        boolean statusAllowsRent = "Available".equalsIgnoreCase(deviceStatus) ||
                                   "Rental Component Available".equalsIgnoreCase(deviceStatus);
        return rentable && owningHospital != null && rentedToHospital == null && statusAllowsRent;
    }

    public boolean isAvailableForBooking() {
        boolean statusAllowsBooking = "Available".equalsIgnoreCase(deviceStatus) ||
                                      "Core Component".equalsIgnoreCase(deviceStatus);
        return rentedToHospital == null && statusAllowsBooking;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name != null ? name : "Unnamed Device");
        if (deviceID != null) {
            sb.append(" (ID: ").append(deviceID).append(")");
        }
        if (isComponent && componentType != null) {
            sb.append(" [Component: ").append(componentType).append("]");
        }
        return sb.toString();
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        if (deviceID != null && device.deviceID != null) {
            return deviceID.equals(device.deviceID);
        }

        return isPortable == device.isPortable &&
               quantity == device.quantity &&
               Double.compare(device.price, price) == 0 &&
               isComponent == device.isComponent &&
               isCoreComponent == device.isCoreComponent &&
               rentable == device.rentable &&
               Objects.equals(name, device.name) &&
               Objects.equals(type, device.type) &&
               Objects.equals(manufacturer, device.manufacturer) &&
               Objects.equals(currentLocation, device.currentLocation) &&
               Objects.equals(owningHospital, device.owningHospital) &&
               Objects.equals(deviceStatus, device.deviceStatus) &&
               Objects.equals(parentDeviceID, device.parentDeviceID) &&
               Objects.equals(componentType, device.componentType) &&
               Objects.equals(rentedToHospital, device.rentedToHospital);
    }

    @Override
    public int hashCode() {
        if (deviceID != null) {
            return Objects.hash(deviceID);
        }
        return Objects.hash(name, type, manufacturer, currentLocation, isPortable, quantity, price, owningHospital, deviceStatus, isComponent, parentDeviceID, componentType, isCoreComponent, rentedToHospital, rentable);
    }

     @Deprecated
     public int getAvail() { return getQuantity(); }
     @Deprecated
     public void setAvail(int avail) { setQuantity(avail); }

     @Deprecated
     public String getManifacturer() { return getManufacturer(); }
     
//     @Override
//    public String toString() {
//        return String.valueOf(name);
//    }
}