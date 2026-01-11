package ui.SupplierRole;

import Business.EcoSystem;
import Business.Employee.Employee;

import Business.Enterprise.Enterprise;

import Business.Organization.SupplierOrganization;
import Business.UserAccount.UserAccount;
import Business.models.device.Device;



import java.awt.CardLayout;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rushabh
 */
public class ManageDeviceCatalogJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ManageProductCatalogJPanel
     */
 
 
    private JPanel userProcessContainer;
    private UserAccount userAccount;
    private SupplierOrganization organization;
    private Enterprise  enterprise;
//   
    

    public ManageDeviceCatalogJPanel(JPanel userProcessContainer, UserAccount account, SupplierOrganization organization, Enterprise enterprise) {
        initComponents();
        this.userProcessContainer = userProcessContainer;
        this.userAccount = account;
        this.organization = organization;
        this.enterprise = enterprise;

         
        lblSupplier.setText("Supplier : " + enterprise.getName());
        refreshTable();
    }

    public void refreshTable() {

        DefaultTableModel model = (DefaultTableModel) tblProductCatalog.getModel();
        model.setRowCount(0);
        
        for (Device d : organization.getDeviceDirectory().getDeviceList()) {
            Object row[] = new Object[2];
            row[0] = d;
            row[1] = d.getDeviceID();

          
            model.addRow(row);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblSupplier = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductCatalog = new javax.swing.JTable();
        btnView = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(650, 600));

        lblTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTitle.setText("Manage Device Catalog");

        lblSupplier.setText("enterprise:");

        tblProductCatalog.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        tblProductCatalog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Device Name", "Device ID"
            }
        ));
        jScrollPane1.setViewportView(tblProductCatalog);

        btnView.setText("View Details...");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnCreate.setText("New Device");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnSearch.setText("Search...");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete Device");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(lblTitle))
                    .addComponent(lblSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnBack))
                .addGap(18, 18, 18)
                .addComponent(lblSupplier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreate)
                    .addComponent(btnView)
                    .addComponent(btnSearch)
                    .addComponent(btnDelete))
                .addContainerGap(309, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed

        int selectedRowIndex = tblProductCatalog.getSelectedRow();
        if (selectedRowIndex < 0) {
            JOptionPane.showMessageDialog(null, "Pls select a row!!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
        Device d = (Device) tblProductCatalog.getValueAt(selectedRowIndex, 0);
        
       
        
        ViewDeviceDetailsJPanel vpdjp = new ViewDeviceDetailsJPanel(userProcessContainer, organization, d);
        userProcessContainer.add("ViewProductDetailJPanel", vpdjp);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed

        CreateNewDeviceJPanel cnpjp = new CreateNewDeviceJPanel(userProcessContainer,  organization, userAccount,enterprise);
        userProcessContainer.add("CreateNewProductJPanel", cnpjp);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

        SearchForDeviceJPanel sfpjp = new SearchForDeviceJPanel(userProcessContainer,organization, userAccount, enterprise);
        userProcessContainer.add("SearchForProductJPanel", sfpjp);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed

        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        int selectedRowIndex = tblProductCatalog.getSelectedRow();

        if (selectedRowIndex < 0) {
            JOptionPane.showMessageDialog(null, "Please select a row from the table first", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
       Device d = (Device) tblProductCatalog.getValueAt(selectedRowIndex, 0);
       organization.getDeviceDirectory().getDeviceList().remove(d);
        refreshTable();
    }//GEN-LAST:event_btnDeleteActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnView;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSupplier;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblProductCatalog;
    // End of variables declaration//GEN-END:variables
}
