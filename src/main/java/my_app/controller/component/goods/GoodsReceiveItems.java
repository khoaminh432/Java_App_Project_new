package my_app.controller.component.goods;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import my_app.controller.BaseController;
import my_app.model.Ingredient;
import my_app.service.FormatBigDecimal;

public class GoodsReceiveItems extends BaseController {

    private Function deleteEvent;
    private static ArrayList<Ingredient> ingredients = new ArrayList<>();
    @FXML
    private Label lbIngredientName;

    @FXML
    private TextField tfNetWeightUpdate;

    @FXML
    private TextField tfQuantityUpdate;

    @FXML
    private TextField tfUnitPriceUpdate;

    private Ingredient ing;

    public static ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setData(Ingredient ing) {
        this.ing = ing;
        ingredients.add(ing);
        setLabelText();
    }

    public void setDeleteEvent(Function deleteEvent) {
        this.deleteEvent = deleteEvent;
    }

    @FXML
    private void handleDeleteIngredient(MouseEvent event) {
        if (deleteEvent != null) {
            ingredients.remove(ing);
            deleteEvent.apply(null);
            System.out.println(ingredients.size());
        }
    }

    private void setLabelText() {
        lbIngredientName.setText(ing.getIngredientName());
        tfUnitPriceUpdate.setText(FormatBigDecimal.getVNFormat(ing.getUnitPrice()));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initUI() {

    }

}
