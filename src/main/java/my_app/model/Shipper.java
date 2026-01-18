package my_app.model;

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