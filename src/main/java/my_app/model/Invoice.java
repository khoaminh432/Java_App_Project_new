package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class Invoice {
    private Integer id;
    private Integer customerId;
    private Integer employeeId;
    private Integer orderId;
    private Integer paymentMethodId;
    private LocalDateTime issuedDate;
    private BigDecimal totalAmount;
    private Customer customer; // Reference to Customer object
    private Employee employee; // Reference to Employee object
    private Order order; // Reference to Order object
    private PaymentMethod paymentMethod; // Reference to PaymentMethod object
    
    // Constructors
    public Invoice() {}
    
    public Invoice(Integer id, Integer customerId, Integer employeeId,
                  Integer orderId, Integer paymentMethodId, 
                  LocalDateTime issuedDate, BigDecimal totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderId = orderId;
        this.paymentMethodId = paymentMethodId;
        this.issuedDate = issuedDate;
        this.totalAmount = totalAmount;
    }

    public Invoice(Integer customerId, Integer employeeId,
                  Integer orderId, Integer paymentMethodId,
                  LocalDateTime issuedDate, BigDecimal totalAmount) {
        this(null, customerId, employeeId, orderId, paymentMethodId, issuedDate, totalAmount);
    }

    public Invoice(Invoice other) {
        this(other.id, other.customerId, other.employeeId, other.orderId, other.paymentMethodId,
             other.issuedDate, other.totalAmount);
        this.customer = other.customer;
        this.employee = other.employee;
        this.order = other.order;
        this.paymentMethod = other.paymentMethod;
    }
    public Invoice(Map<String, Object> data) {
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

        Integer newCustomerId = ModelMapperHelper.getInteger(data, "customerId", "customer_id");
        if (newCustomerId != null) {
            this.customerId = newCustomerId;
        }

        Integer newEmployeeId = ModelMapperHelper.getInteger(data, "employeeId", "employee_id");
        if (newEmployeeId != null) {
            this.employeeId = newEmployeeId;
        }

        Integer newOrderId = ModelMapperHelper.getInteger(data, "orderId", "order_id");
        if (newOrderId != null) {
            this.orderId = newOrderId;
        }

        Integer newPaymentMethodId = ModelMapperHelper.getInteger(data, "paymentMethodId", "payment_method_id");
        if (newPaymentMethodId != null) {
            this.paymentMethodId = newPaymentMethodId;
        }

        LocalDateTime newIssuedDate = ModelMapperHelper.getLocalDateTime(data, "issuedDate", "issued_date");
        if (newIssuedDate != null) {
            this.issuedDate = newIssuedDate;
        }

        BigDecimal newTotalAmount = ModelMapperHelper.getBigDecimal(data, "totalAmount", "total_amount");
        if (newTotalAmount != null) {
            this.totalAmount = newTotalAmount;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    
    public Integer getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(Integer paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    
    public LocalDateTime getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDateTime issuedDate) { this.issuedDate = issuedDate; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    @Override
    public String toString() {
        return "Invoice{" +
               "id=" + id +
               ", customerId=" + customerId +
               ", employeeId=" + employeeId +
               ", orderId=" + orderId +
               ", issuedDate=" + issuedDate +
               ", totalAmount=" + totalAmount +
               '}';
    }
}