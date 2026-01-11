package Business.models.bundle;

import java.util.ArrayList;
import java.util.List;

public class ResourceBundleDirectory {
    private List<ResourceBundle> bundleList;

    public ResourceBundleDirectory() {
        bundleList = new ArrayList<>();
    }

    public List<ResourceBundle> getBundleList() {
        return bundleList;
    }

    public ResourceBundle createBundle(String name, String surgeryType) {
        ResourceBundle bundle = new ResourceBundle();
        bundle.setName(name);
        bundle.setSurgeryType(surgeryType);
        bundleList.add(bundle);
        return bundle;
    }
    
    public ResourceBundle createNewBundle() {
        ResourceBundle bundle = new ResourceBundle();
        bundleList.add(bundle);
        return bundle;
    }


    public void addBundle(ResourceBundle bundle) {
        if (bundle != null && !bundleList.contains(bundle)) {
             bundleList.add(bundle);
        }
    }

    public boolean removeBundle(ResourceBundle bundle) {
        if (bundle != null) {
           return bundleList.remove(bundle);
        }
        return false;
    }

    public ResourceBundle findBundleById(String bundleId) {
        if (bundleId == null || bundleId.trim().isEmpty()) {
            return null;
        }
        for (ResourceBundle bundle : bundleList) {
            if (bundle.getBundleId() != null && bundle.getBundleId().equals(bundleId)) {
                return bundle;
            }
        }
        return null;
    }

    public List<ResourceBundle> findBundlesBySurgeryType(String surgeryType) {
        List<ResourceBundle> foundBundles = new ArrayList<>();
        if (surgeryType == null || surgeryType.trim().isEmpty()) {
            return foundBundles;
        }
        String searchTerm = surgeryType.trim().toLowerCase();
        for (ResourceBundle bundle : bundleList) {
            if (bundle.getSurgeryType() != null && bundle.getSurgeryType().toLowerCase().contains(searchTerm)) {
                foundBundles.add(bundle);
            }
        }
        return foundBundles;
    }
}