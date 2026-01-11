package Business.models.bundle;

import Business.models.device.Device;
import Business.models.room.Room;
import Business.Enterprise.HospitalEnterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ResourceBundle {

    private final String bundleId;
    private String name;
    private String surgeryType;
    private ArrayList<Device> requiredDevices;
    private Room requiredRoom;
    private List<String> requiredTeamSpecializations;
    private HospitalEnterprise managingHospital;
    private double estimatedCost;
    private int estimatedDurationHours;

    public ResourceBundle() {
        this.bundleId = "Bundle - " + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.requiredDevices = new ArrayList<>();
        this.requiredTeamSpecializations = new ArrayList<>();
        this.estimatedCost = 0.0;
        this.estimatedDurationHours = 0;
    }

    public String getBundleId() { return bundleId; }
    public String getName() { return name; }
    public String getSurgeryType() { return surgeryType; }
    public Room getRequiredRoom() { return requiredRoom; }
    public List<String> getRequiredTeamSpecializations() { return requiredTeamSpecializations; }
    public HospitalEnterprise getManagingHospital() { return managingHospital; }
    public double getEstimatedCost() { return estimatedCost; }
    public int getEstimatedDurationHours() { return estimatedDurationHours; }

    public void setName(String name) { this.name = name; }
    public void setSurgeryType(String surgeryType) { this.surgeryType = surgeryType; }

    public void setRequiredDevices(ArrayList<Device> devices) {
        this.requiredDevices = new ArrayList<>();
        if (devices != null) {
            for (Device d : devices) {
                this.requiredDevices.add(d);
            }
        }
        System.out.println("ResourceBundle: After deep copy, requiredDevices contains " + 
            this.requiredDevices.size() + " devices");
    }
    
    public ArrayList<Device> getRequiredDevices() {
        System.out.println("ResourceBundle: getRequiredDevices returning " + 
            (this.requiredDevices != null ? this.requiredDevices.size() : "null") + " devices");
        return this.requiredDevices;
    }

    public void setRequiredTeamSpecializations(List<String> requiredTeamSpecializations) {
        this.requiredTeamSpecializations = (requiredTeamSpecializations != null) ? new ArrayList<>(requiredTeamSpecializations) : new ArrayList<>();
    }

    public void setRequiredRoom(Room requiredRoom) { this.requiredRoom = requiredRoom; }
    public void setManagingHospital(HospitalEnterprise managingHospital) { this.managingHospital = managingHospital; }

    public void setEstimatedCost(double estimatedCost) {
        if (estimatedCost >= 0) {
            this.estimatedCost = estimatedCost;
        } else {
             System.err.println("Estimated cost cannot be negative.");
             this.estimatedCost = 0.0;
        }
    }

    public void setEstimatedDurationHours(int estimatedDurationHours) {
        if (estimatedDurationHours >= 0) {
            this.estimatedDurationHours = estimatedDurationHours;
        } else {
             System.err.println("Estimated duration cannot be negative.");
             this.estimatedDurationHours = 0;
        }
    }

    public void addDevice(Device device) {
        if (device != null && !requiredDevices.contains(device)) {
            requiredDevices.add(device);
        }
    }

    public boolean removeDevice(Device device) {
        return requiredDevices.remove(device);
    }

    public void addTeamSpecialization(String specialization) {
        if (specialization != null && !specialization.trim().isEmpty() && !requiredTeamSpecializations.contains(specialization.trim())) {
            requiredTeamSpecializations.add(specialization.trim());
        }
    }

    public boolean removeTeamSpecialization(String specialization) {
        return requiredTeamSpecializations.remove(specialization);
    }

    @Override
    public String toString() {
        return name != null && !name.trim().isEmpty() ? name : bundleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceBundle that = (ResourceBundle) o;
        return Objects.equals(bundleId, that.bundleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bundleId);
    }
}