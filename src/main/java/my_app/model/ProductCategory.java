package my_app.model;

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