package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Shipper extends Employee {
    private String vehiclePlateNumber;
    private String currentStatus;
    
    // Constructors
    public Shipper() {}
    
    public Shipper(Employee employee, String vehiclePlateNumber, String currentStatus) {
        super(employee.getId(), employee.getFirstName(), employee.getLastName(),
              employee.getPhoneNumber(), employee.getDob(), employee.getAddress(),
              employee.getBasicSalary(), employee.getStatus(), employee.getRoleId());
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.currentStatus = currentStatus;
    }

    public Shipper(Integer id, String firstName, String lastName, String phoneNumber,
                   LocalDate dob, String address, BigDecimal basicSalary, String status,
                   Integer roleId, String vehiclePlateNumber, String currentStatus) {
        super(id, firstName, lastName, phoneNumber, dob, address, basicSalary, status, roleId);
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.currentStatus = currentStatus;
    }

    public Shipper(String firstName, String lastName, String phoneNumber,
                   LocalDate dob, String address, BigDecimal basicSalary, String status,
                   Integer roleId, String vehiclePlateNumber, String currentStatus) {
        this(null, firstName, lastName, phoneNumber, dob, address, basicSalary, status,
             roleId, vehiclePlateNumber, currentStatus);
    }

    public Shipper(Shipper other) {
        this(other.getId(), other.getFirstName(), other.getLastName(), other.getPhoneNumber(),
             other.getDob(), other.getAddress(), other.getBasicSalary(), other.getStatus(),
             other.getRoleId(), other.vehiclePlateNumber, other.currentStatus);
        setRole(other.getRole());
    }
    
    // Getters and Setters
    public String getVehiclePlateNumber() { return vehiclePlateNumber; }
    public void setVehiclePlateNumber(String vehiclePlateNumber) { this.vehiclePlateNumber = vehiclePlateNumber; }
    
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    
    @Override
    public String toString() {
        return "Shipper{" +
               "id=" + getId() +
               ", fullName='" + getFullName() + '\'' +
               ", phoneNumber='" + getPhoneNumber() + '\'' +
               ", vehiclePlateNumber='" + vehiclePlateNumber + '\'' +
               ", currentStatus='" + currentStatus + '\'' +
               '}';
    }
}