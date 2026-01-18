package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Integer id;
    private Integer customerId;
    private LocalDateTime orderDate;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private String status;
    private Customer customer; // Reference to Customer object
    
    // Constructors
    public Order() {}
    
    public Order(Integer id, Integer customerId, LocalDateTime orderDate,
                BigDecimal subTotal, BigDecimal totalAmount, String status) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    @Override
    public String toString() {
        return "Order{" +
               "id=" + id +
               ", customerId=" + customerId +
               ", orderDate=" + orderDate +
               ", subTotal=" + subTotal +
               ", totalAmount=" + totalAmount +
               ", status='" + status + '\'' +
               '}';
    }
}