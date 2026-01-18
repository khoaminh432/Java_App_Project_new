package my_app.model;

import java.math.BigDecimal;

public class GoodsReceiptDetail {
    private Integer id;
    private Integer receiptId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private GoodsReceipt goodsReceipt; // Reference to GoodsReceipt object
    private Product product; // Reference to Product object
    
    // Constructors
    public GoodsReceiptDetail() {}
    
    public GoodsReceiptDetail(Integer id, Integer receiptId, Integer productId,
                             Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.receiptId = receiptId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getReceiptId() { return receiptId; }
    public void setReceiptId(Integer receiptId) { this.receiptId = receiptId; }
    
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public GoodsReceipt getGoodsReceipt() { return goodsReceipt; }
    public void setGoodsReceipt(GoodsReceipt goodsReceipt) { this.goodsReceipt = goodsReceipt; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    // Helper method to calculate line total
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    @Override
    public String toString() {
        return "GoodsReceiptDetail{" +
               "id=" + id +
               ", receiptId=" + receiptId +
               ", productId=" + productId +
               ", quantity=" + quantity +
               ", unitPrice=" + unitPrice +
               '}';
    }
}