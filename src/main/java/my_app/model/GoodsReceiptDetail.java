package my_app.model;

import java.math.BigDecimal;
import java.util.Map;

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

    public GoodsReceiptDetail(Integer receiptId, Integer productId,
                             Integer quantity, BigDecimal unitPrice) {
        this(null, receiptId, productId, quantity, unitPrice);
    }

    public GoodsReceiptDetail(GoodsReceiptDetail other) {
        this(other.id, other.receiptId, other.productId, other.quantity, other.unitPrice);
        this.goodsReceipt = other.goodsReceipt;
        this.product = other.product;
    }
    public GoodsReceiptDetail(Map<String, Object> data) {
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

        Integer newReceiptId = ModelMapperHelper.getInteger(data, "receiptId", "receipt_id");
        if (newReceiptId != null) {
            this.receiptId = newReceiptId;
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