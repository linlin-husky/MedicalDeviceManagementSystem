/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.MedicalTeamManagerRole;

import Business.EcoSystem;
import Business.Employee.Employee;
import Business.Enterprise.Enterprise;
import Business.Enterprise.HospitalEnterprise;
import Business.Enterprise.PHSEnterprise;
import Business.Network.Network;
import Business.Organization.DoctorOrganization;
import Business.Organization.Organization;
import Business.Organization.PHSAdminOrganization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.BundleReservationWorkRequest;
import Business.WorkQueue.TeamAssignmentWorkRequest;
import Business.WorkQueue.WorkQueue;
import Business.WorkQueue.WorkRequest;
import Business.models.team.MedicalTeam;
import Business.models.team.MedicalTeamDirectory;
import java.awt.CardLayout;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class MedicalTeamManagerWorkAreaJPanel extends javax.swing.JPanel {

    /**
     * Creates new form MedicalTeamManagerWorkAreaJPanel
     */
    
    private JPanel userProcessContainer;
    private UserAccount userAccount;    // The logged-in MedicalTeamManager
    private Organization organization;  // The organization the manager belongs to
    private Enterprise enterprise;      // The enterprise the manager belongs to
    private EcoSystem business;         // Reference to the entire system
    private PHSEnterprise phsEnterprise; // Reference to the PHS enterprise
    private WorkQueue sharedWorkQueue;  // Reference to shared work queue

    private DefaultTableModel teamMemberTableModel;
    private DefaultTableModel pendingTasksTableModel;
    private DefaultTableModel inProgressTasksTableModel;
    
    public MedicalTeamManagerWorkAreaJPanel(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise, EcoSystem business) {
        initComponents();
        
        this.userProcessContainer = userProcessContainer;
        this.userAccount = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;
        
        // Find the PHS enterprise for shared work queue
        this.phsEnterprise = findPHSEnterprise();

        if (this.phsEnterprise != null) {
            this.sharedWorkQueue = phsEnterprise.getSharedTeamAssignmentWorkQueue();
            System.out.println("Got shared team assignment queue from PHS Enterprise");
        }
        
        initializeTables();
        populateTeamMemberTable();
        populatePendingTasksTable();
        populateInProgressTasksTable();
        
        if (userAccount != null && userAccount.getEmployee() != null) {
            lblTeamName.setText(account.getEmployee().getName());
        } else {
            lblTeamName.setText("Unknown Manager");
        }
    }
    
    private void initializeTables() {
        // Link table models to the JTable components
        teamMemberTableModel = (DefaultTableModel) tblMedicalTeam.getModel();
        pendingTasksTableModel = (DefaultTableModel) tblPendingTasks.getModel();
        inProgressTasksTableModel = (DefaultTableModel) tblInProgressTasks.getModel();

        // Hide the object column in tables where it's used
        hideTableColumn(tblPendingTasks, 0); // Hide Request Object column
        hideTableColumn(tblInProgressTasks, 0); // Hide Request Object column
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

    private PHSEnterprise findPHSEnterprise() {
        for (Network network : business.getNetworkList()) {
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof PHSEnterprise) {
                    return (PHSEnterprise) ent;
                }
            }
        }
        return null;
    }

    // Method to get specializations managed by this manager
    private List<String> getManagedSpecializations() {
        List<String> specializations = new ArrayList<>();
        
        if (organization instanceof DoctorOrganization) {
            DoctorOrganization docOrg = (DoctorOrganization) organization;
            MedicalTeamDirectory teamDir = docOrg.getMedicalTeamDirectory();
            if (teamDir != null) {
                for (MedicalTeam team : teamDir.getTeamList()) {
                    if (team.getTeamManager() != null && userAccount.getEmployee().equals(team.getTeamManager())) {
                        if (team.getSpecialization() != null && !specializations.contains(team.getSpecialization())) {
                            specializations.add(team.getSpecialization());
                        }
                    }
                }
            } else {
                System.err.println("MedicalTeamDirectory not found in DoctorOrganization: " + organization.getName());
            }
        } else {
            System.err.println("Manager's organization is not DoctorOrganization. Cannot determine managed specializations.");
        }
        
        System.out.println("Manager " + userAccount.getUsername() + " manages specializations: " + specializations);
        return specializations;
    }

    // Helper method to find employee role
    private String findEmployeeRole(Employee member) {
        if (member == null || organization == null) {
            return "Unknown Role";
        }
        
        // Try to find the user account for this employee
        for (UserAccount ua : organization.getUserAccountDirectory().getUserAccountList()) {
            if (ua.getEmployee() != null && ua.getEmployee().equals(member)) {
                return ua.getRole() != null ? ua.getRole().toString() : "Unknown Role";
            }
        }
        
        // If not found in current organization, could check enterprise level
        if (enterprise != null) {
            for (UserAccount ua : enterprise.getUserAccountDirectory().getUserAccountList()) {
                if (ua.getEmployee() != null && ua.getEmployee().equals(member)) {
                    return ua.getRole() != null ? ua.getRole().toString() : "Unknown Role";
                }
            }
        }
        
        return "Medical Staff"; // Default role if not found
    }

    // Populate the table showing team members managed by this manager
    private void populateTeamMemberTable() {
        teamMemberTableModel.setRowCount(0);

        if (organization == null || userAccount == null || userAccount.getEmployee() == null) {
            System.err.println("Unable to load team members: organization or user account is null");
            return;
        }

        if (organization instanceof DoctorOrganization) {
            DoctorOrganization docOrg = (DoctorOrganization) organization;
            MedicalTeamDirectory teamDir = docOrg.getMedicalTeamDirectory();

            if (teamDir == null || teamDir.getTeamList() == null || teamDir.getTeamList().isEmpty()) {
                System.err.println("Medical team directory is empty or inaccessible");
                return;
            }

            boolean foundTeam = false;
            for (MedicalTeam team : teamDir.getTeamList()) {
                if (team.getTeamManager() != null && userAccount.getEmployee().equals(team.getTeamManager())) {
                    foundTeam = true;
                    lblTeamName.setText(team.getTeamName() + " (" + team.getSpecialization() + ")");

                    if (team.getTeamMembers() != null && !team.getTeamMembers().isEmpty()) {
                        for (Employee member : team.getTeamMembers()) {
                            if (member != null) {
                                Object[] row = new Object[2]; 
                                row[0] = member.getName();
                                row[1] = findEmployeeRole(member);
                                teamMemberTableModel.addRow(row);
                                System.out.println("Added team member: " + member.getName());
                            }
                        }
                    } else {
                        System.out.println("Team " + team.getTeamName() + " has no members");
                    }
                    break;
                }
            }

            if (!foundTeam) {
                System.err.println("No team found managed by current user");
                lblTeamName.setText("No team assigned");
            }
        } else {
            System.err.println("Current organization is not DoctorOrganization");
            lblTeamName.setText("No team assigned");
        }
    }

    // Populate the "Pending Tasks" table
    private void populatePendingTasksTable() {
        pendingTasksTableModel.setRowCount(0);

        if (this.sharedWorkQueue == null) {
            System.err.println("Shared work queue is null, cannot load pending tasks");
            return;
        }

        List<String> managedSpecs = getManagedSpecializations();
        if (managedSpecs.isEmpty()) {
            System.out.println("Current manager doesn't manage any specializations");
            return;
        }

        System.out.println("Looking for pending tasks in specializations: " + managedSpecs);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean foundTasks = false;

        for (WorkRequest request : this.sharedWorkQueue.getWorkRequestList()) {
            if (request instanceof TeamAssignmentWorkRequest) {
                TeamAssignmentWorkRequest teamRequest = (TeamAssignmentWorkRequest) request;

                if ("Pending Assignment".equalsIgnoreCase(teamRequest.getStatusString()) &&
                    teamRequest.getReceiver() == null &&
                    managedSpecs.contains(teamRequest.getRequiredSpecialization()))
                {
                    foundTasks = true;
                    Object[] row = new Object[5];
                    row[0] = teamRequest;

                    BundleReservationWorkRequest parent = findParentBundleRequest(teamRequest.getParentBundleRequestId());
                    row[1] = parent != null ? parent.getPatientName() : "Unknown Patient";
                    row[2] = parent != null ? parent.getSurgeryType() : teamRequest.getRequiredSpecialization();
                    row[3] = (teamRequest.getRequiredDateTime() != null) ? sdf.format(teamRequest.getRequiredDateTime()) : "Unspecified Date";
                    row[4] = teamRequest.getStatus();

                    pendingTasksTableModel.addRow(row);
                    System.out.println("Added pending task: " + row[1] + ", " + row[2] + ", " + row[3]);
                }
            }
        }

        if (!foundTasks) {
            System.out.println("No pending tasks found");
        }
    }

    // Populate the "In-progress Tasks" table
    private void populateInProgressTasksTable() {
        inProgressTasksTableModel.setRowCount(0);

        if (this.sharedWorkQueue == null || userAccount == null) {
            System.err.println("Shared work queue or user account is null, cannot load in-progress tasks");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean foundTasks = false;

        for (WorkRequest request : this.sharedWorkQueue.getWorkRequestList()) {
            if (request instanceof TeamAssignmentWorkRequest) {
                TeamAssignmentWorkRequest teamRequest = (TeamAssignmentWorkRequest) request;

                if (userAccount.equals(teamRequest.getReceiver())) {
                    foundTasks = true;
                    Object[] row = new Object[5];
                    row[0] = teamRequest;

                    BundleReservationWorkRequest parent = findParentBundleRequest(teamRequest.getParentBundleRequestId());
                    row[1] = parent != null ? parent.getPatientName() : "Unknown Patient";
                    row[2] = parent != null ? parent.getSurgeryType() : teamRequest.getRequiredSpecialization();
                    row[3] = (teamRequest.getRequiredDateTime() != null) ? sdf.format(teamRequest.getRequiredDateTime()) : "Unspecified Date";
                    row[4] = teamRequest.getStatus();

                    inProgressTasksTableModel.addRow(row);
                    System.out.println("Added in-progress task: " + row[1] + ", " + row[2] + ", " + row[3]);
                }
            }
        }

        if (!foundTasks) {
            System.out.println("No in-progress tasks found");
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

        lblTitle = new javax.swing.JLabel();
        lblMedicalTeam = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMedicalTeam = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPendingTasks = new javax.swing.JTable();
        lblPendingTasks = new javax.swing.JLabel();
        btnViewDetails = new javax.swing.JButton();
        lblInProgressTasks = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblInProgressTasks = new javax.swing.JTable();
        btnAssign = new javax.swing.JButton();
        lblTeamName = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();

        lblTitle.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        lblTitle.setText("Medical Team Manager WorkArea");

        lblMedicalTeam.setText("Team:");

        tblMedicalTeam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Role"
            }
        ));
        jScrollPane1.setViewportView(tblMedicalTeam);

        tblPendingTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Request ID", "Patient", "Surgery Type", "Requried Date", "Status"
            }
        ));
        jScrollPane2.setViewportView(tblPendingTasks);

        lblPendingTasks.setText("Pending Tasks:");

        btnViewDetails.setText("View Details");
        btnViewDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDetailsActionPerformed(evt);
            }
        });

        lblInProgressTasks.setText("In-progress Tasks:");

        tblInProgressTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Request ID", "Patient", "Surgery Type", "Requried Date", "Status"
            }
        ));
        jScrollPane3.setViewportView(tblInProgressTasks);

        btnAssign.setText("Assign Task to the Team");
        btnAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignActionPerformed(evt);
            }
        });

        lblTeamName.setText("<value>");

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnViewDetails)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblMedicalTeam)
                            .addGap(18, 18, 18)
                            .addComponent(lblTeamName))
                        .addComponent(jScrollPane2)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblPendingTasks)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRefresh))
                        .addComponent(lblInProgressTasks)
                        .addComponent(jScrollPane3)
                        .addComponent(jScrollPane1))
                    .addComponent(btnAssign))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(lblTitle)
                .addGap(48, 221, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lblTitle)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMedicalTeam)
                    .addComponent(lblTeamName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPendingTasks)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAssign)
                        .addGap(30, 30, 30)
                        .addComponent(lblInProgressTasks)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewDetails))
                    .addComponent(btnRefresh))
                .addContainerGap(57, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        populateTeamMemberTable();
        populatePendingTasksTable();
        populateInProgressTasksTable();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignActionPerformed
        int selectedRow = tblPendingTasks.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a task to assign.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the selected team assignment request
        TeamAssignmentWorkRequest request = (TeamAssignmentWorkRequest) pendingTasksTableModel.getValueAt(selectedRow, 0);

        if (request == null || !"Pending Assignment".equalsIgnoreCase(request.getStatusString()) || request.getReceiver() != null) {
            JOptionPane.showMessageDialog(this, "This task is no longer available or already assigned.", "Error", JOptionPane.ERROR_MESSAGE);
            populatePendingTasksTable(); // Refresh to remove it
            return;
        }

        // Find an available team managed by this user
        MedicalTeam availableTeam = findAvailableTeam(request.getRequiredSpecialization(), request.getRequiredDateTime());

        if (availableTeam == null) {
            JOptionPane.showMessageDialog(this, "You currently have no available teams with specialization '" + request.getRequiredSpecialization() + "' for the required time.", "No Team Available", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Assign the task
        int confirm = JOptionPane.showConfirmDialog(this, "Assign this task to team '" + availableTeam.getTeamName() + "'?", "Confirm Assignment", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            request.setReceiver(userAccount);              // Assign to this manager
            request.setAssignedTeam(availableTeam);        // Assign to the specific team
            request.setStatusString("Team Assigned");            // Update status

            // Find the parent BundleReservationWorkRequest and notify it
            BundleReservationWorkRequest parentRequest = findParentBundleRequest(request.getParentBundleRequestId());
            if (parentRequest != null) {
                parentRequest.incrementTeamApprovalsReceived();
                System.out.println("Parent Bundle Request " + parentRequest.getBundleIdFromTemplate() + " notified of team assignment.");
            } else {
                System.err.println("Could not find parent Bundle Request with ID: " + request.getParentBundleRequestId());
            }

            JOptionPane.showMessageDialog(this, "Task assigned successfully to team: " + availableTeam.getTeamName());

            // Refresh the tables
            populatePendingTasksTable();
            populateInProgressTasksTable();
        }
    }//GEN-LAST:event_btnAssignActionPerformed

    private void btnViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewDetailsActionPerformed
        int selectedRowPending = tblPendingTasks.getSelectedRow();
        int selectedRowInProgress = tblInProgressTasks.getSelectedRow();
        TeamAssignmentWorkRequest selectedRequest = null;

        if (selectedRowPending >= 0) {
            selectedRequest = (TeamAssignmentWorkRequest) pendingTasksTableModel.getValueAt(selectedRowPending, 0);
        } else if (selectedRowInProgress >= 0) {
            selectedRequest = (TeamAssignmentWorkRequest) inProgressTasksTableModel.getValueAt(selectedRowInProgress, 0);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to view details.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedRequest != null) {
            // Display details using JOptionPane
            BundleReservationWorkRequest parent = findParentBundleRequest(selectedRequest.getParentBundleRequestId());
            String details = String.format(
                "Request Details:\n------------------\n" +
                "Request Type: Team Assignment\n" +
                "Status: %s\n" +
                "Required Specialization: %s\n" +
                "Required Date/Time: %s\n" +
                "Assigned Team: %s\n" +
                "Parent Bundle ID: %s\n" +
                "Patient: %s\n" +
                "Surgery Type: %s\n" +
                "Sender: %s",
                selectedRequest.getStatus(),
                selectedRequest.getRequiredSpecialization(),
                (selectedRequest.getRequiredDateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm").format(selectedRequest.getRequiredDateTime()) : "Not specified"),
                (selectedRequest.getAssignedTeam() != null ? selectedRequest.getAssignedTeam().getTeamName() : "Not yet assigned"),
                selectedRequest.getParentBundleRequestId(),
                (parent != null ? parent.getPatientName() : "Unknown"),
                (parent != null ? parent.getSurgeryType() : "Unknown"),
                (selectedRequest.getSender() != null ? selectedRequest.getSender().getUsername() : "Unknown")
            );
            JOptionPane.showMessageDialog(this, details, "Task Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnViewDetailsActionPerformed

    private MedicalTeam findAvailableTeam(String specialization, Date requiredDate) {
        if (!(organization instanceof DoctorOrganization)) {
            System.err.println("Manager's organization is not DoctorOrganization, cannot find teams.");
            return null;
        }

        DoctorOrganization docOrg = (DoctorOrganization) organization;
        MedicalTeamDirectory teamDir = docOrg.getMedicalTeamDirectory();

        if (teamDir == null) {
            System.err.println("Organization: " + organization.getName() + " MedicalTeamDirectory is null.");
            return null;
        }

        Date requiredStartDate = requiredDate;
        if (requiredStartDate == null) {
            System.err.println("Required date for team assignment is null. Cannot check availability.");
            return null;
        }

        // 设置开始时间为当天开始
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(requiredStartDate);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        requiredStartDate = startCal.getTime();

        // 设置结束时间为当天结束
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(requiredStartDate);
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        Date requiredEndDate = endCal.getTime();

        System.out.println("Searching for team with spec '" + specialization + "' available from " + requiredStartDate + " to " + requiredEndDate);

        for (MedicalTeam team : teamDir.getTeamList()) {
            System.out.println("Checking team: " + team.getTeamName() + ", Spec: " + team.getSpecialization() + ", Manager: " + (team.getTeamManager() != null ? team.getTeamManager().getName() : "null"));

            // 检查是否由当前用户管理AND匹配专业
            if (team.getTeamManager() != null && userAccount.getEmployee().equals(team.getTeamManager()) &&
                specialization.equalsIgnoreCase(team.getSpecialization()))
            {
                System.out.println(" -> Team matches manager and specialization. Checking availability...");

                try {
                    // 假设isTeamAvailable接收开始、结束时间和可能需要的成员（此处为null）
                    if (team.isTeamAvailable(requiredStartDate, requiredEndDate, null)) {
                        System.out.println(" --> Team " + team.getTeamName() + " is available!");
                        return team; // 找到可用团队
                    } else {
                        System.out.println(" --> Team " + team.getTeamName() + " is NOT available at the required time.");
                    }
                } catch (Exception e) {
                    System.err.println("Error checking availability for team " + team.getTeamName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        System.out.println("No available team found for spec '" + specialization + "' managed by " + userAccount.getUsername());
        return null; // 没有找到合适且可用的团队
    }

    // Helper method to find the parent BundleReservationWorkRequest
    private BundleReservationWorkRequest findParentBundleRequest(String parentId) {
        if (parentId == null || parentId.isEmpty()) {
            System.err.println("findParentBundleRequest called with null or empty parentId.");
            return null;
        }
        
        System.out.println("Searching for parent Bundle Request with ID: " + parentId);

        // Find PHS Enterprise
        if (phsEnterprise == null) {
            System.err.println("PHS Enterprise is null, cannot search for parent bundle request.");
            return null;
        }

        // Check the organizations within the PHS Enterprise
        for (Organization org : phsEnterprise.getOrganizationDirectory().getOrganizationList()) {
            System.out.println(" Checking Org: " + org.getName() + " Type: " + org.getClass().getSimpleName());
            
            if (org.getWorkQueue() != null && org.getWorkQueue().getWorkRequestList() != null) {
                for (WorkRequest wr : org.getWorkQueue().getWorkRequestList()) {
                    if (wr instanceof BundleReservationWorkRequest) {
                        BundleReservationWorkRequest brwr = (BundleReservationWorkRequest) wr;
                        System.out.println("   Found BundleReservationWorkRequest with ID: " + brwr.getBundleIdFromTemplate());
                        
                        // Use equalsIgnoreCase for robustness
                        if (parentId.equalsIgnoreCase(brwr.getBundleIdFromTemplate())) {
                            System.out.println("   --> Match found!");
                            return brwr;
                        }
                    }
                }
            } else {
                System.out.println("  WorkQueue or WorkRequestList is null for " + org.getName());
            }
        }

        System.err.println("Parent Bundle Request with ID " + parentId + " not found in searched queues.");
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAssign;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnViewDetails;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblInProgressTasks;
    private javax.swing.JLabel lblMedicalTeam;
    private javax.swing.JLabel lblPendingTasks;
    private javax.swing.JLabel lblTeamName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblInProgressTasks;
    private javax.swing.JTable tblMedicalTeam;
    private javax.swing.JTable tblPendingTasks;
    // End of variables declaration//GEN-END:variables
}
