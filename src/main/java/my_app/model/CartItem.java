package my_app.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CartItem {
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final DoubleProperty totalPrice;

    public CartItem(int productId, String productName, int quantity, double price) {
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.totalPrice = new SimpleDoubleProperty(quantity * price);
    }

    // Getters and Setters
    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int value) {
        productId.set(value);
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String value) {
        productName.set(value);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int value) {
        quantity.set(value);
        updateTotalPrice();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        price.set(value);
        updateTotalPrice();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(double value) {
        totalPrice.set(value);
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    private void updateTotalPrice() {
        totalPrice.set(quantity.get() * price.get());
    }
}
