package my_app.model;

import java.math.BigDecimal;
import java.util.Map;

public class Ingredient {

    private Integer id;
    private String ingredientName;
    private Integer netWeight;
    private Integer quantity;
    private Integer totalWeight;
    private BigDecimal unitPrice;

    public Ingredient() {
    }

    public Ingredient(Integer id, String ingredientName, Integer netWeight, Integer quantity) {
        this(id, ingredientName, netWeight, quantity, null, null);
    }

    public Ingredient(Integer id, String ingredientName, Integer netWeight, Integer quantity, Integer totalWeight) {
        this(id, ingredientName, netWeight, quantity, totalWeight, null);
    }

    public Ingredient(Integer id, String ingredientName, Integer netWeight, Integer quantity, Integer totalWeight, BigDecimal unitPrice) {
        this.id = id;
        this.ingredientName = ingredientName;
        this.netWeight = netWeight;
        this.quantity = quantity;
        this.totalWeight = totalWeight != null ? totalWeight : calculateTotal(netWeight, quantity);
        this.unitPrice = unitPrice;
    }

    public Ingredient(String ingredientName, Integer netWeight, Integer quantity) {
        this(null, ingredientName, netWeight, quantity, null, null);
    }

    public Ingredient(Ingredient other) {
        this(other.id, other.ingredientName, other.netWeight, other.quantity, other.totalWeight, other.unitPrice);
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

        Integer newTotalWeight = ModelMapperHelper.getInteger(data, "totalWeight", "total_weight");
        if (newTotalWeight != null) {
            this.totalWeight = newTotalWeight;
        }

        BigDecimal newUnitPrice = ModelMapperHelper.getBigDecimal(data, "unitPrice", "unit_price");
        if (newUnitPrice != null) {
            this.unitPrice = newUnitPrice;
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

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    private Integer calculateTotal(Integer net, Integer qty) {
        if (net == null || qty == null) {
            return totalWeight;
        }
        return net * qty;
    }

    @Override
    public String toString() {
        return "Ingredient{"
                + "id=" + id
                + ", ingredientName='" + ingredientName + '\''
                + ", netWeight=" + netWeight
                + ", quantity=" + quantity
                + ", totalWeight=" + totalWeight
                + ", unitPrice=" + unitPrice
                + '}';
    }
}
