package my_app.controller.component.goods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import my_app.bus.IngredientBus;
import my_app.bus.SupplierBus;
import my_app.controller.BaseController;
import my_app.model.Ingredient;
import my_app.model.Supplier;
import my_app.service.AlertInformation;
import my_app.service.ComboBoxService;
import my_app.service.LoadFileGUI;

public class GoodsReceiptController extends BaseController {

    private final static SupplierBus supplierBus = new SupplierBus();
    private final static IngredientBus ingredientBus = new IngredientBus();

    @FXML
    private ComboBox<Supplier> cbSupplier;

    @FXML
    private TableColumn<Ingredient, String> colName;

    @FXML
    private TableColumn<Ingredient, Double> colPrice;

    @FXML
    private TableColumn<Ingredient, Integer> colQuantity;

    @FXML
    private TableColumn<Ingredient, Double> colTotal;

    @FXML
    private TableColumn<Ingredient, Double> colWeight;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Label lbTotal;

    @FXML
    private TableView<Ingredient> tbReceipt;

    @FXML
    private TextField txtEmployee;

    @FXML
    private TextField txtNote;

    @FXML
    private VBox vbIngredientItems;

    private void setComboBoxData() {
        ComboBoxService<Supplier> comboBoxService = new ComboBoxService<>(cbSupplier);
        comboBoxService.setData(supplierBus.getSuppliers());
        cbSupplier.getSelectionModel().select(0);
    }

    private void RenderData() {
        ingredientBus.findAll();
        supplierBus.findAll();
    }

    @FXML
    private void EmptyForm() {

    }

    private void setInputData() {
        txtEmployee.setText("Nhân viên 1");
        dpDate.setValue(java.time.LocalDate.now());
    }

    @Override
    protected void initData() {
        RenderData();
        setTable();
        setComboBoxData();
        setInputData();

    }

    private void setDataIngredient() {
        tbReceipt.setItems(ingredientBus.getIngredients());
        colName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("netWeight"));
    }

    private void setTable() {
        setDataIngredient();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initUI() {

    }

    private void handleDeleteIngredient(Ingredient ing, Node node) {

        vbIngredientItems.getChildren().remove(node);
        tbReceipt.getSelectionModel().clearSelection();
    }

    private void handleIngredientControllForm(GoodsReceiveItems controller, Node node) {

        Ingredient ing = tbReceipt.getSelectionModel().getSelectedItem();
        if (ing != null) {
            if (!controller.getIngredients().contains(ing)) {
                vbIngredientItems.getChildren().add(node);
                controller.setData(ing);
                controller.setDeleteEvent((e) -> {
                    System.out.println("e: " + e);
                    handleDeleteIngredient(ing, node);
                    return null;
                });
            } else {
                AlertInformation.showErrorAlert("Lỗi", "Nguyên liệu đã tồn tại", "Nguyên liệu này đã tồn tại trong phiếu nhập.");
            }
        } else {
            AlertInformation.showErrorAlert("Lỗi", "Vui lòng chọn nguyên liệu", "Bạn cần chọn một nguyên liệu để chỉnh sửa.");

        }
        tbReceipt.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleIngredientAdd(ActionEvent event) {
        try {
            LoadFileGUI loadingitem = new LoadFileGUI("/fxml/admin/component/ingredient/goodsreceiptdetails.fxml");
            Node node = loadingitem.getNode();
            GoodsReceiveItems controller = (GoodsReceiveItems) loadingitem.getLoader().getController();
            handleIngredientControllForm(controller, node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EmptyIngredient() {
        vbIngredientItems.getChildren().clear();
        GoodsReceiveItems.getIngredients().clear();
    }

    @FXML
    private void handleCanceIngredient(ActionEvent event) {
        RenderData();
        setComboBoxData();
        dpDate.setValue(java.time.LocalDate.now());
        txtNote.clear();
        txtEmployee.setText("Nhân viên 1");
        EmptyIngredient();
    }

}
