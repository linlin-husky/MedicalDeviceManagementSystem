package Business.Employee;

import java.util.ArrayList;

public class EmployeeDirectory {

    private ArrayList<Employee> employeeList;

    public EmployeeDirectory() {
        employeeList = new ArrayList<>();
    }

    public ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }

    public Employee createEmployee(String name){
        Employee employee = new Employee();
        employee.setName(name);
        employeeList.add(employee);
        return employee;
    }

    public Employee findEmployeeById(int id) {
        for (Employee employee : employeeList) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    public boolean removeEmployee(Employee employee) {
        return employeeList.remove(employee);
    }
}