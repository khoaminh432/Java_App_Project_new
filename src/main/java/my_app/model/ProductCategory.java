package my_app.model;

import java.util.Map;

public class ProductCategory {
    private Integer id;
    private String categoryName;
    private String description;
    
    // Constructors
    public ProductCategory() {}
    
    public ProductCategory(Integer id, String categoryName, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
    }

    public ProductCategory(String categoryName, String description) {
        this(null, categoryName, description);
    }

    public ProductCategory(ProductCategory other) {
        this(other.id, other.categoryName, other.description);
    }
    public ProductCategory(Map<String, Object> data) {
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

        String newCategoryName = ModelMapperHelper.getString(data, "categoryName", "category_name");
        if (newCategoryName != null) {
            this.categoryName = newCategoryName;
        }

        String newDescription = ModelMapperHelper.getString(data, "description");
        if (newDescription != null) {
            this.description = newDescription;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return "ProductCategory{" +
               "id=" + id +
               ", categoryName='" + categoryName + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}