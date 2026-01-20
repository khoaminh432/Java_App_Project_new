package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

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
    public Shipper(Map<String, Object> data) {
        applyFromMap(data);
    }
    public void applyFromMap(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        super.applyFromMap(data);

        String newVehiclePlate = ModelMapperHelper.getString(data, "vehiclePlateNumber", "vehicle_plate_number");
        if (newVehiclePlate != null) {
            this.vehiclePlateNumber = newVehiclePlate;
        }

        String newCurrentStatus = ModelMapperHelper.getString(data, "currentStatus", "current_status");
        if (newCurrentStatus != null) {
            this.currentStatus = newCurrentStatus;
        }
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