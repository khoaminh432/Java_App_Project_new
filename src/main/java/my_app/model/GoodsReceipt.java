package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GoodsReceipt {
    private Integer id;
    private LocalDateTime receivedDate;
    private Integer supplierId;
    private Integer totalQuantity;
    private BigDecimal totalPrice;
    private Supplier supplier; // Reference to Supplier object
    
    // Constructors
    public GoodsReceipt() {}
    
    public GoodsReceipt(Integer id, LocalDateTime receivedDate, Integer supplierId,
                       Integer totalQuantity, BigDecimal totalPrice) {
        this.id = id;
        this.receivedDate = receivedDate;
        this.supplierId = supplierId;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public LocalDateTime getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }
    
    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
    
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
    
    @Override
    public String toString() {
        return "GoodsReceipt{" +
               "id=" + id +
               ", receivedDate=" + receivedDate +
               ", supplierId=" + supplierId +
               ", totalQuantity=" + totalQuantity +
               ", totalPrice=" + totalPrice +
               '}';
    }
}