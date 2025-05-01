package Business.Employee;

public class Employee {

    private String name;
    private final int id;
    private static int count = 1;

    public Employee() {
        id = count++;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
             this.name = name.trim();
        } else {
             System.err.println("Attempted to set null or empty employee name.");
             this.name = "Unnamed Employee";
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name != null ? name : ("Employee ID: " + id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}