/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.PHSAdminRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Enterprise.HospitalEnterprise;
import Business.Enterprise.PHSEnterprise;
import Business.Network.Network;
import Business.Organization.*; 
import Business.UserAccount.UserAccount;
import Business.WorkQueue.*; 
import Business.models.bundle.ResourceBundle;
import Business.models.device.Device;
import Business.models.room.Room;

import java.awt.CardLayout;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Administrator
 */
public class CreateResourceBundlesJPanel extends javax.swing.JPanel {

    /**
     * Creates new form CreateResourceBundlesJPanel
     */
    
    private JPanel userProcessContainer;
    private Enterprise enterprise;
    private UserAccount userAccount;
    private EcoSystem business;
    private PHSEnterprise phsEnterprise; 

    private BundleReservationWorkRequest processingRequest;
    private ResourceBundle currentEditingBundle;

    private DefaultTableModel availableDevicesTableModel;
    private DefaultTableModel selectedDevicesTableModel;
    private DefaultTableModel availableRoomsTableModel;
    private DefaultTableModel selectedRoomTableModel;
    private DefaultTableModel availableTeamTypesTableModel;
    private DefaultTableModel selectedTeamTypesTableModel;
    
    private Device nonMovableDevice;

    private final String[] DEVICE_COLUMNS_SIMPLIFIED = {"Name", "Hospital", "Status", "Device Object"};
    private final int DEVICE_OBJECT_COL_INDEX_SIMPLIFIED = 3;

    private final String[] ROOM_COLUMNS_SIMPLIFIED = {"Number", "Floor", "Hospital", "Room Object"};
    private final int ROOM_OBJECT_COL_INDEX_SIMPLIFIED = 3;

    private final String[] TEAM_TYPE_COLUMNS = {"Specialization", "Type Object"};
    private final int TEAM_TYPE_OBJECT_COL_INDEX = 1;

    private final List<String> TEAM_SPECIALIZATIONS = Arrays.asList(
        "Cardiology", "Neurology", "Orthopedics", "General Surgery",
        "Anesthesia", "Radiology", "Robotics Team", "Gynecology", "Nursing Support"
    );
    
    private final double DEVICE_BASE_COST_PER_HOUR = 100.0;
    private final double ROOM_BASE_COST_PER_HOUR = 500.0;
    private final double TEAM_BASE_COST_PER_HOUR = 200.0;
    
    private final double DAVINCI_COST_MULTIPLIER = 3.0;
    private final double ROBOTIC_ROOM_COST_MULTIPLIER = 2.0;
    
    public CreateResourceBundlesJPanel(JPanel userProcessContainer, UserAccount account, Enterprise enterprise, EcoSystem business) {
        this(userProcessContainer, account, enterprise, business, null);
    }
    
