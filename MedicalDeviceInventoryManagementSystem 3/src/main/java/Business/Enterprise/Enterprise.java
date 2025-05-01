package Business.Enterprise;

import Business.Organization.Organization;
import Business.Organization.OrganizationDirectory;

public abstract class Enterprise extends Organization{

    private EnterpriseType enterpriseType;
    private OrganizationDirectory organizationDirectory;

    public Enterprise(String name, EnterpriseType type){
        super(name);
        this.enterpriseType = type;
        this.organizationDirectory = new OrganizationDirectory();
    }

    public enum EnterpriseType{
        Hospital("Hospital"),
        Supply("Supply"),
        Logistics("Logistics"),
        PHS("PHS");

        private final String value;

        private EnterpriseType(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString(){
            return value;
        }
    }

    public EnterpriseType getEnterpriseType() {
        return enterpriseType;
    }

    public OrganizationDirectory getOrganizationDirectory() {
        return organizationDirectory;
    }

}