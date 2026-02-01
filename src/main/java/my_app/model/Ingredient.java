package my_app.model;

import java.util.Map;

public class Ingredient {

    private Integer id;
    private String ingredientName;
    private Integer netWeight;
    private Integer quantity;

    public Ingredient() {
    }

    public Ingredient(Integer id, String ingredientName, Integer netWeight, Integer quantity) {
        this.id = id;
        this.ingredientName = ingredientName;
        this.netWeight = netWeight;
        this.quantity = quantity;
    }

    public Ingredient(String ingredientName, Integer netWeight, Integer quantity) {
        this(null, ingredientName, netWeight, quantity);
    }

    public Ingredient(Ingredient other) {
        this(other.id, other.ingredientName, other.netWeight, other.quantity);
    }

    public Ingredient(Map<String, Object> data) {
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

        String newIngredientName = ModelMapperHelper.getString(data, "ingredientName", "ingredient_name");
        if (newIngredientName != null) {
            this.ingredientName = newIngredientName;
        }

        Integer newNetWeight = ModelMapperHelper.getInteger(data, "netWeight", "net_weight");
        if (newNetWeight != null) {
            this.netWeight = newNetWeight;
        }

        Integer newQuantity = ModelMapperHelper.getInteger(data, "quantity");
        if (newQuantity != null) {
            this.quantity = newQuantity;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Integer getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Integer netWeight) {
        this.netWeight = netWeight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Ingredient{"
                + "id=" + id
                + ", ingredientName='" + ingredientName + '\''
                + ", netWeight=" + netWeight
                + ", quantity=" + quantity
                + '}';
    }
}
