package my_app.controller.component.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Function removeFunction;
    private Function changeFunction;

    private void setlbText() {
        lbNameIngredient.setText(ingredient.getIngredient().getIngredientName());
        lbCost.setText(String.valueOf(ingredient.getTotalPrice() != null ? ingredient.getTotalPrice().setScale(0, RoundingMode.HALF_UP) : "chưa có"));
    }

    public void setData(IngredientProduct ingredient) {
        this.ingredient = ingredient;
        setlbText();
    }

    public void setRemove(Function func) {
        this.removeFunction = func;
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
                lbCost.setText("chưa có");
                ingredient.setTotalPrice(BigDecimal.ZERO);
                return;
            }
            Integer estimate = Integer.parseInt(newValue);
            ingredient.setEstimate(estimate);
            ingredient.setTotalPrice();
            changeFunction.apply(null);
            setlbText();
        });
    }

    @FXML
    private void handleDeleteIngredient() {
        // Implement the logic to delete the ingredient
        System.out.println("Deleting ingredient: " + ingredient.getIngredient().getIngredientName());
        removeFunction.apply(null);

    }

}
