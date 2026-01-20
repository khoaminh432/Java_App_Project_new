package my_app.model;

import java.math.BigDecimal;
import java.util.Map;

public class OrderDetail {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Order order; // Reference to Order object
    private Product product; // Reference to Product object
    
    // Constructors
    public OrderDetail() {}
    
    public OrderDetail(Integer id, Integer orderId, Integer productId,
                      Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderDetail(Integer orderId, Integer productId,
                      Integer quantity, BigDecimal unitPrice) {
        this(null, orderId, productId, quantity, unitPrice);
    }

    public OrderDetail(OrderDetail other) {
        this(other.id, other.orderId, other.productId, other.quantity, other.unitPrice);
        this.order = other.order;
        this.product = other.product;
    }
    public OrderDetail(Map<String, Object> data) {
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

        Integer newOrderId = ModelMapperHelper.getInteger(data, "orderId", "order_id");
        if (newOrderId != null) {
            this.orderId = newOrderId;
        }

        Integer newProductId = ModelMapperHelper.getInteger(data, "productId", "product_id");
        if (newProductId != null) {
            this.productId = newProductId;
        }

        Integer newQuantity = ModelMapperHelper.getInteger(data, "quantity");
        if (newQuantity != null) {
            this.quantity = newQuantity;
        }

        BigDecimal newUnitPrice = ModelMapperHelper.getBigDecimal(data, "unitPrice", "unit_price");
        if (newUnitPrice != null) {
            this.unitPrice = newUnitPrice;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    // Helper method to calculate line total
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    @Override
    public String toString() {
        return "OrderDetail{" +
               "id=" + id +
               ", orderId=" + orderId +
               ", productId=" + productId +
               ", quantity=" + quantity +
               ", unitPrice=" + unitPrice +
               '}';
    }
}