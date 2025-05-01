package Business.Enterprise;

import Business.Role.Role;
import Business.WorkQueue.WorkQueue;
import Business.models.bundle.ResourceBundle;
import Business.models.bundle.ResourceBundleDirectory;
import java.util.ArrayList;
import java.util.List;

public class PHSEnterprise extends Enterprise {

    private ResourceBundleDirectory globalBundleDirectory;
    private WorkQueue sharedTeamAssignmentWorkQueue;

    public PHSEnterprise(String name) {
        super(name, EnterpriseType.PHS);
        globalBundleDirectory = new ResourceBundleDirectory();
        sharedTeamAssignmentWorkQueue = new WorkQueue();
    }

    public ResourceBundleDirectory getGlobalBundleDirectory() {
        return globalBundleDirectory;
    }

    public void setGlobalBundleDirectory(ResourceBundleDirectory globalBundleDirectory) {
        this.globalBundleDirectory = globalBundleDirectory;
    }

    public List<ResourceBundle> getGlobalBundles() {
        if (globalBundleDirectory != null) {
            return globalBundleDirectory.getBundleList();
        }
        return new ArrayList<>();
    }
    
    public WorkQueue getSharedTeamAssignmentWorkQueue() {
        return sharedTeamAssignmentWorkQueue;
    }

    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList<>();
        return roles;
    }
}