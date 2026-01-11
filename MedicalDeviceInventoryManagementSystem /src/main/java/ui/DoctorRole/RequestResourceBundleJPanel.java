/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.DoctorRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Enterprise.HospitalEnterprise;
import Business.Enterprise.PHSEnterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.Organization.PHSAdminOrganization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.BundleReservationWorkRequest;
import Business.models.bundle.ResourceBundle;
import Business.models.device.Device;
import Business.models.device.DeviceDirectory;
import Business.models.room.Room;
import Business.models.room.RoomDirectory;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.DateFormatter;

/**
 *
 * @author Di
 */
public class RequestResourceBundleJPanel extends javax.swing.JPanel {

    /**
     * Creates new form RequestResourceBundleJPanel
     */
    
    private JPanel userProcessContainer;
    private UserAccount userAccount;
    private Enterprise enterprise;
    private EcoSystem business; 
    private HashMap<String, String> deviceRoomTypeMap = new HashMap<>();
    private ResourceBundle resourceBundle;
    private ArrayList<Device> selectedDevices;
    
    private final String[] SURGERY_TYPE_OPTIONS = {
        "-- Select Surgery Type --", 
        "Urology",
        "Gynecology",
        "General Surgery",
        "Thoracic Surgery",
        "Cardiac Surgery",
        "Head and Neck Surgery",
        "Pediatric Surgery",
        "Orthopedics",
        "Other..."
    };
    
    private final String[] ROOM_TYPE_OPTIONS = {
        "-- Select Room Type --",
        "Robotic OR",
        "Imaging Room",
        "General OR",
        "Not Required"
    };
    
    private String inferredRoomTypeForRequest = null;
    
