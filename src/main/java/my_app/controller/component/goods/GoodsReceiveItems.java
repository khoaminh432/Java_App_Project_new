package my_app.controller.component.goods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import my_app.controller.BaseController;
import my_app.model.GoodsReceiptDetail;
import my_app.model.Ingredient;
import my_app.service.ConfigTextField;
import my_app.service.FormatBigDecimal;

public class GoodsReceiveItems extends BaseController {

    private Function deleteEvent;
    private static ArrayList<GoodsReceiveItems> Arrayingredients = new ArrayList<>();
    @FXML
    private Label lbIngredientName;

    @FXML
    private TextField tfNetWeightUpdate;

    @FXML
    private TextField tfQuantityUpdate;

    @FXML
    private TextField tfUnitPriceUpdate;

    private Ingredient ing;

    private int getIndex() {
        return IntStream.range(0, Arrayingredients.size())
                .filter(i -> Arrayingredients.get(i).ing.getId() == ing.getId())
                .findFirst()
                .orElse(-1);
    }

    public static ArrayList<GoodsReceiveItems> getArrayIngredients() {
        return Arrayingredients;
    }

    public void setData(Ingredient ing) {
        this.ing = ing;
        Arrayingredients.add(this);
        setLabelText();
    }

    public void setDeleteEvent(Function deleteEvent) {
        this.deleteEvent = deleteEvent;
    }

    protected void validateData() throws IllegalArgumentException {
        try {
            ing.setUnitPrice(new BigDecimal(Double.parseDouble(tfUnitPriceUpdate.getText())));
            ing.setQuantity(Integer.parseInt(tfQuantityUpdate.getText()));
            ing.setNetWeight(Integer.parseInt(tfNetWeightUpdate.getText()));
            Arrayingredients.set(getIndex(), this);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ing.getIngredientName() + ": Các trường dữ liệu không hợp lệ! ");
            // Handle the exception, e.g., show an error message
        }

    }

    protected Ingredient getIngredient() {
        return ing;
    }

    protected GoodsReceiptDetail getGoodsReceiptDetail(int receiptId) {
        GoodsReceiptDetail detail = new GoodsReceiptDetail();
        detail.setReceiptId(receiptId);
        detail.setIngredient(ing);
        detail.setIngredientId(ing.getId());
        detail.setNetWeight(ing.getNetWeight());
        detail.setQuantity(ing.getQuantity());
        detail.setUnitPrice(ing.getUnitPrice());
        return detail;
    }

    @FXML
    private void handleDeleteIngredient(MouseEvent event) {
        if (deleteEvent != null) {
            Arrayingredients.remove(getIndex());
            deleteEvent.apply(null);
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
        ConfigTextField.AcceptOnlyNumber(tfNetWeightUpdate);
        ConfigTextField.AcceptOnlyNumber(tfQuantityUpdate);
        ConfigTextField.AcceptOnlyNumber(tfUnitPriceUpdate);
    }

    @Override
    protected void initUI() {

    }

}
