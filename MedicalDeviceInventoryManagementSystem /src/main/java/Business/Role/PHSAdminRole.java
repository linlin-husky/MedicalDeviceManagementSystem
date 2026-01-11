package Business.Role;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import javax.swing.JPanel;
import ui.PHSAdminRole.PHSAdminWorkAreaJPanel;


public class PHSAdminRole extends Role {

    @Override
    public JPanel createWorkArea(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise,  Network network,EcoSystem business) {
        // --- 重点：确保这里返回的是 PHSAdminWorkAreaJPanel ---
        System.out.println(">>> PHSAdminRole.createWorkArea called! Returning PHSAdminWorkAreaJPanel..."); // 添加日志确认调用
        return new PHSAdminWorkAreaJPanel(userProcessContainer, account, organization, enterprise, business);
        // --- 结束重点 ---
    }

    @Override
    public String toString() {
        return RoleType.PHSAdmin.getValue();
    }

}