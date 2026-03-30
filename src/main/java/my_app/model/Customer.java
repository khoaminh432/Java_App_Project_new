package my_app.model;

import java.util.Map;

public class Customer {
    private Integer id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private String status;
    public static int customerCount = 0;
    // Constructors
    public Customer() {customerCount++;}
    
    public Customer(Integer id, String fullName, String phoneNumber, 
                   String email, String password, String status) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.status = status;
        customerCount++;
    }
    public Customer(String fullName, String phoneNumber, 
                   String email, String password, String status) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.status = status;
        customerCount++;
    }
    public Customer(Customer other) {
        
        this.id = other.id;
        this.fullName = other.fullName;
        this.phoneNumber = other.phoneNumber;
        this.email = other.email;
        this.password = other.password;
        this.status = other.status;
        customerCount++;
    }
    public Customer(Map<String, Object> data) {
        applyFromMap(data);
        customerCount++;
    }
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void applyFromMap(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }

        Integer newId = ModelMapperHelper.getInteger(data, "id");
        if (newId != null) {
            this.id = newId;
        }

        String newFullName = ModelMapperHelper.getString(data, "fullName", "full_name", "full_Name");
        if (newFullName != null) {
            this.fullName = newFullName;
        }

        String newPhone = ModelMapperHelper.getString(data, "phoneNumber", "phone_number", "phone_Number");
        if (newPhone != null) {
            this.phoneNumber = newPhone;
        }

        String newEmail = ModelMapperHelper.getString(data, "email");
        if (newEmail != null) {
            this.email = newEmail;
        }

        String newPassword = ModelMapperHelper.getString(data, "password");
        if (newPassword != null) {
            this.password = newPassword;
        }

        String newStatus = ModelMapperHelper.getString(data, "status");
        if (newStatus != null) {
            this.status = newStatus;
        }
    }
    
    @Override
    public String toString() {
        return "Customer{" +
               "id=" + id +
               ", fullName='" + fullName + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", email='" + email + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}