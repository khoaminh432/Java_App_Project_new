package my_app.controller.component.product;

import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import my_app.controller.component.ProductController;
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

    public void setData(IngredientProduct ingredient) {
        this.ingredient = ingredient;
        lbNameIngredient.setText(ingredient.getIngredient().getIngredientName());
        lbCost.setText(String.valueOf(ingredient.getUnitPrice()));
    }

    public void setChange(Function func) {
        this.changeFunction = func;
    }

    @FXML
    private void initialize() {
        ConfigTextField.AcceptOnlyNumber(tfEstimate);
    }

    @FXML
    private void handleDeleteIngredient() {
        // Implement the logic to delete the ingredient
        System.out.println("Deleting ingredient: " + ingredient.getIngredient().getIngredientName());
        System.out.println(ProductController.ingredientTemp);
        changeFunction.apply(null);

    }

}
