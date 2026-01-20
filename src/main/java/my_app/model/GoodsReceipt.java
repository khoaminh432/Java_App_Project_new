package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class GoodsReceipt {
    private Integer id;
    private LocalDateTime receivedDate;
    private Integer supplierId;
    private Integer totalQuantity;
    private BigDecimal totalPrice;
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

    public GoodsReceipt(LocalDateTime receivedDate, Integer supplierId,
                       Integer totalQuantity, BigDecimal totalPrice) {
        this(null, receivedDate, supplierId, totalQuantity, totalPrice);
    }

    public GoodsReceipt(GoodsReceipt other) {
        this(other.id, other.receivedDate, other.supplierId, other.totalQuantity, other.totalPrice);
    }
    public GoodsReceipt(Map<String, Object> data) {
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

        LocalDateTime newReceivedDate = ModelMapperHelper.getLocalDateTime(data, "receivedDate", "received_date");
        if (newReceivedDate != null) {
            this.receivedDate = newReceivedDate;
        }

        Integer newSupplierId = ModelMapperHelper.getInteger(data, "supplierId", "supplier_id");
        if (newSupplierId != null) {
            this.supplierId = newSupplierId;
        }

        Integer newTotalQuantity = ModelMapperHelper.getInteger(data, "totalQuantity", "total_quantity");
        if (newTotalQuantity != null) {
            this.totalQuantity = newTotalQuantity;
        }

        BigDecimal newTotalPrice = ModelMapperHelper.getBigDecimal(data, "totalPrice", "total_price");
        if (newTotalPrice != null) {
            this.totalPrice = newTotalPrice;
        }
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