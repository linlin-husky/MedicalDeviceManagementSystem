package Business.Network;

import Business.Enterprise.EnterpriseDirectory;
import java.util.Objects;

public class Network {
    private String name;
    private EnterpriseDirectory enterpriseDirectory;

    public Network(){
        enterpriseDirectory=new EnterpriseDirectory();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnterpriseDirectory getEnterpriseDirectory() {
        return enterpriseDirectory;
    }

    @Override
    public String toString(){
        return name != null ? name : "Unnamed Network";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;
        return Objects.equals(name, network.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}