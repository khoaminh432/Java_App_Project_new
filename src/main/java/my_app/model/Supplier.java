package my_app.model;

import java.util.Map;

public class Supplier {
    private Integer id;
    private String supplierName;
    private String address;
    private String phoneNumber;
    
    // Constructors
    public Supplier() {}
    
    public Supplier(Integer id, String supplierName, String address, String phoneNumber) {
        this.id = id;
        this.supplierName = supplierName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Supplier(String supplierName, String address, String phoneNumber) {
        this(null, supplierName, address, phoneNumber);
    }

    public Supplier(Supplier other) {
        this(other.id, other.supplierName, other.address, other.phoneNumber);
    }
    public Supplier(Map<String, Object> data) {
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

        String newSupplierName = ModelMapperHelper.getString(data, "supplierName", "supplier_name");
        if (newSupplierName != null) {
            this.supplierName = newSupplierName;
        }

        String newAddress = ModelMapperHelper.getString(data, "address");
        if (newAddress != null) {
            this.address = newAddress;
        }

        String newPhoneNumber = ModelMapperHelper.getString(data, "phoneNumber", "phone_number");
        if (newPhoneNumber != null) {
            this.phoneNumber = newPhoneNumber;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    @Override
    public String toString() {
        return "Supplier{" +
               "id=" + id +
               ", supplierName='" + supplierName + '\'' +
               ", address='" + address + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               '}';
    }
}