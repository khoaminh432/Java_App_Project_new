package my_app.model;

import java.math.BigDecimal;
import java.util.Map;

public class Product {
    private Integer id;
    private String productName;
    private BigDecimal unitPrice;
    private String unit;
    private Integer quantity;
    private String status;
    private Integer categoryId;
    private ProductCategory category; // Reference to ProductCategory object
    
    // Constructors
    public Product() {}
    
    public Product(Integer id, String productName, BigDecimal unitPrice, 
                  String unit, Integer quantity, String status, Integer categoryId) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.quantity = quantity;
        this.status = status;
        this.categoryId = categoryId;
    }

    public Product(String productName, BigDecimal unitPrice,
                  String unit, Integer quantity, String status, Integer categoryId) {
        this(null, productName, unitPrice, unit, quantity, status, categoryId);
    }

    public Product(Product other) {
        this(other.id, other.productName, other.unitPrice, other.unit, other.quantity,
             other.status, other.categoryId);
        this.category = other.category;
    }

    public Product(Map<String, Object> data) {
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

        String newProductName = ModelMapperHelper.getString(data, "productName", "product_name");
        if (newProductName != null) {
            this.productName = newProductName;
        }

        BigDecimal newUnitPrice = ModelMapperHelper.getBigDecimal(data, "unitPrice", "unit_price");
        if (newUnitPrice != null) {
            this.unitPrice = newUnitPrice;
        }

        String newUnit = ModelMapperHelper.getString(data, "unit");
        if (newUnit != null) {
            this.unit = newUnit;
        }

        Integer newQuantity = ModelMapperHelper.getInteger(data, "quantity");
        if (newQuantity != null) {
            this.quantity = newQuantity;
        }

        String newStatus = ModelMapperHelper.getString(data, "status");
        if (newStatus != null) {
            this.status = newStatus;
        }

        Integer newCategoryId = ModelMapperHelper.getInteger(data, "categoryId", "category_id");
        if (newCategoryId != null) {
            this.categoryId = newCategoryId;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    
    public ProductCategory getCategory() { return category; }
    public void setCategory(ProductCategory category) { this.category = category; }
    
    // Helper method to calculate total value
    public BigDecimal getTotalValue() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    @Override
    public String toString() {
        return "Product{" +
               "id=" + id +
               ", productName='" + productName + '\'' +
               ", unitPrice=" + unitPrice +
               ", unit='" + unit + '\'' +
               ", quantity=" + quantity +
               ", status='" + status + '\'' +
               ", categoryId=" + categoryId +
               '}';
    }
}