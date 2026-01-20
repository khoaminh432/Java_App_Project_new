package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dob;
    private String address;
    private BigDecimal basicSalary;
    private String status;
    private Integer roleId;
    private Role role; // Reference to Role object
    
    // Constructors
    public Employee() {}
    
    public Employee(Integer id, String firstName, String lastName, String phoneNumber,
                   LocalDate dob, String address, BigDecimal basicSalary, 
                   String status, Integer roleId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.address = address;
        this.basicSalary = basicSalary;
        this.status = status;
        this.roleId = roleId;
    }

    public Employee(String firstName, String lastName, String phoneNumber,
                   LocalDate dob, String address, BigDecimal basicSalary,
                   String status, Integer roleId) {
        this(null, firstName, lastName, phoneNumber, dob, address, basicSalary, status, roleId);
    }

    public Employee(Employee other) {
        this(other.id, other.firstName, other.lastName, other.phoneNumber, other.dob,
             other.address, other.basicSalary, other.status, other.roleId);
        this.role = other.role;
    }
    public Employee(Map<String, Object> data) {
        applyFromMap(data);
    }
    public void applyFromMap(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }

        Integer newId = ModelMapperHelper.getInteger(data, "id");
        if (newId != null) {
            this.id = newId;
        }

        String newFirstName = ModelMapperHelper.getString(data, "firstName", "first_name");
        if (newFirstName != null) {
            this.firstName = newFirstName;
        }

        String newLastName = ModelMapperHelper.getString(data, "lastName", "last_name");
        if (newLastName != null) {
            this.lastName = newLastName;
        }

        String newPhone = ModelMapperHelper.getString(data, "phoneNumber", "phone_number");
        if (newPhone != null) {
            this.phoneNumber = newPhone;
        }

        LocalDate newDob = ModelMapperHelper.getLocalDate(data, "dob", "date_of_birth");
        if (newDob != null) {
            this.dob = newDob;
        }

        String newAddress = ModelMapperHelper.getString(data, "address");
        if (newAddress != null) {
            this.address = newAddress;
        }

        BigDecimal newSalary = ModelMapperHelper.getBigDecimal(data, "basicSalary", "basic_salary");
        if (newSalary != null) {
            this.basicSalary = newSalary;
        }

        String newStatus = ModelMapperHelper.getString(data, "status");
        if (newStatus != null) {
            this.status = newStatus;
        }

        Integer newRoleId = ModelMapperHelper.getInteger(data, "roleId", "role_id");
        if (newRoleId != null) {
            this.roleId = newRoleId;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public BigDecimal getBasicSalary() { return basicSalary; }
    public void setBasicSalary(BigDecimal basicSalary) { this.basicSalary = basicSalary; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", dob=" + dob +
               ", address='" + address + '\'' +
               ", basicSalary=" + basicSalary +
               ", status='" + status + '\'' +
               ", roleId=" + roleId +
               '}';
    }
}