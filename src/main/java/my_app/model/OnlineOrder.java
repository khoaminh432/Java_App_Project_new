package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OnlineOrder extends Order {
    private Integer shipperId;
    private String receiverName;
    private String phoneNumber;
    private String address;
    private BigDecimal shippingFee;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime completedTime;
    private Shipper shipper; // Reference to Shipper object
    
    // Constructors
    public OnlineOrder() {}
    
    public OnlineOrder(Order order, Integer shipperId, String receiverName,
                      String phoneNumber, String address, BigDecimal shippingFee,
                      LocalDateTime estimatedDeliveryTime, LocalDateTime completedTime) {
        super(order.getId(), order.getCustomerId(), order.getOrderDate(),
              order.getSubTotal(), order.getTotalAmount(), order.getStatus());
        this.shipperId = shipperId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.shippingFee = shippingFee;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.completedTime = completedTime;
    }
    
    // Getters and Setters
    public Integer getShipperId() { return shipperId; }
    public void setShipperId(Integer shipperId) { this.shipperId = shipperId; }
    
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public BigDecimal getShippingFee() { return shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }
    
    public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }
    
    public LocalDateTime getCompletedTime() { return completedTime; }
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }
    
    public Shipper getShipper() { return shipper; }
    public void setShipper(Shipper shipper) { this.shipper = shipper; }
    
    // Calculate total including shipping fee
    public BigDecimal getTotalWithShipping() {
        return getTotalAmount().add(shippingFee);
    }
    
    @Override
    public String toString() {
        return "OnlineOrder{" +
               "id=" + getId() +
               ", customerId=" + getCustomerId() +
               ", orderDate=" + getOrderDate() +
               ", receiverName='" + receiverName + '\'' +
               ", address='" + address + '\'' +
               ", shippingFee=" + shippingFee +
               ", totalAmount=" + getTotalAmount() +
               ", status='" + getStatus() + '\'' +
               '}';
    }
}