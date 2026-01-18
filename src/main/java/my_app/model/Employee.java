package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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