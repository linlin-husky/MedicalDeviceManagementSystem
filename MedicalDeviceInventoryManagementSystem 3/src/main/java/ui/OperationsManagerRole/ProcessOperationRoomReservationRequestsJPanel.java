/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.OperationsManagerRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Enterprise.HospitalEnterprise;
import Business.Enterprise.PHSEnterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.Organization.PHSAdminOrganization;
import Business.Organization.OperationsManagementOrganization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.BundleReservationWorkRequest;
import Business.WorkQueue.OperationsRoomReservationWorkRequest;
import Business.WorkQueue.WorkRequest;
import Business.models.room.Room;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author raunak
 */
public class ProcessOperationRoomReservationRequestsJPanel extends javax.swing.JPanel {

    private JPanel userProcessContainer;
    private UserAccount userAccount;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    
    private DefaultTableModel requestsTableModel;
    
    /**
     * Creates new form ProcessWorkRequestJPanel
     */
    public ProcessOperationRoomReservationRequestsJPanel(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise, EcoSystem business) {
        initComponents();
        
        this.userProcessContainer = userProcessContainer;
        this.userAccount = account;

        if (organization instanceof OperationsManagementOrganization) {
            this.organization = (OperationsManagementOrganization) organization;
        } else {
            System.err.println("Error: Incorrect organization type passed to ProcessOperationRoomReservationRequestsJPanel.");
            btnApprove.setEnabled(false);
            btnReject.setEnabled(false);
        }

        this.enterprise = enterprise;
        this.business = business;

        initializeTable();
        populateTable();
    }

    private void initializeTable() {
        requestsTableModel = (DefaultTableModel) tblRoomRequests.getModel();
        hideTableColumn(tblRoomRequests, 0);
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
            System.err.println("OperationsManagementOrganization or its WorkQueue is null.");
            return;
        }

        for (WorkRequest request : organization.getWorkQueue().getWorkRequestList()) {
            if (request instanceof OperationsRoomReservationWorkRequest) {
                OperationsRoomReservationWorkRequest roomRequest = (OperationsRoomReservationWorkRequest) request;

                if ("Pending Ops Manager Approval".equalsIgnoreCase(roomRequest.getStatusString())) {
                    Object[] row = new Object[5];
                    row[0] = roomRequest;

                    String requestingHospital = "Unknown";
                    String parentId = extractParentId(roomRequest.getMessage());
                    BundleReservationWorkRequest parentReq = findParentBundleRequest(parentId);

                    if (parentReq != null && parentReq.getSender() != null) {
                        requestingHospital = findHospitalNameForUser(parentReq.getSender());
                    }

                    row[1] = requestingHospital;
                    row[2] = roomRequest.getRoom() != null ? 
                             roomRequest.getRoom().toString() : "N/A";
                    row[3] = roomRequest.getScheduleDate() != null ? 
                             roomRequest.getScheduleDate() : "N/A";
                    row[4] = roomRequest.getStatus();

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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblRoomRequests = new javax.swing.JTable();
        lblOperationRoomReservationRequests = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();

        tblRoomRequests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Request ID", "Requesting Hospital", "Operation Room", "Required Date", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblRoomRequests);

        lblOperationRoomReservationRequests.setText("Operation Room Reservation Requests:");

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

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        lblTitle.setText("Process Operation Room Reservation Requests");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(btnBack)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(163, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnApprove)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReject))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblOperationRoomReservationRequests)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRefresh))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(144, 144, 144))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addGap(189, 189, 189))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btnBack)
                .addGap(30, 30, 30)
                .addComponent(lblTitle)
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOperationRoomReservationRequests)
                    .addComponent(btnRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApprove)
                    .addComponent(btnReject))
                .addContainerGap(212, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        populateTable();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        int selectedRow = tblRoomRequests.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request to approve.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OperationsRoomReservationWorkRequest request = (OperationsRoomReservationWorkRequest) requestsTableModel.getValueAt(selectedRow, 0);

        if (!"Pending Ops Manager Approval".equalsIgnoreCase(request.getStatusString())) {
            JOptionPane.showMessageDialog(this, "This request is not pending approval.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        request.setStatusString("Approved");
        request.setApproval(true);
        request.setApprovedBy(userAccount);
        request.setReceiver(userAccount);
        request.setResolveDate(new Date());

        Room room = request.getRoom();
        if (room != null) {
            if (room.isAvailable()) {
                room.setAvailable(false);

                // 获取父 bundle 并更新其状态
                String parentId = extractParentId(request.getMessage());
                BundleReservationWorkRequest parentBundle = findParentBundleRequest(parentId);

                if (parentBundle != null) {
                    // 通知父 bundle 房间已批准 - 这是关键部分
                    parentBundle.setRoomApproved(true);
                    System.out.println("Parent Bundle Request " + parentId + " notified of room approval.");

                    // 手动触发状态检查
                    parentBundle.checkAndFinalizeStatus();
                } else {
                    System.err.println("Could not find parent bundle to notify of room approval: " + parentId);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Room " + room.getNumber() + " is no longer available. Cannot approve request.", "Room Unavailable", JOptionPane.ERROR_MESSAGE);
                request.setStatusString("Approved - Room Unavailable");
            }
        } else {
            System.err.println("Room object is null in the request " + request.getMessage());
            request.setStatusString("Approved - Room Missing");
        }

        JOptionPane.showMessageDialog(this, "Request approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
        populateTable();
    }//GEN-LAST:event_btnApproveActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        int selectedRow = tblRoomRequests.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request to reject.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OperationsRoomReservationWorkRequest request = (OperationsRoomReservationWorkRequest) requestsTableModel.getValueAt(selectedRow, 0);

        if (!"Pending Ops Manager Approval".equalsIgnoreCase(request.getStatusString())) {
            JOptionPane.showMessageDialog(this, "This request is not pending approval.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        request.setStatusString("Rejected");
        request.setApproval(false);
        request.setApprovedBy(userAccount);
        request.setReceiver(userAccount);
        request.setResolveDate(new Date());

        Room room = request.getRoom();
        if (room != null && !room.isAvailable()) {
            room.setAvailable(true);
        }
        
        JOptionPane.showMessageDialog(this, "Request rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
        populateTable();
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed

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

        // Check all organizations in PHS Enterprise
        for (Organization org : phsEnt.getOrganizationDirectory().getOrganizationList()) {
            // Check work queue in all organizations
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

        System.err.println("Parent Bundle Request with ID " + parentId + " not found in any PHS organization.");
        return null;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReject;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOperationRoomReservationRequests;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblRoomRequests;
    // End of variables declaration//GEN-END:variables
}
