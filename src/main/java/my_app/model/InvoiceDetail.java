package my_app.model;

import java.math.BigDecimal;
import java.util.Map;

public class InvoiceDetail {
    private Integer id;
    private Integer invoiceId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Invoice invoice; // Reference to Invoice object
    private Product product; // Reference to Product object
    
    // Constructors
    public InvoiceDetail() {}
    
    public InvoiceDetail(Integer id, Integer invoiceId, Integer productId,
                        Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public InvoiceDetail(Integer invoiceId, Integer productId,
                        Integer quantity, BigDecimal unitPrice) {
        this(null, invoiceId, productId, quantity, unitPrice);
    }

    public InvoiceDetail(InvoiceDetail other) {
        this(other.id, other.invoiceId, other.productId, other.quantity, other.unitPrice);
        this.invoice = other.invoice;
        this.product = other.product;
    }
    public InvoiceDetail(Map<String, Object> data) {
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

        Integer newInvoiceId = ModelMapperHelper.getInteger(data, "invoiceId", "invoice_id");
        if (newInvoiceId != null) {
            this.invoiceId = newInvoiceId;
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
    
    public Integer getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }
    
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    // Helper method to calculate line total
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    @Override
    public String toString() {
        return "InvoiceDetail{" +
               "id=" + id +
               ", invoiceId=" + invoiceId +
               ", productId=" + productId +
               ", quantity=" + quantity +
               ", unitPrice=" + unitPrice +
               '}';
    }
}