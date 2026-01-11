/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Role;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization; // Assuming teams belong to an org
import Business.UserAccount.UserAccount;
import javax.swing.JPanel;
import ui.MedicalTeamManagerRole.MedicalTeamManagerWorkAreaJPanel;


/**
 * 
 * @author Di
 */
public class MedicalTeamManagerRole extends Role {

    @Override
    public JPanel createWorkArea(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise,  Network network,EcoSystem business) {
        return new MedicalTeamManagerWorkAreaJPanel(userProcessContainer, account, organization, enterprise, business);
    }

    @Override
    public String toString() {
        return RoleType.MedicalTeamManager.getValue();
    }
}