    public CreateResourceBundlesJPanel(JPanel userProcessContainer, UserAccount account, Enterprise enterprise, EcoSystem business, BundleReservationWorkRequest requestToProcess) {
        initComponents();
        
        this.userProcessContainer = userProcessContainer;
        this.enterprise = enterprise; 
        this.userAccount = account;
        this.business = business;
        this.processingRequest = requestToProcess;

        if (enterprise instanceof PHSEnterprise) {
            this.phsEnterprise = (PHSEnterprise) enterprise;
        } else {
            JOptionPane.showMessageDialog(this, "Error: Invalid Enterprise type. Expected PHSEnterprise.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            return;
        }

        initializeTableModels();
        configureTables();

        populateAvailableResourceLists();

        if (this.processingRequest != null) {
            populateRequestInfoTable();
            preparePanelForRequestProcessing();
        } else {
            lblTitle.setText("Manage Bundle Templates");
            clearBundleDetails();
            setBundleDetailsEditable(false);
            clearRequestInfo();
        }
        
        setupDurationListener();
    }
    
    private void setupDurationListener() {
        txtEstimatedDuration.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calculateTotalCost();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calculateTotalCost();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calculateTotalCost();
            }
        });
    }
    
    private void populateRequestInfoTable() {
        DefaultTableModel model = (DefaultTableModel) tblGlobalBundles.getModel();
        model.setRowCount(0);

        Object[] row = new Object[5];
        row[0] = processingRequest.getPatientName();
        row[1] = processingRequest.getPatientID();
        row[2] = processingRequest.getSurgeryType();
        row[3] = processingRequest.getRequiredDate();
        
        String deviceInfo = getRequestedDevicesInfo(processingRequest);
        row[4] = deviceInfo;
        model.addRow(row);
    }
    
    private String getRequestedDevicesInfo(BundleReservationWorkRequest request) {
        if (request == null) return "None";

        ResourceBundle doctorRequestBundle = request.getResourceBundle();
        if (doctorRequestBundle == null || doctorRequestBundle.getRequiredDevices() == null 
            || doctorRequestBundle.getRequiredDevices().isEmpty()) {
            return "None";
        }

        StringBuilder devices = new StringBuilder();
        boolean daVinciFound = false;

        // First check if any DaVinci device is present
        for (Device device : doctorRequestBundle.getRequiredDevices()) {
            if (device != null && device.getName().toLowerCase().contains("davinci")) {
                daVinciFound = true;
                break;
            }
        }

        // If DaVinci is found, add all components as a hardcoded string
        if (daVinciFound) {
            devices.append("DaVinci Console, DaVinci Vision Cart, DaVinci Arms Set");
        }

        // Add all non-DaVinci devices
        for (Device device : doctorRequestBundle.getRequiredDevices()) {
            if (device != null && !device.getName().toLowerCase().contains("davinci")) {
                if (devices.length() > 0) {
                    devices.append(", ");
                }
                devices.append(device.getName());
            }
        }

        return devices.length() > 0 ? devices.toString() : "None";
    }
    
    private void initializeTableModels() {
         availableDevicesTableModel = createNonEditableTableModel(DEVICE_COLUMNS_SIMPLIFIED);
         selectedDevicesTableModel = createNonEditableTableModel(DEVICE_COLUMNS_SIMPLIFIED);
         availableRoomsTableModel = createNonEditableTableModel(ROOM_COLUMNS_SIMPLIFIED);
         selectedRoomTableModel = createNonEditableTableModel(ROOM_COLUMNS_SIMPLIFIED);
         availableTeamTypesTableModel = createNonEditableTableModel(TEAM_TYPE_COLUMNS);
         selectedTeamTypesTableModel = createNonEditableTableModel(TEAM_TYPE_COLUMNS);
    }

     private void configureTables() {
        tblAvailDevices.setModel(availableDevicesTableModel);
        tblSelectedDevices.setModel(selectedDevicesTableModel);
        tblAvailRooms.setModel(availableRoomsTableModel);
        tblSelectedRoom.setModel(selectedRoomTableModel);
        tblAvailTeamTypes.setModel(availableTeamTypesTableModel);
        tblSelectedTeamTypes.setModel(selectedTeamTypesTableModel);

        hideTableColumn(tblAvailDevices, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);
        hideTableColumn(tblSelectedDevices, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);
        hideTableColumn(tblAvailRooms, ROOM_OBJECT_COL_INDEX_SIMPLIFIED);
        hideTableColumn(tblSelectedRoom, ROOM_OBJECT_COL_INDEX_SIMPLIFIED);
        hideTableColumn(tblAvailTeamTypes, TEAM_TYPE_OBJECT_COL_INDEX);

        tblAvailDevices.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblSelectedDevices.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblAvailRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSelectedRoom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAvailTeamTypes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblSelectedTeamTypes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private DefaultTableModel createNonEditableTableModel(String[] columns) {
        return new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void hideTableColumn(javax.swing.JTable table, int columnIndex) {
       if (table != null && table.getColumnModel().getColumnCount() > columnIndex && columnIndex >= 0) {
           TableColumn column = table.getColumnModel().getColumn(columnIndex);
           column.setMinWidth(0);
           column.setMaxWidth(0);
           column.setWidth(0);
           column.setPreferredWidth(0);
        } else {
            System.err.println("Error hiding column " + columnIndex + " for table " + (table != null ? table.getName() : "null"));
        }
    }

    private void populateAvailableResourceLists() {

        availableDevicesTableModel.setRowCount(0);
        availableRoomsTableModel.setRowCount(0);
        availableTeamTypesTableModel.setRowCount(0);

        if (business == null) {
            System.err.println("EcoSystem (business) is null. Cannot populate resources.");
            return;
        }

        String requiredRoomType = inferRequiredRoomType();

        for (Network network : business.getNetworkList()) {
            if (network == null || network.getEnterpriseDirectory() == null) continue;
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof HospitalEnterprise) {
                    HospitalEnterprise hospital = (HospitalEnterprise) ent;
                    String hospitalAbbr = getAbbreviatedHospitalName(hospital.getName());

                    if (hospital.getDeviceDirectory() != null) {
                        for (Device device : hospital.getDeviceDirectory().getDeviceList()) {
                            boolean shouldAdd = false;

                            if (device != null) {
                                boolean isAvailable = device.isAvailableForBooking();

                                if (isAvailable) {
                                    shouldAdd = true;
                                }
                            }

                            if (shouldAdd) {
                                Object[] row = new Object[DEVICE_COLUMNS_SIMPLIFIED.length];
                                row[0] = device.getName();
                                row[1] = hospitalAbbr;
                                row[2] = device.getDeviceStatus();
                                row[DEVICE_OBJECT_COL_INDEX_SIMPLIFIED] = device;
                                availableDevicesTableModel.addRow(row);
                            }
                        }
                    }

                    if (hospital.getRoomDirectory() != null) {
                        for (Room room : hospital.getRoomDirectory().getRoomList()) {
                            if (room != null && room.isAvailable() &&
                                (requiredRoomType == null || requiredRoomType.equalsIgnoreCase(room.getRoomType())))
                            {
                                Object[] row = new Object[ROOM_COLUMNS_SIMPLIFIED.length];
                                row[0] = String.valueOf(room.getNumber());
                                row[1] = room.getFloor();
                                row[2] = hospitalAbbr;
                                row[ROOM_OBJECT_COL_INDEX_SIMPLIFIED] = room;
                                availableRoomsTableModel.addRow(row);
                            }
                        }
                    }
                }
            }
        }

        for (String specialization : TEAM_SPECIALIZATIONS) {
             Object[] row = new Object[TEAM_TYPE_COLUMNS.length];
             row[0] = specialization;
             row[TEAM_TYPE_OBJECT_COL_INDEX] = specialization;
             availableTeamTypesTableModel.addRow(row);
        }
    
        nonMovableDevice = findSelectedNonMovableDevice();
        
        if (nonMovableDevice != null) {
            HospitalEnterprise deviceHospital = nonMovableDevice.getOwningHospital();
            if (deviceHospital != null) {

                List<Integer> rowsToRemove = new ArrayList<>();

                for (int i = availableRoomsTableModel.getRowCount() - 1; i >= 0; i--) {
                    Object roomObj = availableRoomsTableModel.getValueAt(i, ROOM_OBJECT_COL_INDEX_SIMPLIFIED);
                    if (roomObj instanceof Room) {
                        Room room = (Room) roomObj;

                        String roomHospitalAbbr = (String) availableRoomsTableModel.getValueAt(i, 2);
                        String deviceHospitalAbbr = getAbbreviatedHospitalName(deviceHospital.getName());

                        if (!deviceHospitalAbbr.equals(roomHospitalAbbr)) {
                            rowsToRemove.add(i);
                        }
                    }
                }

                for (int rowIndex : rowsToRemove) {
                    availableRoomsTableModel.removeRow(rowIndex);
                }
            }
        }
        
        Set<String> requestedParentDevices = new HashSet<>();
    
        // 从请求信息或已选设备中检查是否有请求组件设备
        boolean anyComponentRequested = false;

        // 检查顶部表格是否显示了复杂设备
        for (int row = 0; row < tblGlobalBundles.getModel().getRowCount(); row++) {
            String deviceInfo = (String) tblGlobalBundles.getModel().getValueAt(row, 4);
            if (deviceInfo != null && (
                deviceInfo.toLowerCase().contains("davinci") || 
                deviceInfo.toLowerCase().contains("robot"))) {
                anyComponentRequested = true;
                break;
            }
        }

        // 如果请求了组件设备，加载所有相关组件
        if (anyComponentRequested) {
            // 首先收集所有设备的parentDeviceID
            for (Network network : business.getNetworkList()) {
                for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                    if (ent instanceof HospitalEnterprise) {
                        HospitalEnterprise hospital = (HospitalEnterprise) ent;
                        if (hospital.getDeviceDirectory() != null) {
                            for (Device device : hospital.getDeviceDirectory().getDeviceList()) {
                                if (device.isComponent() && device.getParentDeviceID() != null &&
                                    !device.getParentDeviceID().trim().isEmpty()) {
                                    // 收集所有父设备ID
                                    requestedParentDevices.add(device.getParentDeviceID());
                                }
                            }
                        }
                    }
                }
            }

            // 现在，对于每个父设备ID，加载所有对应的组件
            for (String parentId : requestedParentDevices) {
                System.out.println("Loading all components for parent device ID: " + parentId);

                for (Network network : business.getNetworkList()) {
                    for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                        if (ent instanceof HospitalEnterprise) {
                            HospitalEnterprise hospital = (HospitalEnterprise) ent;
                            String hospitalAbbr = getAbbreviatedHospitalName(hospital.getName());

                            if (hospital.getDeviceDirectory() != null) {
                                for (Device device : hospital.getDeviceDirectory().getDeviceList()) {
                                    if (device.isComponent() && 
                                        parentId.equals(device.getParentDeviceID())) {

                                        // 检查是否已在表格中
                                        boolean alreadyAdded = false;
                                        for (int i = 0; i < availableDevicesTableModel.getRowCount(); i++) {
                                            Device existingDevice = (Device) availableDevicesTableModel.getValueAt(
                                                i, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);
                                            if (existingDevice != null && existingDevice.equals(device)) {
                                                alreadyAdded = true;
                                                break;
                                            }
                                        }

                                        // 如果未添加，则添加
                                        if (!alreadyAdded) {
                                            Object[] row = new Object[DEVICE_COLUMNS_SIMPLIFIED.length];
                                            row[0] = device.getName();
                                            row[1] = hospitalAbbr;
                                            row[2] = device.getDeviceStatus();
                                            row[DEVICE_OBJECT_COL_INDEX_SIMPLIFIED] = device;
                                            availableDevicesTableModel.addRow(row);
                                            System.out.println("Added component: " + device.getName() + 
                                                             " for parent " + parentId + 
                                                             " from " + hospitalAbbr);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private Device findSelectedNonMovableDevice() {
        if (selectedDevicesTableModel == null) {
            return null;
        }

        for (int i = 0; i < selectedDevicesTableModel.getRowCount(); i++) {
            if (i < 0 || selectedDevicesTableModel.getRowCount() <= i || 
                DEVICE_OBJECT_COL_INDEX_SIMPLIFIED >= selectedDevicesTableModel.getColumnCount()) {
                continue;
            }

            Object deviceObj = selectedDevicesTableModel.getValueAt(i, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);
            if (deviceObj instanceof Device) {
                Device device = (Device) deviceObj;
                if (device != null && !device.isPortable()) {
                    return device;
                }
            }
        }
        return null;
    }

    private void preparePanelForRequestProcessing() {
        if (processingRequest == null) {
            System.err.println("preparePanelForRequestProcessing called with null request.");
            return;
        }

        String patientName = processingRequest.getPatientName() != null ? processingRequest.getPatientName() : "N/A";
        String patientID = processingRequest.getPatientID() != null ? processingRequest.getPatientID() : "N/A";
        String surgeryTypeReq = processingRequest.getSurgeryType() != null ? processingRequest.getSurgeryType() : "N/A";
        String requiredDateReq = processingRequest.getRequiredDate() != null ? processingRequest.getRequiredDate() : "N/A";
        String statusReq = processingRequest.getStatus() != null ? processingRequest.getStatusString() : "N/A";

        // Initialize the form fields
        clearBundleDetails();
        currentEditingBundle = new ResourceBundle();

        // Set the bundle name to be more user-friendly 
        txtBundleName.setText("Bundle - " + processingRequest.getPatientName() + " - " + processingRequest.getSurgeryType());
        txtEstimatedCost.setText("");
        txtEstimatedDuration.setText("");

        // Populate available resources but don't preselect them
        populateAvailableResourceLists();

        setBundleDetailsEditable(true);
    }


    private boolean isRoomInAvailableTable(Room roomToCheck) {
        for (int i = 0; i < availableRoomsTableModel.getRowCount(); i++) {
             Object roomObj = availableRoomsTableModel.getValueAt(i, ROOM_OBJECT_COL_INDEX_SIMPLIFIED);
             if (roomToCheck.equals(roomObj)) {
                 return true;
             }
        }
        return false;
    }

    private Component findComponentByName(String name) {
        for (Component comp : this.getComponents()) {
             if (name.equals(comp.getName())) {
                  return comp;
             }
        }
        return null;
    }


    private void clearBundleDetails() {
        currentEditingBundle = null;
        txtBundleName.setText("");
        txtEstimatedCost.setText("");
        txtEstimatedDuration.setText("");
        selectedDevicesTableModel.setRowCount(0);
        selectedRoomTableModel.setRowCount(0);
        selectedTeamTypesTableModel.setRowCount(0);
        populateAvailableResourceLists();
    }

    private void clearRequestInfo() {

         if (findComponentByName("lblRequestPatientNameValue") instanceof javax.swing.JLabel) ((javax.swing.JLabel)findComponentByName("lblRequestPatientNameValue")).setText("N/A");
         if (findComponentByName("lblRequestPatientIDValue") instanceof javax.swing.JLabel) ((javax.swing.JLabel)findComponentByName("lblRequestPatientIDValue")).setText("N/A");
         if (findComponentByName("lblRequestSurgeryTypeValue") instanceof javax.swing.JLabel) ((javax.swing.JLabel)findComponentByName("lblRequestSurgeryTypeValue")).setText("N/A");
         if (findComponentByName("lblRequestDateValue") instanceof javax.swing.JLabel) ((javax.swing.JLabel)findComponentByName("lblRequestDateValue")).setText("N/A");
         if (findComponentByName("lblRequestStatusValue") instanceof javax.swing.JLabel) ((javax.swing.JLabel)findComponentByName("lblRequestStatusValue")).setText("N/A");
    }


     private void setBundleDetailsEditable(boolean editable) {

        txtBundleName.setEditable(editable);
        txtEstimatedCost.setEditable(editable);
        txtEstimatedDuration.setEditable(editable);

        tblAvailDevices.setEnabled(editable);
        tblSelectedDevices.setEnabled(editable);
        btnAddDevice.setEnabled(editable);
        btnRemoveDevice.setEnabled(editable);

        tblAvailRooms.setEnabled(editable);
        tblSelectedRoom.setEnabled(editable);
        btnAddRoom.setEnabled(editable);
        btnRemoveRoom.setEnabled(editable);

        tblAvailTeamTypes.setEnabled(editable);
        tblSelectedTeamTypes.setEnabled(editable);
        btnAddTeamType.setEnabled(editable);
        btnRemoveTeamType.setEnabled(editable);

        btnSave.setEnabled(editable);
        btnCancel.setEnabled(editable);

    }

    private String getAbbreviatedHospitalName(String fullName) {
        if (fullName == null) return "N/A";
        if (fullName.contains("Massachusetts General")) return "MGH";
        if (fullName.contains("Western Massachusetts")) return "WMH";
        String[] parts = fullName.split(" ");
        return parts.length > 0 ? parts[0] : fullName;
    }

    private String getAbbreviatedHospitalName(Room room) {
         String hospitalName = findRoomHospitalName(room);
         return getAbbreviatedHospitalName(hospitalName);
    }

    private String getAbbreviatedHospitalName(Device device) {
         if (device != null && device.getOwningHospital() != null) {
             return getAbbreviatedHospitalName(device.getOwningHospital().getName());
         }
         return "N/A";
    }

    private String findRoomHospitalName(Room roomToFind) {
        if (business == null || roomToFind == null) return "Unknown";
        for (Network network : business.getNetworkList()) {
            if (network == null || network.getEnterpriseDirectory() == null) continue;
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof HospitalEnterprise) {
                    HospitalEnterprise hospital = (HospitalEnterprise) ent;
                    if (hospital.getRoomDirectory() != null) {
                        for (Room room : hospital.getRoomDirectory().getRoomList()) {
                            if (roomToFind.equals(room)) {
                                return hospital.getName();
                            }
                        }
                    }
                }
            }
        }
        return "Unknown";
    }


    private String inferRequiredRoomType() {
        String inferredType = null;
        boolean requiresRobotic = false;
        boolean requiresImaging = false;

        for (int i = 0; i < selectedDevicesTableModel.getRowCount(); i++) {

            Device device = (Device) selectedDevicesTableModel.getValueAt(i, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);
            if (device != null) {
                String deviceType = device.getType();
                String deviceName = device.getName();
                if (deviceName != null && (deviceName.toLowerCase().contains("davinci") || deviceName.toLowerCase().contains("robot"))) {
                    requiresRobotic = true;
                    break;
                }
                if (deviceType != null && deviceType.equalsIgnoreCase("Imaging")) {
                     requiresImaging = true;
                }
            }
        }

        if (requiresRobotic) {
            inferredType = "Robotic OR";
        } else if (requiresImaging) {
            inferredType = "Imaging Room";
        } else if (selectedDevicesTableModel.getRowCount() > 0) {
            inferredType = "General OR";
        } else {
             inferredType = null;
        }
        return inferredType;
    }


     private <T> void addResourceToSelectedTable(javax.swing.JTable availableTable, DefaultTableModel selectedModel, T resourceHint, int objectColumnIndex) {
        if (resourceHint == null) return;

        int rowToAdd = -1;
        for (int i = 0; i < availableTable.getRowCount(); i++) {
            Object objInTable = availableTable.getValueAt(i, objectColumnIndex);
            if (objInTable != null && objInTable.equals(resourceHint)) {
                rowToAdd = i;
                break;
            }
        }

        if (rowToAdd != -1) {
            boolean alreadyAdded = false;
            for (int i = 0; i < selectedModel.getRowCount(); i++) {
                if (selectedModel.getValueAt(i, objectColumnIndex) != null &&
                    selectedModel.getValueAt(i, objectColumnIndex).equals(resourceHint)) {
                    alreadyAdded = true;
                    break;
                }
            }

            if (!alreadyAdded) {
                 Object[] rowData = new Object[selectedModel.getColumnCount()];
                 boolean rowBuilt = false;

                 if (resourceHint instanceof Device && selectedModel == selectedDevicesTableModel) {
                      Device d = (Device) resourceHint;
                      rowData[0] = d.getName();
                      rowData[1] = getAbbreviatedHospitalName(d);
                      rowData[2] = d.getDeviceStatus();
                      rowData[objectColumnIndex] = d;
                      rowBuilt = true;
                 } else if (resourceHint instanceof Room && selectedModel == selectedRoomTableModel) {
                      Room r = (Room) resourceHint;
                      rowData[0] = String.valueOf(r.getNumber());
                      rowData[1] = r.getFloor();
                      rowData[2] = getAbbreviatedHospitalName(r);
                      rowData[objectColumnIndex] = r;
                      rowBuilt = true;
                 } else if (resourceHint instanceof String && selectedModel == selectedTeamTypesTableModel) {
                      String spec = (String) resourceHint;
                      rowData[0] = spec;
                      rowData[objectColumnIndex] = spec;
                      rowBuilt = true;
                 }

                if(rowBuilt) {
                    selectedModel.addRow(rowData);
                } else {
                    System.err.println("Mismatch between resource type and target table model during auto-add.");
                }
            } else {
            }
        } else {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBundleName = new javax.swing.JLabel();
        lblEstimatedCost = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblSelectedRoom = new javax.swing.JTable();
        lblEstimatedDuration = new javax.swing.JLabel();
        txtBundleName = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblAvailTeamTypes = new javax.swing.JTable();
        txtEstimatedDuration = new javax.swing.JTextField();
        txtEstimatedCost = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblSelectedTeamTypes = new javax.swing.JTable();
        lblDevice = new javax.swing.JLabel();
        lblOperationRoom = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblMedicalTeamType = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAddDevice = new javax.swing.JButton();
        btnRemoveDevice = new javax.swing.JButton();
        btnAddRoom = new javax.swing.JButton();
        btnRemoveRoom = new javax.swing.JButton();
        btnAddTeamType = new javax.swing.JButton();
        btnRemoveTeamType = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAvailDevices = new javax.swing.JTable();
        lblTitle = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSelectedDevices = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGlobalBundles = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblAvailRooms = new javax.swing.JTable();

        lblBundleName.setText("Bundle Name:");

        lblEstimatedCost.setText("Estimated Cost ($):");

        tblSelectedRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Hospital", "Status", "Object"
            }
        ));
        jScrollPane5.setViewportView(tblSelectedRoom);

        lblEstimatedDuration.setText("Estimated Duration (hours):");

        tblAvailTeamTypes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Specialization", "Type Object"
            }
        ));
        jScrollPane6.setViewportView(tblAvailTeamTypes);

        tblSelectedTeamTypes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Hospital", "Status", "Object"
            }
        ));
        jScrollPane7.setViewportView(tblSelectedTeamTypes);

        lblDevice.setText("Available Device:");

        lblOperationRoom.setText("Available Operation Room:");

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        jLabel1.setText("Create a new Bundle:");

        lblMedicalTeamType.setText("Medical Team Type");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnAddDevice.setText(">>");
        btnAddDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDeviceActionPerformed(evt);
            }
        });

        btnRemoveDevice.setText("<<");
        btnRemoveDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveDeviceActionPerformed(evt);
            }
        });

        btnAddRoom.setText(">>");
        btnAddRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRoomActionPerformed(evt);
            }
        });

        btnRemoveRoom.setText("<<");
        btnRemoveRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveRoomActionPerformed(evt);
            }
        });

        btnAddTeamType.setText(">>");
        btnAddTeamType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTeamTypeActionPerformed(evt);
            }
        });

        btnRemoveTeamType.setText("<<");
        btnRemoveTeamType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveTeamTypeActionPerformed(evt);
            }
        });

        tblAvailDevices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Hospital", "Object", "Status"
            }
        ));
        jScrollPane2.setViewportView(tblAvailDevices);

        lblTitle.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        lblTitle.setText("Create Resource Bundle");

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        tblSelectedDevices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Hospital", "Status", "Object"
            }
        ));
        jScrollPane3.setViewportView(tblSelectedDevices);

        tblGlobalBundles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Patient Name", "Patient ID", "Surgery Type", "Required Date", "Required Device(s)"
            }
        ));
        jScrollPane1.setViewportView(tblGlobalBundles);

        tblAvailRooms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Hospital", "Floor", "Object", "Status"
            }
        ));
        jScrollPane4.setViewportView(tblAvailRooms);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(btnBack)
                        .addGap(105, 105, 105)
                        .addComponent(lblTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAddDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnRemoveDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblDevice)
                            .addComponent(lblOperationRoom)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnRemoveRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblMedicalTeamType)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnSave)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(lblEstimatedDuration)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtEstimatedDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnRemoveTeamType, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAddTeamType, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblEstimatedCost)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtEstimatedCost, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnCancel)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblBundleName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBundleName))
                            .addComponent(jScrollPane1))))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(40, 40, 40))
                    .addComponent(lblTitle, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBundleName)
                    .addComponent(txtBundleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDevice)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnAddDevice)
                        .addGap(9, 9, 9)
                        .addComponent(btnRemoveDevice)
                        .addGap(26, 26, 26))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addComponent(lblOperationRoom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnAddRoom)
                        .addGap(9, 9, 9)
                        .addComponent(btnRemoveRoom))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMedicalTeamType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(btnAddTeamType)
                        .addGap(9, 9, 9)
                        .addComponent(btnRemoveTeamType))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEstimatedDuration)
                    .addComponent(txtEstimatedDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEstimatedCost)
                    .addComponent(txtEstimatedCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         if (currentEditingBundle == null) {
            JOptionPane.showMessageDialog(this, "Cannot save, no bundle is being edited or created.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bundleName = txtBundleName.getText().trim();
        String surgeryType = currentEditingBundle.getSurgeryType();

        if (bundleName.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Bundle Name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
             txtBundleName.requestFocus();
             return;
        }
        double estCost;
        int estDuration;
        try {
             estCost = Double.parseDouble(txtEstimatedCost.getText().trim());
             if (estCost < 0) throw new NumberFormatException("Cost cannot be negative");
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Invalid Estimated Cost: Please enter a non-negative number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
             txtEstimatedCost.requestFocus();
             return;
        }
        try {
             estDuration = Integer.parseInt(txtEstimatedDuration.getText().trim());
              if (estDuration <= 0) throw new NumberFormatException("Duration must be positive");
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Invalid Estimated Duration: Please enter a positive whole number of hours.", "Validation Error", JOptionPane.ERROR_MESSAGE);
             txtEstimatedDuration.requestFocus();
             return;
        }

        if (selectedDevicesTableModel.getRowCount() == 0 &&
            selectedRoomTableModel.getRowCount() == 0 &&
            selectedTeamTypesTableModel.getRowCount() == 0) {
             JOptionPane.showMessageDialog(this, "A bundle must contain at least one resource (Device, Room, or Team Type).", "Validation Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
         if (selectedRoomTableModel.getRowCount() > 1) {
              JOptionPane.showMessageDialog(this, "A bundle can only contain one Operation Room.", "Validation Error", JOptionPane.ERROR_MESSAGE);
              return;
         }

        currentEditingBundle.setName(bundleName);
        currentEditingBundle.setEstimatedCost(estCost);
        currentEditingBundle.setEstimatedDurationHours(estDuration);

        List<Device> devices = new ArrayList<>();
        for (int i=0; i < selectedDevicesTableModel.getRowCount(); i++) {
            devices.add((Device) selectedDevicesTableModel.getValueAt(i, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED));
        }
        currentEditingBundle.setRequiredDevices((ArrayList<Device>) devices);

        if (selectedRoomTableModel.getRowCount() > 0) {
            currentEditingBundle.setRequiredRoom((Room) selectedRoomTableModel.getValueAt(0, ROOM_OBJECT_COL_INDEX_SIMPLIFIED));
        } else {
            currentEditingBundle.setRequiredRoom(null);
        }

        List<String> teamTypes = new ArrayList<>();
        for (int i=0; i < selectedTeamTypesTableModel.getRowCount(); i++) {
            teamTypes.add((String) selectedTeamTypesTableModel.getValueAt(i, TEAM_TYPE_OBJECT_COL_INDEX));
        }
        currentEditingBundle.setRequiredTeamSpecializations(teamTypes);

        if (this.processingRequest != null) {

            this.processingRequest.setResourceBundle(currentEditingBundle);

            boolean success = sendApprovalRequests(currentEditingBundle, this.processingRequest);

            if (success) {
                this.processingRequest.setStatusString("Bundle Created - Pending Approvals");

                this.processingRequest.checkAndFinalizeStatus();

                JOptionPane.showMessageDialog(this, "Bundle creation complete for request " + bundleName + ".\n" + "Component requests have been sent out for approval/assignment.", "Bundle Created", JOptionPane.INFORMATION_MESSAGE);

                btnBackActionPerformed(null);
            } else {
                 JOptionPane.showMessageDialog(this, "Bundle created, but failed to send out some component requests.\nPlease review the bundle and system configuration, then try saving again.", "Error Sending Requests", JOptionPane.ERROR_MESSAGE);
                 setBundleDetailsEditable(true);
            }

        } else {
             if (phsEnterprise != null && phsEnterprise.getGlobalBundleDirectory() != null) {
                  phsEnterprise.getGlobalBundleDirectory().addBundle(currentEditingBundle);
                  System.out.println("Saved bundle template: " + currentEditingBundle.getName());
                  JOptionPane.showMessageDialog(this, "Bundle template saved successfully.");
                  setBundleDetailsEditable(false);
                  clearBundleDetails();
                  clearRequestInfo();
             } else {
                   JOptionPane.showMessageDialog(this, "Error: Could not save bundle template. PHS Enterprise data missing.", "Save Error", JOptionPane.ERROR_MESSAGE);
             }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

         if (this.processingRequest != null) {
             int confirm = JOptionPane.showConfirmDialog(this, "Discard changes and go back?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
             if (confirm == JOptionPane.YES_OPTION) {
                 btnBackActionPerformed(null);
             }
         } else {

             clearBundleDetails();
             setBundleDetailsEditable(false);
         }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAddDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDeviceActionPerformed
        int[] selectedRows = tblAvailDevices.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select device(s) from the 'Available Devices' list.");
            return;
        }
        boolean added = false;
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int selectedRowIndex = selectedRows[i];
            if (selectedRowIndex < 0 || selectedRowIndex >= availableDevicesTableModel.getRowCount()) continue;

            Device deviceToAdd = (Device) availableDevicesTableModel.getValueAt(selectedRowIndex, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);

            if (addResourceToSelectedTableModel(selectedDevicesTableModel, deviceToAdd, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED,
                availableDevicesTableModel.getValueAt(selectedRowIndex, 0),
                availableDevicesTableModel.getValueAt(selectedRowIndex, 1), 
                availableDevicesTableModel.getValueAt(selectedRowIndex, 2)
            )) {
                availableDevicesTableModel.removeRow(selectedRowIndex);
                added = true;
            }
        }
        if (added) {
            updateAvailableRooms();
            calculateTotalCost();
        }
    }//GEN-LAST:event_btnAddDeviceActionPerformed

    private void btnRemoveDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveDeviceActionPerformed
        int[] selectedRows = tblSelectedDevices.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select device(s) from the 'Available Devices' list.");
            return;
        }

        boolean removed = false;
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int rowIndex = selectedRows[i];
            if (rowIndex >= 0 && rowIndex < selectedDevicesTableModel.getRowCount()) {

                Device deviceToRemove = (Device) selectedDevicesTableModel.getValueAt(rowIndex, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);

                Object[] row = new Object[DEVICE_COLUMNS_SIMPLIFIED.length];
                row[0] = selectedDevicesTableModel.getValueAt(rowIndex, 0);
                row[1] = selectedDevicesTableModel.getValueAt(rowIndex, 1);
                row[2] = selectedDevicesTableModel.getValueAt(rowIndex, 2); 
                row[DEVICE_OBJECT_COL_INDEX_SIMPLIFIED] = deviceToRemove; 
                availableDevicesTableModel.addRow(row);

                selectedDevicesTableModel.removeRow(rowIndex);
                removed = true;
            }
        }

        if (removed) {
            updateAvailableRooms();
        }
    }//GEN-LAST:event_btnRemoveDeviceActionPerformed

    private void btnAddRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRoomActionPerformed
        int selectedRow = tblAvailRooms.getSelectedRow();
          if (selectedRow < 0) {
              JOptionPane.showMessageDialog(this, "Please select a room from the 'Available Operation Room' list.");
              return;
          }
          if (selectedRoomTableModel.getRowCount() > 0) {
              int replace = JOptionPane.showConfirmDialog(this,
                  "Only one room can be added to a bundle. Replace the current selection?",
                  "Replace Selected Room?", JOptionPane.YES_NO_OPTION);
              if (replace == JOptionPane.NO_OPTION) {
                  return;
              }
              selectedRoomTableModel.setRowCount(0); 
          }

          Room roomToAdd = (Room) availableRoomsTableModel.getValueAt(selectedRow, ROOM_OBJECT_COL_INDEX_SIMPLIFIED);
          addResourceToSelectedTableModel(selectedRoomTableModel, roomToAdd, ROOM_OBJECT_COL_INDEX_SIMPLIFIED,
              availableRoomsTableModel.getValueAt(selectedRow, 0),
              availableRoomsTableModel.getValueAt(selectedRow, 1),
              availableRoomsTableModel.getValueAt(selectedRow, 2)
          );
    }//GEN-LAST:event_btnAddRoomActionPerformed

    private void btnRemoveRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveRoomActionPerformed
        if (selectedRoomTableModel.getRowCount() > 0) {
             selectedRoomTableModel.setRowCount(0); 
          } else {
              JOptionPane.showMessageDialog(this, "No room selected to remove.");
          }
    }//GEN-LAST:event_btnRemoveRoomActionPerformed

    private void btnAddTeamTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTeamTypeActionPerformed
        int[] selectedRows = tblAvailTeamTypes.getSelectedRows();
          if (selectedRows.length == 0) {
              JOptionPane.showMessageDialog(this, "Please select team type(s) from the 'Available Medical Team Type' list.");
              return;
          }
          for (int selectedRowIndex : selectedRows) {
               if (selectedRowIndex < 0 || selectedRowIndex >= availableTeamTypesTableModel.getRowCount()) continue;
              String specToAdd = (String) availableTeamTypesTableModel.getValueAt(selectedRowIndex, TEAM_TYPE_OBJECT_COL_INDEX);
              addResourceToSelectedTableModel(selectedTeamTypesTableModel, specToAdd, TEAM_TYPE_OBJECT_COL_INDEX,
                  availableTeamTypesTableModel.getValueAt(selectedRowIndex, 0) 
              );
          }
    }//GEN-LAST:event_btnAddTeamTypeActionPerformed

    private void btnRemoveTeamTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveTeamTypeActionPerformed
        int[] selectedRows = tblSelectedTeamTypes.getSelectedRows();
           if (selectedRows.length == 0) {
               JOptionPane.showMessageDialog(this, "Please select team type(s) from the 'Selected' list.");
               return;
           }
           for (int i = selectedRows.length - 1; i >= 0; i--) {
               int rowIndex = selectedRows[i];
               if (rowIndex >= 0 && rowIndex < selectedTeamTypesTableModel.getRowCount()) {
                   selectedTeamTypesTableModel.removeRow(rowIndex);
               }
           }
    }//GEN-LAST:event_btnRemoveTeamTypeActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        userProcessContainer.remove(this); 

        try {
            Component[] componentArray = userProcessContainer.getComponents();
            if (componentArray.length > 0) {
                Component component = componentArray[componentArray.length - 1];
                if (component instanceof PHSAdminWorkAreaJPanel) {
                    PHSAdminWorkAreaJPanel prevPanel = (PHSAdminWorkAreaJPanel) component;

                    if (prevPanel != null && prevPanel.getOrganization() != null) {
                        prevPanel.populateTable();
                    } else {
                        System.err.println("Could not refresh previous panel: Panel or its organization is null.");
                    }

                } else {

                }
            } else {

            }
        } catch (Exception e) {
            System.err.println("Error refreshing previous panel: " + e.getMessage());
            e.printStackTrace();
        }

        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed

    private <T> boolean addResourceToSelectedTableModel(DefaultTableModel selectedModel, T resourceToAdd, int objectColIndex, Object... visibleColumnData) {
           if (resourceToAdd == null) return false;

           for (int i = 0; i < selectedModel.getRowCount(); i++) {
               if (selectedModel.getValueAt(i, objectColIndex) != null &&
                   selectedModel.getValueAt(i, objectColIndex).equals(resourceToAdd)) {
                   return false; 
               }
           }

           Object[] rowData = new Object[selectedModel.getColumnCount()];
           for(int i=0; i < visibleColumnData.length; i++){
                if(i < objectColIndex) rowData[i] = visibleColumnData[i];
                else rowData[i+1] = visibleColumnData[i];
           }
           rowData[objectColIndex] = resourceToAdd;
           selectedModel.addRow(rowData);
           return true;
      }
    
    private boolean sendApprovalRequests(ResourceBundle bundle, BundleReservationWorkRequest parentRequest) {
        if (bundle == null || parentRequest == null) {
            System.err.println("Cannot send approvals: Bundle or Parent Request is null.");
            return false;
        }

        int deviceApprovalsNeeded = 0;
        int transferRequestsNeeded = 0;
        boolean roomApprovalNeeded = false;
        int teamApprovalsNeeded = 0;
        boolean allRequestsSent = true;

        HospitalEnterprise targetHospital = findHospitalForRequest(parentRequest);
        if (targetHospital == null) {
            System.err.println("CRITICAL ERROR: Cannot determine target hospital for Bundle Request " + 
                               parentRequest.getBundleIdFromTemplate());
            JOptionPane.showMessageDialog(this, 
                "Error: Could not determine the requesting hospital. Cannot send component requests.", 
                "Configuration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int deviceResult = sendDeviceRequests(bundle, parentRequest, targetHospital);
        if (deviceResult >= 0) {
            deviceApprovalsNeeded = deviceResult;
        } else {
            allRequestsSent = false;
        }

        int transferResult = sendTransferRequests(bundle, parentRequest, targetHospital);
        if (transferResult >= 0) {
            transferRequestsNeeded = transferResult;
        } else {
            allRequestsSent = false;
        }

        if (bundle.getRequiredRoom() != null) {
            roomApprovalNeeded = true;
            if (!sendRoomRequest(bundle, parentRequest, targetHospital)) {
                allRequestsSent = false;
            }
        }

        int teamResult = sendTeamRequests(bundle, parentRequest);
        if (teamResult >= 0) {
            teamApprovalsNeeded = teamResult;
        } else {
            allRequestsSent = false;
        }

        parentRequest.setDeviceApprovalsNeeded(deviceApprovalsNeeded);
        parentRequest.setTransferRequestsNeeded(transferRequestsNeeded);
        parentRequest.setRoomApprovalNeeded(roomApprovalNeeded);
        parentRequest.setTeamApprovalsNeeded(teamApprovalsNeeded);

        return allRequestsSent;
    }

    private int sendDeviceRequests(ResourceBundle bundle, BundleReservationWorkRequest parentRequest, HospitalEnterprise targetHospital) {
        int deviceRequestsSent = 0;

        if (bundle.getRequiredDevices() == null) {
            return 0; 
        }

        for (Device device : bundle.getRequiredDevices()) {
            if (device == null) continue;

            HospitalEnterprise owningHospital = device.getOwningHospital();

            if (owningHospital == null) {
                System.err.println("Device " + device.getName() + " has no owning hospital, cannot send request");
                continue;
            }

            DeviceReservationWorkRequest devRequest = new DeviceReservationWorkRequest();
            devRequest.setSender(this.userAccount); 
            devRequest.setDevice(device);
            devRequest.setStatusString("Pending Device Manager Approval");
            devRequest.setMessage("ParentBundleID:" + parentRequest.getBundleIdFromTemplate());
            devRequest.setScheduleDate(parentRequest.getRequiredDate());

            Organization deviceOrg = findOrgInEnterprise(owningHospital, DeviceManagementOrganization.class);
            if (deviceOrg != null) {
                deviceOrg.getWorkQueue().getWorkRequestList().add(devRequest);
                deviceRequestsSent++;
                System.out.println("Sent device request for: " + device.getName() + " to " + owningHospital.getName());
            } else {
                System.err.println("Could not find Device Management Organization in hospital " + owningHospital.getName());
                return -1;
            }
        }

        return deviceRequestsSent;
    }

    /**
     * Process transfer requests (devices from other hospitals)
     */
    private int sendTransferRequests(ResourceBundle bundle, BundleReservationWorkRequest parentRequest, 
                                    HospitalEnterprise targetHospital) {
        int transferRequestsSent = 0;

        if (bundle.getRequiredDevices() == null) {
            return 0;
        }

        for (Device device : bundle.getRequiredDevices()) {
            if (device == null) continue;

            HospitalEnterprise owningHospital = device.getOwningHospital();


            boolean needsTransfer = owningHospital != null && 
                                   !owningHospital.equals(targetHospital) && 
                                   (device.isComponent() || device.isRentable());

            if (!needsTransfer) continue;

            InterHospitalDeviceTransferRequest transferRequest = new InterHospitalDeviceTransferRequest();
            transferRequest.setSender(this.userAccount);
            transferRequest.setDevice(device);
            transferRequest.setOwningHospital(owningHospital);
            transferRequest.setRequestingHospital(targetHospital);
            transferRequest.setJustification("Required for Bundle: " + parentRequest.getBundleIdFromTemplate());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date reqDate = sdf.parse(parentRequest.getRequiredDate());
                transferRequest.setRequiredFrom(reqDate);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(reqDate);
                calendar.add(Calendar.DAY_OF_YEAR, 3);
                transferRequest.setRequiredUntil(calendar.getTime());

            } catch (ParseException e) {
                System.err.println("Could not parse date '" + parentRequest.getRequiredDate() + "' for transfer request");
            }

            Organization owningDeviceOrg = findOrgInEnterprise(owningHospital, DeviceManagementOrganization.class);
            if (owningDeviceOrg != null) {
                transferRequest.setStatusString("Pending Owner Approval");
                owningDeviceOrg.getWorkQueue().getWorkRequestList().add(transferRequest);
                parentRequest.addTransferRequest(transferRequest);
                transferRequestsSent++;
            } else {
                System.err.println("Could not find Device Management Organization in hospital " + owningHospital.getName());
                return -1;
            }
        }

        return transferRequestsSent;
    }

    /**
     * Process room request
     */
    private boolean sendRoomRequest(ResourceBundle bundle, BundleReservationWorkRequest parentRequest, 
                                   HospitalEnterprise targetHospital) {
        Room room = bundle.getRequiredRoom();
        if (room == null) {
            return true;
        }

        OperationsRoomReservationWorkRequest roomRequest = new OperationsRoomReservationWorkRequest();
        roomRequest.setSender(this.userAccount);
        roomRequest.setRoom(room);
        roomRequest.setStatusString("Pending Ops Manager Approval");
        roomRequest.setMessage("ParentBundleID:" + parentRequest.getBundleIdFromTemplate());
        roomRequest.setScheduleDate(parentRequest.getRequiredDate());

        roomRequest.setDuration(String.valueOf(bundle.getEstimatedDurationHours()));

        Organization opsOrg = findOrgInEnterprise(targetHospital, OperationsManagementOrganization.class);
        if (opsOrg != null) {
            opsOrg.getWorkQueue().getWorkRequestList().add(roomRequest);
            return true;
        } else {
            System.err.println("Could not find Operations Management Organization in hospital " + targetHospital.getName());
            return false;
        }
    }

    /**
     * Process team requests
     */
    private int sendTeamRequests(ResourceBundle bundle, BundleReservationWorkRequest parentRequest) {
        int teamRequestsSent = 0;

        // Find PHS Enterprise
        PHSEnterprise phsEnt = null;
        for (Network network : business.getNetworkList()) {
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof PHSEnterprise) {
                    phsEnt = (PHSEnterprise) ent;
                    break;
                }
            }
            if (phsEnt != null) break;
        }

        if (phsEnt == null) {
            System.err.println("Cannot send team requests: PHS Enterprise not found");
            return -1;
        }

        WorkQueue sharedTeamQueue = phsEnt.getSharedTeamAssignmentWorkQueue();
        if (sharedTeamQueue == null) {
            System.err.println("Cannot send team requests: Shared team assignment work queue is null");
            return -1;
        }

        // REMOVE THIS LINE - it uses teamRequest before it's defined
        // sharedTeamQueue.getWorkRequestList().add(teamRequest);

        if (bundle.getRequiredTeamSpecializations() == null || bundle.getRequiredTeamSpecializations().isEmpty()) {
            return 0;
        }

        Date requiredDateObj = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            requiredDateObj = sdf.parse(parentRequest.getRequiredDate());
        } catch (Exception e) {
            System.err.println("Could not parse date for team request: " + parentRequest.getRequiredDate());
            return -1;
        }

        for (String specialization : bundle.getRequiredTeamSpecializations()) {
            TeamAssignmentWorkRequest teamRequest = new TeamAssignmentWorkRequest();
            teamRequest.setSender(this.userAccount);
            teamRequest.setRequiredSpecialization(specialization.trim());
            teamRequest.setRequiredDateTime(requiredDateObj);
            teamRequest.setStatusString("Pending Assignment");
            teamRequest.setParentBundleRequestId(parentRequest.getBundleIdFromTemplate());
            teamRequest.setMessage("Requesting team for Bundle: " + parentRequest.getBundleIdFromTemplate());

            // Add request to shared team queue
            sharedTeamQueue.getWorkRequestList().add(teamRequest);
            System.out.println("Sent team request for: " + specialization + " to shared team assignment queue");
            teamRequestsSent++;
        }

        return teamRequestsSent;
    }

    /**
     * Find the hospital for a request
     */
    private HospitalEnterprise findHospitalForRequest(BundleReservationWorkRequest request) {
        UserAccount sender = request.getSender();
        if (sender == null || business == null) return null;

        for (Network network : business.getNetworkList()) {
            for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {

                for (UserAccount ua : enterprise.getUserAccountDirectory().getUserAccountList()) {
                    if (ua.equals(sender) && enterprise instanceof HospitalEnterprise) {
                        return (HospitalEnterprise) enterprise;
                    }
                }

                for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
                    for (UserAccount ua : org.getUserAccountDirectory().getUserAccountList()) {
                        if (ua.equals(sender) && enterprise instanceof HospitalEnterprise) {
                            return (HospitalEnterprise) enterprise;
                        }
                    }
                }
            }
        }

        System.err.println("Could not find originating hospital for user: " + sender.getUsername());
        return null;
    }

    /**
     * Find an organization of specific type in an enterprise
     */
    private Organization findOrgInEnterprise(Enterprise enterprise, Class<? extends Organization> orgClass) {
        if (enterprise == null || enterprise.getOrganizationDirectory() == null || orgClass == null) return null;

        for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
            if (orgClass.isInstance(org)) {
                return org;
            }
        }

        System.err.println("Organization of type " + orgClass.getSimpleName() + " not found in enterprise " + enterprise.getName());
        return null;
    }

    /**
     * Find the medical team coordination organization
     */
     
    private void calculateTotalCost() {

        if (txtEstimatedDuration.getText().trim().isEmpty()) {
            return;
        }

        try {
            int duration = Integer.parseInt(txtEstimatedDuration.getText().trim());
            double totalCost = 0.0;

            for (int i = 0; i < selectedDevicesTableModel.getRowCount(); i++) {
                Device device = (Device) selectedDevicesTableModel.getValueAt(i, DEVICE_OBJECT_COL_INDEX_SIMPLIFIED);
                if (device != null) {
                    double deviceCost = DEVICE_BASE_COST_PER_HOUR;

                    if (device.getName().toLowerCase().contains("davinci") || 
                        device.getName().toLowerCase().contains("robot")) {
                        deviceCost *= DAVINCI_COST_MULTIPLIER;
                    }
                    totalCost += deviceCost * duration;
                }
            }


            if (selectedRoomTableModel.getRowCount() > 0) {
                Room room = (Room) selectedRoomTableModel.getValueAt(0, ROOM_OBJECT_COL_INDEX_SIMPLIFIED);
                if (room != null) {
                    double roomCost = ROOM_BASE_COST_PER_HOUR;

                    if (room.getRoomType() != null && 
                        (room.getRoomType().equals("Robotic OR") || 
                         room.getRoomType().contains("Imaging"))) {
                        roomCost *= ROBOTIC_ROOM_COST_MULTIPLIER;
                    }
                    totalCost += roomCost * duration;
                }
            }

            totalCost += selectedTeamTypesTableModel.getRowCount() * TEAM_BASE_COST_PER_HOUR * duration;


            txtEstimatedCost.setText(String.valueOf(Math.round(totalCost)));

        } catch (NumberFormatException e) {
            System.err.println("Invalid duration format: " + e.getMessage());
        }
    }
    
    private void updateAvailableRooms() {
        availableRoomsTableModel.setRowCount(0);

        nonMovableDevice = findSelectedNonMovableDevice();

        String requiredRoomType = inferRequiredRoomType();

        for (Network network : business.getNetworkList()) {
            if (network == null || network.getEnterpriseDirectory() == null) continue;
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof HospitalEnterprise) {
                    HospitalEnterprise hospital = (HospitalEnterprise) ent;
                    String hospitalAbbr = getAbbreviatedHospitalName(hospital.getName());

                    if (nonMovableDevice != null) {
                        HospitalEnterprise deviceHospital = nonMovableDevice.getOwningHospital();
                        if (deviceHospital != null && !hospital.equals(deviceHospital)) {
                            continue; 
                        }
                    }

                    if (hospital.getRoomDirectory() != null) {
                        for (Room room : hospital.getRoomDirectory().getRoomList()) {
                            if (room != null && room.isAvailable() &&
                                (requiredRoomType == null || requiredRoomType.equalsIgnoreCase(room.getRoomType())))
                            {
                                Object[] row = new Object[ROOM_COLUMNS_SIMPLIFIED.length];
                                row[0] = String.valueOf(room.getNumber());
                                row[1] = room.getFloor();
                                row[2] = hospitalAbbr;
                                row[ROOM_OBJECT_COL_INDEX_SIMPLIFIED] = room;
                                availableRoomsTableModel.addRow(row);
                            }
                        }
                    }
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDevice;
    private javax.swing.JButton btnAddRoom;
    private javax.swing.JButton btnAddTeamType;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRemoveDevice;
    private javax.swing.JButton btnRemoveRoom;
    private javax.swing.JButton btnRemoveTeamType;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lblBundleName;
    private javax.swing.JLabel lblDevice;
    private javax.swing.JLabel lblEstimatedCost;
    private javax.swing.JLabel lblEstimatedDuration;
    private javax.swing.JLabel lblMedicalTeamType;
    private javax.swing.JLabel lblOperationRoom;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblAvailDevices;
    private javax.swing.JTable tblAvailRooms;
    private javax.swing.JTable tblAvailTeamTypes;
    private javax.swing.JTable tblGlobalBundles;
    private javax.swing.JTable tblSelectedDevices;
    private javax.swing.JTable tblSelectedRoom;
    private javax.swing.JTable tblSelectedTeamTypes;
    private javax.swing.JTextField txtBundleName;
    private javax.swing.JTextField txtEstimatedCost;
    private javax.swing.JTextField txtEstimatedDuration;
    // End of variables declaration//GEN-END:variables
}
