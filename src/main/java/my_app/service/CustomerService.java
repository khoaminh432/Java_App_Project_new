package my_app.service;

import my_app.model.Customer;
import my_app.dao.CustomerDao;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerService {
    private CustomerDao customerDao = new CustomerDao();
    
    // 1. THÊM (Add)
    public void addCustomer(Customer customer) {
        if (customer != null && customer.getEmail() != null) {
            customerDao.create(customer);
            System.out.println("✓ Thêm khách hàng thành công: " + customer.getFullName());
        } else {
            System.out.println("✗ Dữ liệu khách hàng không hợp lệ");
        }
    }
    
    // 2. XÓA (Delete)
    public boolean deleteCustomer(Integer id) {
        int result = customerDao.delete(id);
        if (result > 0) {
            System.out.println("✓ Xóa khách hàng ID " + id + " thành công");
            return true;
        } else {
            System.out.println("✗ Không tìm thấy khách hàng ID " + id);
            return false;
        }
    }
    
    public boolean deleteCustomerByEmail(String email) {
        Customer customer = findCustomerByEmail(email);
        if (customer != null) {
            return deleteCustomer(customer.getId());
        }
        return false;
    }
    
    // 3. SỬA (Update)
    public boolean updateCustomer(Integer id, Customer updatedData) {
        Customer existing = findCustomerById(id);
        if (existing != null) {
            updatedData.setId(id);
            int result = customerDao.update(updatedData);
            if (result > 0) {
                System.out.println("✓ Cập nhật khách hàng ID " + id + " thành công");
                return true;
            }
        }
        System.out.println("✗ Không tìm thấy khách hàng ID " + id);
        return false;
    }
    
    // 4. TÌM KIẾM (Search)
    public Customer findCustomerById(Integer id) {
        return customerDao.findById(id);
    }
    
    public Customer findCustomerByEmail(String email) {
        List<Customer> all = getAllCustomers();
        return all.stream()
                .filter(c -> email != null && email.equals(c.getEmail()))
                .findFirst()
                .orElse(null);
    }
    
    public List<Customer> searchByName(String name) {
        List<Customer> all = getAllCustomers();
        return all.stream()
                .filter(c -> c.getFullName() != null && 
                            c.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Customer> searchByStatus(String status) {
        List<Customer> all = getAllCustomers();
        return all.stream()
                .filter(c -> status != null && status.equals(c.getStatus()))
                .collect(Collectors.toList());
    }
    
    public List<Customer> searchByPhoneNumber(String phone) {
        List<Customer> all = getAllCustomers();
        return all.stream()
                .filter(c -> phone != null && phone.equals(c.getPhoneNumber()))
                .collect(Collectors.toList());
    }
    
    // 5. THỐNG KÊ (Statistics)
    public void printStatistics() {
        System.out.println("\n========== THỐNG KÊ KHÁCH HÀNG ==========");
        System.out.println("Tổng số khách hàng: " + getTotalCustomers());
        System.out.println("Khách hàng hoạt động: " + getActiveCustomersCount());
        System.out.println("Khách hàng không hoạt động: " + getInactiveCustomersCount());
        System.out.println("=========================================\n");
    }
    
    public int getTotalCustomers() {
        return getAllCustomers().size();
    }
    
    public long getActiveCustomersCount() {
        return getAllCustomers().stream()
                .filter(c -> "active".equalsIgnoreCase(c.getStatus()))
                .count();
    }
    
    public long getInactiveCustomersCount() {
        return getAllCustomers().stream()
                .filter(c -> "inactive".equalsIgnoreCase(c.getStatus()))
                .count();
    }
    
    public Map<String, Long> getCustomersByStatus() {
        return getAllCustomers().stream()
                .collect(Collectors.groupingBy(
                    c -> c.getStatus() != null ? c.getStatus() : "Unknown",
                    Collectors.counting()
                ));
    }
    
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }
    
    public void displayAllCustomers() {
        List<Customer> customers = getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("Không có khách hàng nào");
            return;
        }
        customers.forEach(System.out::println);
    }
}