package my_app.model;

import java.util.Map;

public class IngredientProduct {

    private Integer id;
    private Integer productId;
    private Integer ingredientId;
    private Integer estimate;
    private Product product;
    private Ingredient ingredient;

    public IngredientProduct() {
    }

    public IngredientProduct(Integer id, Integer productId, Integer ingredientId, Integer estimate) {
        this.id = id;
        this.productId = productId;
        this.ingredientId = ingredientId;
        this.estimate = estimate;
    }

    public IngredientProduct(Integer productId, Integer ingredientId, Integer estimate) {
        this(null, productId, ingredientId, estimate);
    }

    public IngredientProduct(IngredientProduct other) {
        this(other.id, other.productId, other.ingredientId, other.estimate);
        this.product = other.product;
        this.ingredient = other.ingredient;
    }

    public IngredientProduct(Map<String, Object> data) {
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

        Integer newProductId = ModelMapperHelper.getInteger(data, "productId", "product_id");
        if (newProductId != null) {
            this.productId = newProductId;
        }

        Integer newIngredientId = ModelMapperHelper.getInteger(data, "ingredientId", "ingredient_id");
        if (newIngredientId != null) {
            this.ingredientId = newIngredientId;
        }

        Integer newEstimate = ModelMapperHelper.getInteger(data, "estimate");
        if (newEstimate != null) {
            this.estimate = newEstimate;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "IngredientProduct{"
                + "id=" + id
                + ", productId=" + productId
                + ", ingredientId=" + ingredientId
                + ", estimate=" + estimate
                + '}';
    }
}
