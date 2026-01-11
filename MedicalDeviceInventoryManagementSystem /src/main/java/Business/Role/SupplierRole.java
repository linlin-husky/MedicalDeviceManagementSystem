/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Role;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.DoctorOrganization;
import Business.Organization.Organization;
import Business.Organization.SupplierOrganization;
import Business.UserAccount.UserAccount;
import javax.swing.JPanel;
import ui.DoctorRole.ManageOperationsJPanel;
import ui.SupplierRole.SupplierWorkAreaJPanel;

/**
 *
 * @author linlinfan
 */
public class SupplierRole extends Role {
    @Override
    public JPanel createWorkArea(JPanel userProcessContainer, UserAccount account, Organization organization,
            Enterprise enterprise, Network network, EcoSystem business) {

        // Supplier supplier = account.getEmployee().getSupplier();
        return new SupplierWorkAreaJPanel(userProcessContainer, account, (SupplierOrganization) organization,
                enterprise, network,business);
    }

}