    public RequestResourceBundleJPanel(JPanel userProcessContainer, UserAccount account, Enterprise enterprise, EcoSystem business) {
        initComponents();
        
        this.userProcessContainer = userProcessContainer;
        this.userAccount = account;
        this.enterprise = enterprise;
        this.business = business;
        this.selectedDevices = new ArrayList<>();
        this.resourceBundle = new ResourceBundle();
        
        cmbSurgeryType.removeAllItems();
        DefaultComboBoxModel<String> surgeryModel = new DefaultComboBoxModel<>(SURGERY_TYPE_OPTIONS);
        cmbSurgeryType.setModel(surgeryModel);
        
        cmbRequiredRoomType.removeAllItems();
        DefaultComboBoxModel<String> roomTypeModel = new DefaultComboBoxModel<>(ROOM_TYPE_OPTIONS);
        cmbRequiredRoomType.setModel(roomTypeModel);
        
        setupDeviceRoomMapping();
        
        setupDeviceListeners();
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            dateFormatter.setOverwriteMode(true);
            dateFormatter.setCommitsOnValidEdit(true);

            ftfRequiredDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(dateFormatter));
            ftfRequiredDate.setToolTipText("Enter date as yyyy-MM-dd");

        } catch (Exception e) {
            System.err.println("Error setting date formatter for ftfRequiredDate: " + e.getMessage());
        }
    }
    
    private void setupDeviceRoomMapping() {
        deviceRoomTypeMap.put("da Vinci Surgical Robot System", "Robotic OR");
        deviceRoomTypeMap.put("X-Ray Machine", "Imaging Room");
        deviceRoomTypeMap.put("MRI Scanner", "Imaging Room");
        deviceRoomTypeMap.put("Ultrasound", "General OR");
    }
    
    private void setupDeviceListeners() {
        ItemListener listener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object source = e.getSource();
                String sourceName = "Unknown";
                if (source instanceof javax.swing.JCheckBox) {
                   sourceName = ((javax.swing.JCheckBox) source).getText();
                }
                updateRoomOptions();
            }
        };

        chkDaVinci.addItemListener(listener);
        chkXRay.addItemListener(listener);
        chkMRI.addItemListener(listener);
        chkUltrasound.addItemListener(listener);
    }
    
    private void updateRoomOptions() {

        String requiredRoomType = null;
        if (chkDaVinci.isSelected()) {
            requiredRoomType = "Robotic OR";
        } else if (chkXRay.isSelected() || chkMRI.isSelected()) {
            requiredRoomType = "Imaging Room";
        } else if (chkUltrasound.isSelected()) {
            requiredRoomType = "General OR";
        } else {
            requiredRoomType = "General OR";
        }

        this.inferredRoomTypeForRequest = requiredRoomType;

        boolean typeFoundInCombo = false;
        if (requiredRoomType != null) {
            for (int i = 0; i < cmbRequiredRoomType.getItemCount(); i++) {
                Object item = cmbRequiredRoomType.getItemAt(i);
                if (item instanceof String && requiredRoomType.equalsIgnoreCase((String) item)) {
                    cmbRequiredRoomType.setSelectedIndex(i);
                    typeFoundInCombo = true;
                    break;
                }
            }
        }

        if (!typeFoundInCombo) {
            cmbRequiredRoomType.setSelectedIndex(0);
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

        backJButton = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblPatientName = new javax.swing.JLabel();
        txtPatientName = new javax.swing.JTextField();
        lblPatientId = new javax.swing.JLabel();
        txtPatientId = new javax.swing.JTextField();
        lblSurgeryType = new javax.swing.JLabel();
        lblRequiredDate = new javax.swing.JLabel();
        lblNotes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNotes = new javax.swing.JTextArea();
        btnSubmit = new javax.swing.JButton();
        chkXRay = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        chkUltrasound = new javax.swing.JCheckBox();
        chkMRI = new javax.swing.JCheckBox();
        chkDaVinci = new javax.swing.JCheckBox();
        lblORType = new javax.swing.JLabel();
        ftfRequiredDate = new javax.swing.JFormattedTextField();
        cmbSurgeryType = new javax.swing.JComboBox<>();
        cmbRequiredRoomType = new javax.swing.JComboBox<>();

        backJButton.setText("<<Back");
        backJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backJButtonActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        lblTitle.setText("Request Resource Bundle");

        lblPatientName.setText("Patient Name:");

        lblPatientId.setText("Patient ID:");

        lblSurgeryType.setText("Surgery Type:");

        lblRequiredDate.setText("Required Date:");

        lblNotes.setText("Notes:");

        txtNotes.setColumns(20);
        txtNotes.setRows(5);
        jScrollPane1.setViewportView(txtNotes);

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        chkXRay.setText("X-Ray Machine");

        jLabel1.setText("Required Device(s):");

        chkUltrasound.setText("Ultrasound");

        chkMRI.setText("MRI Scanner");

        chkDaVinci.setText("da Vinci Surgical Robot System");
        chkDaVinci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDaVinciActionPerformed(evt);
            }
        });

        lblORType.setText("Required Room Type:");

        cmbSurgeryType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSurgeryType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSurgeryTypeActionPerformed(evt);
            }
        });

        cmbRequiredRoomType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRequiredRoomType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRequiredRoomTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(backJButton)
                        .addGap(115, 115, 115)
                        .addComponent(lblTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(btnSubmit))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPatientName)
                                    .addComponent(lblPatientId)
                                    .addComponent(lblSurgeryType)
                                    .addComponent(lblRequiredDate)
                                    .addComponent(jLabel1)
                                    .addComponent(lblORType))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkDaVinci)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(chkXRay)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkUltrasound)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkMRI))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(ftfRequiredDate, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPatientId, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cmbSurgeryType, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE)
                                        .addComponent(txtPatientName, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(cmbRequiredRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNotes))))
                .addContainerGap(194, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, lblPatientId, lblPatientName, lblRequiredDate, lblSurgeryType});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(backJButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(lblTitle)))
                        .addGap(171, 171, 171)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chkXRay)
                                .addComponent(chkUltrasound)
                                .addComponent(chkMRI))
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPatientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPatientName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPatientId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPatientId))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbSurgeryType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSurgeryType))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ftfRequiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRequiredDate))
                        .addGap(45, 45, 45)))
                .addComponent(chkDaVinci)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblORType)
                    .addComponent(cmbRequiredRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNotes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSubmit)
                .addContainerGap(101, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backJButtonActionPerformed
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_backJButtonActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed

        String patientName = txtPatientName.getText().trim();
        String patientId = txtPatientId.getText().trim();
        String notes = txtNotes.getText().trim();

        if (patientName.isEmpty() || patientId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient Name and Patient ID cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedSurgeryType = null;
        int surgeryTypeIndex = cmbSurgeryType.getSelectedIndex();
        if (surgeryTypeIndex <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a valid Surgery Type.", "Input Error", JOptionPane.WARNING_MESSAGE);
            cmbSurgeryType.requestFocus();
            return;
        }
        selectedSurgeryType = (String) cmbSurgeryType.getSelectedItem();

        String requiredDateString = null;
        Object value = ftfRequiredDate.getValue();
        if (value instanceof java.util.Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            requiredDateString = sdf.format((java.util.Date) value);
        } else {
            String textValue = ftfRequiredDate.getText().trim();
            if (textValue.isEmpty() || textValue.contains("_")) {
                JOptionPane.showMessageDialog(this, "Please enter a complete and valid Required Date in yyyy-MM-dd format.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                ftfRequiredDate.requestFocus();
                return;
            }
            JOptionPane.showMessageDialog(this, "Required Date is not valid. Please ensure it matches yyyy-MM-dd format.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            ftfRequiredDate.requestFocus();
            return;
        }

        if (!chkDaVinci.isSelected() && !chkXRay.isSelected() && !chkMRI.isSelected() &&
            !chkUltrasound.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please select at least one device!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HospitalEnterprise hospitalEnterprise = null;
        if (enterprise instanceof HospitalEnterprise) {
            hospitalEnterprise = (HospitalEnterprise) enterprise;
        } else {
            for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
                if (org.getClass().getName().contains("Hospital")) {
                    hospitalEnterprise = (HospitalEnterprise) enterprise;
                    break;
                }
            }
        }

        if (hospitalEnterprise == null) {
            JOptionPane.showMessageDialog(null, "Hospital enterprise not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedDevices = new ArrayList<>();
        DeviceDirectory deviceDirectory = hospitalEnterprise.getDeviceDirectory(); 
        if (deviceDirectory != null) {
            if (chkDaVinci.isSelected()) {
                for (Device device : deviceDirectory.getDeviceList()) {
                    if (device.getName().contains("DaVinci")) {
                        selectedDevices.add(device);
                    }
                }
            }
            if (chkXRay.isSelected()) {
                for (Device device : deviceDirectory.getDeviceList()) {
                    if (device.getName().contains("X-Ray")) {
                        selectedDevices.add(device);
                    }
                }
            }
            if (chkMRI.isSelected()) {
                for (Device device : deviceDirectory.getDeviceList()) {
                    if (device.getName().contains("MRI")) {
                        selectedDevices.add(device);
                    }
                }
            }
            if (chkUltrasound.isSelected()) {
                for (Device device : deviceDirectory.getDeviceList()) {
                    if (device.getName().contains("Ultrasound")) {
                        selectedDevices.add(device);
                    }
                }
            }
        } else {
            System.err.println("ERROR: Device Directory is null! Cannot add devices.");
        }

        resourceBundle = new ResourceBundle();
        resourceBundle.setName(selectedSurgeryType + " - " + patientName);
        resourceBundle.setSurgeryType(selectedSurgeryType);
        resourceBundle.setRequiredDevices(selectedDevices);
        resourceBundle.setRequiredRoom(null);

        BundleReservationWorkRequest request = new BundleReservationWorkRequest();
        request.setBusiness(business);
        System.out.println("Bundle reservation work request created" + business);
        
        request.setPatientName(patientName);
        request.setPatientID(patientId);
        request.setSurgeryType(selectedSurgeryType);
        request.setRequiredDate(requiredDateString);
        request.setRequiredRoomType(this.inferredRoomTypeForRequest);
        request.setNotes(notes);
        request.setResourceBundle(resourceBundle);

        request.setSender(userAccount);
        request.setStatusString("Submitted to PHS");
        request.setDeviceApprovalsNeeded(selectedDevices.size());
        request.setRoomApprovalNeeded(this.inferredRoomTypeForRequest != null);

        PHSAdminOrganization phsAdminOrg = null;
        if (this.business == null) {
            JOptionPane.showMessageDialog(null, "System Error: EcoSystem context not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            outerLoop:
            for (Network network : this.business.getNetworkList()) {

                if (network.getEnterpriseDirectory() == null) continue;
                for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {

                    if (ent instanceof PHSEnterprise) {
                        if (ent.getOrganizationDirectory() == null) continue;
                        for (Organization org : ent.getOrganizationDirectory().getOrganizationList()) {
                            if (org instanceof PHSAdminOrganization) {
                                phsAdminOrg = (PHSAdminOrganization) org;
                                break outerLoop;
                            }
                        }
                    }
                }
            }
        }

        if (phsAdminOrg == null) {
            JOptionPane.showMessageDialog(null, "PHS Admin Organization not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        phsAdminOrg.getWorkQueue().getWorkRequestList().add(request);
        userAccount.getWorkQueue().getWorkRequestList().add(request);

        JOptionPane.showMessageDialog(null, "Request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        clearFields();
        
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void chkDaVinciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDaVinciActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDaVinciActionPerformed

    private void cmbSurgeryTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSurgeryTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSurgeryTypeActionPerformed

    private void cmbRequiredRoomTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRequiredRoomTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbRequiredRoomTypeActionPerformed

    private void clearFields() {
        txtPatientName.setText("");
        txtPatientId.setText("");
        cmbSurgeryType.setSelectedIndex(0);
        ftfRequiredDate.setText("");
        ftfRequiredDate.setValue(null);
        txtNotes.setText("");
        
        chkDaVinci.setSelected(false);
        chkXRay.setSelected(false);
        chkMRI.setSelected(false);
        chkUltrasound.setSelected(false);
        
        cmbRequiredRoomType.setSelectedIndex(0);
        this.inferredRoomTypeForRequest = null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backJButton;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JCheckBox chkDaVinci;
    private javax.swing.JCheckBox chkMRI;
    private javax.swing.JCheckBox chkUltrasound;
    private javax.swing.JCheckBox chkXRay;
    private javax.swing.JComboBox<String> cmbRequiredRoomType;
    private javax.swing.JComboBox<String> cmbSurgeryType;
    private javax.swing.JFormattedTextField ftfRequiredDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNotes;
    private javax.swing.JLabel lblORType;
    private javax.swing.JLabel lblPatientId;
    private javax.swing.JLabel lblPatientName;
    private javax.swing.JLabel lblRequiredDate;
    private javax.swing.JLabel lblSurgeryType;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextArea txtNotes;
    private javax.swing.JTextField txtPatientId;
    private javax.swing.JTextField txtPatientName;
    // End of variables declaration//GEN-END:variables
}
