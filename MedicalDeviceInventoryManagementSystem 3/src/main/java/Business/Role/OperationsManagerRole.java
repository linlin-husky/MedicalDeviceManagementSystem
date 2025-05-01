/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Role;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.OperationsManagementOrganization;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import javax.swing.JPanel;
import ui.OperationsManagerRole.OperationsManagerWorkAreaJPanel;

/**
 *
 * @author rajath
 */
public class OperationsManagerRole extends Role{
    
    @Override
    public JPanel createWorkArea(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise,  Network network,EcoSystem business) {
        return new OperationsManagerWorkAreaJPanel(userProcessContainer, account, (OperationsManagementOrganization)organization, enterprise, business);
    }
    
}
