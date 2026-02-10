package my_app.controller.component.product;

import java.math.RoundingMode;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import my_app.controller.component.ProductController;
import my_app.model.Ingredient;
import my_app.model.IngredientProduct;
import my_app.service.ConfigTextField;

public class handleIngredientController {

    @FXML
    private Label lbCost;

    @FXML
    private Label lbNameIngredient;

    @FXML
    private TextField tfEstimate;

    private IngredientProduct ingredient;
    private Function changeFunction;

    private void setlbText() {
        lbNameIngredient.setText(ingredient.getIngredient().getIngredientName());
        lbCost.setText(String.valueOf(ingredient.getTotalPrice() != null ? ingredient.getTotalPrice().setScale(2, RoundingMode.HALF_UP) : "chưa có"));
    }

    public void setData(IngredientProduct ingredient) {
        this.ingredient = ingredient;
        setlbText();
    }

    public void setChange(Function func) {
        this.changeFunction = func;
    }

    @FXML
    private void initialize() {

        setupEvent();
    }

    private void setupEvent() {
        ConfigTextField.AcceptOnlyNumber(tfEstimate);
        tfEstimate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                return;
            }
            Integer estimate = Integer.parseInt(newValue);
            Ingredient ingtemp = ingredient.getIngredient();
            ingredient.setEstimate(estimate);
            ingredient.setTotalPrice();
            setlbText();
        });
    }

    @FXML
    private void handleDeleteIngredient() {
        // Implement the logic to delete the ingredient
        System.out.println("Deleting ingredient: " + ingredient.getIngredient().getIngredientName());
        System.out.println(ProductController.ingredientTemp);
        changeFunction.apply(null);
    }

}
