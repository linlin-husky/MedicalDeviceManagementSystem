/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.DeviceManagerRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Enterprise.HospitalEnterprise;
import Business.Enterprise.PHSEnterprise;
import Business.Network.Network;
import Business.Organization.DeviceManagementOrganization;
import Business.Organization.Organization;
import Business.Organization.PHSAdminOrganization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.BundleReservationWorkRequest;
import Business.WorkQueue.DeviceReservationWorkRequest;
import Business.WorkQueue.WorkRequest;
import Business.models.device.Device;
import java.awt.CardLayout;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rajath, Di
 */
public class ProcessDeviceReservationRequestsJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ProcessHospitalTransferRequestsJPanel
     */
    
    private JPanel userProcessContainer;
    private UserAccount userAccount;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    
    private DefaultTableModel requestsTableModel;
    
    public ProcessDeviceReservationRequestsJPanel(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise, EcoSystem business) {
        initComponents();
        
        this.userProcessContainer = userProcessContainer;
        this.userAccount = account;
        
        if (organization instanceof DeviceManagementOrganization) {
             this.organization = (DeviceManagementOrganization) organization;
        } else {
             System.err.println("Error: Incorrect organization type passed to ManageDeviceReservationRequestsJPanel.");
             btnApprove.setEnabled(false);
             btnReject.setEnabled(false);
        }
        
        this.enterprise = enterprise;
        this.business = business;

        initializeTable();
        populateTable();
    }
    
    private void initializeTable() {
        requestsTableModel = (DefaultTableModel) tblDeviceRequests.getModel();
        hideTableColumn(tblDeviceRequests, 0);
    }

      private void hideTableColumn(javax.swing.JTable table, int columnIndex) {
        if (table != null && table.getColumnModel().getColumnCount() > columnIndex && columnIndex >= 0) {
            javax.swing.table.TableColumn column = table.getColumnModel().getColumn(columnIndex);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setWidth(0);
            column.setPreferredWidth(0);
        } else {
            System.err.println("Error hiding column " + columnIndex + " for table " + (table != null ? table.getName() : "null"));
        }
   }

    public void populateTable() {
        requestsTableModel.setRowCount(0);

        if (organization == null || organization.getWorkQueue() == null) {
            System.err.println("DeviceManagementOrganization or its WorkQueue is null.");
            return;
        }

        for (WorkRequest request : organization.getWorkQueue().getWorkRequestList()) {
            if (request instanceof DeviceReservationWorkRequest) {
                DeviceReservationWorkRequest deviceRequest = (DeviceReservationWorkRequest) request;

                // Add debugging to see all requests
                System.out.println("Found device request: " + 
                    (deviceRequest.getDevice() != null ? deviceRequest.getDevice().getName() : "null") + 
                    " - Status: " + deviceRequest.getStatus());

                if ("Pending Device Manager Approval".equalsIgnoreCase(deviceRequest.getStatusString())) {
                    Object[] row = new Object[5];
                    row[0] = deviceRequest;

                    String requestingHospital = "Unknown";
                    String parentId = extractParentId(deviceRequest.getMessage());
                    BundleReservationWorkRequest parentReq = findParentBundleRequest(parentId);

                    if (parentReq != null && parentReq.getSender() != null) {
                        requestingHospital = findHospitalNameForUser(parentReq.getSender());
                    }

                    row[1] = requestingHospital;
                    row[2] = deviceRequest.getDevice() != null ? 
                             deviceRequest.getDevice().getName() : "N/A";
                    row[3] = deviceRequest.getScheduleDate() != null ? 
                             deviceRequest.getScheduleDate() : "N/A";
                    row[4] = deviceRequest.getStatusString();

                    requestsTableModel.addRow(row);
                }
            }
        }
    }

    private String findHospitalNameForUser(UserAccount userAccount) {
        if (userAccount == null || business == null) {
            return "Unknown";
        }

        for (Network network : business.getNetworkList()) {
            for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (enterprise instanceof HospitalEnterprise) {
                    for (UserAccount ua : enterprise.getUserAccountDirectory().getUserAccountList()) {
                        if (ua.equals(userAccount)) {
                            return enterprise.getName();
                        }
                    }

                    for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
                        for (UserAccount ua : org.getUserAccountDirectory().getUserAccountList()) {
                            if (ua.equals(userAccount)) {
                                return enterprise.getName();
                            }
                        }
                    }
                }
            }
        }

        return "Unknown Hospital";
    }

    private String extractParentId(String message) {
        if (message != null && message.startsWith("ParentBundleID:")) {
            return message.substring("ParentBundleID:".length());
        }
        return null;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDeviceRequests = new javax.swing.JTable();
        lblDeviceReservationRequests = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        lblTitle.setText("Process Device Reservation Requests");

        tblDeviceRequests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Request ID", "Requesting Hospital", "Device", "Required Date", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblDeviceRequests);

        lblDeviceReservationRequests.setText("Device Reservation Requests:");

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnApprove.setText("Approve");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveActionPerformed(evt);
            }
        });

        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(btnBack))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(lblTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnApprove)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReject))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblDeviceReservationRequests)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRefresh))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnApprove, btnRefresh, btnReject});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(btnBack)
                .addGap(8, 8, 8)
                .addComponent(lblTitle)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeviceReservationRequests)
                    .addComponent(btnRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApprove)
                    .addComponent(btnReject))
                .addContainerGap(256, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        populateTable();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        int selectedRow = tblDeviceRequests.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request to approve.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DeviceReservationWorkRequest request = (DeviceReservationWorkRequest) requestsTableModel.getValueAt(selectedRow, 0);

        if (!"Pending Device Manager Approval".equalsIgnoreCase(request.getStatusString())) {
             JOptionPane.showMessageDialog(this, "This request is not pending approval.", "Warning", JOptionPane.WARNING_MESSAGE);
             return;
        }

        request.setStatusString("Approved");
        request.setApproval(true);
        request.setApprovedBy(userAccount);
        request.setReceiver(userAccount);
        request.setResolveDate(new Date());

        Device device = request.getDevice();
        if (device != null) {

            if ("Available".equalsIgnoreCase(device.getDeviceStatus())) {
                device.setDeviceStatus("Reserved");
                System.out.println("Device " + device.getName() + " status set to Reserved.");
            } else {
                 JOptionPane.showMessageDialog(this, "Device " + device.getName() + " is no longer available ("+device.getDeviceStatus()+"). Cannot approve request.", "Device Unavailable", JOptionPane.ERROR_MESSAGE);
                 request.setStatusString("Approved - Device Unavailable");
            }
        } else {
            System.err.println("Device object is null in the request " + request.getMessage());
            request.setStatusString("Approved - Device Missing");
        }

        String parentId = extractParentId(request.getMessage());
        BundleReservationWorkRequest parentRequest = findParentBundleRequest(parentId);
        if (parentRequest != null) {
            parentRequest.incrementDeviceApprovalsReceived();
            System.out.println("Parent Bundle Request " + parentId + " notified of device approval.");
        } else {
            System.err.println("Could not find parent Bundle Request with ID: " + parentId + " to notify approval.");
        }

        JOptionPane.showMessageDialog(this, "Request approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
        populateTable();
    }//GEN-LAST:event_btnApproveActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        int selectedRow = tblDeviceRequests.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request to reject.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DeviceReservationWorkRequest request = (DeviceReservationWorkRequest) requestsTableModel.getValueAt(selectedRow, 0);

        if (!"Pending Device Manager Approval".equalsIgnoreCase(request.getStatusString())) {
             JOptionPane.showMessageDialog(this, "This request is not pending approval.", "Warning", JOptionPane.WARNING_MESSAGE);
             return;
        }

        request.setStatusString("Rejected");
        request.setApproval(false);
        request.setApprovedBy(userAccount);
        request.setReceiver(userAccount);
        request.setResolveDate(new Date());



         Device device = request.getDevice();
         if (device != null && "Reserved".equalsIgnoreCase(device.getDeviceStatus())) {

              device.setDeviceStatus("Available");
              System.out.println("Device " + device.getName() + " status set back to Available after rejection.");
         }

         String parentId = extractParentId(request.getMessage());
         BundleReservationWorkRequest parentRequest = findParentBundleRequest(parentId);
        if (parentRequest != null) {
            parentRequest.markAsRejected();
            System.out.println("Parent Bundle Request " + parentId + " notified of device rejection (marked as Rejected).");
        } else {
            System.err.println("Could not find parent Bundle Request with ID: " + parentId + " to notify rejection.");
        }

        JOptionPane.showMessageDialog(this, "Request rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
        populateTable();
    }//GEN-LAST:event_btnRejectActionPerformed

    private BundleReservationWorkRequest findParentBundleRequest(String parentId) {
        if (parentId == null || parentId.isEmpty()) return null;

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
            System.err.println("Cannot find PHS Enterprise to search for parent bundle request.");
            return null;
        }

        // Search through all organizations in PHS
        for (Organization org : phsEnt.getOrganizationDirectory().getOrganizationList()) {
            // No need to check for specific organization type, just check all work queues
            if (org.getWorkQueue() != null && org.getWorkQueue().getWorkRequestList() != null) {
                for (WorkRequest wr : org.getWorkQueue().getWorkRequestList()) {
                    if (wr instanceof BundleReservationWorkRequest) {
                        BundleReservationWorkRequest brwr = (BundleReservationWorkRequest) wr;
                        if (parentId.equalsIgnoreCase(brwr.getBundleIdFromTemplate())) {
                            return brwr;
                        }
                    }
                }
            }
        }

        System.err.println("Parent Bundle Request with ID " + parentId + " not found in PHS organizations.");
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReject;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDeviceReservationRequests;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblDeviceRequests;
    // End of variables declaration//GEN-END:variables
}
