package my_app.controller.component.goods;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import my_app.bus.GoodsReceiptBus;
import my_app.bus.GoodsReceiptDetailBus;
import my_app.bus.IngredientBus;
import my_app.bus.SupplierBus;
import my_app.controller.BaseController;
import my_app.model.GoodsReceipt;
import my_app.model.Ingredient;
import my_app.model.Supplier;
import my_app.service.AlertInformation;
import my_app.service.ComboBoxService;
import my_app.service.LoadFileGUI;

public class GoodsReceiptController extends BaseController {

    private SupplierBus supplierBus;
    private IngredientBus ingredientBus;
    private GoodsReceiptBus goodsReceiptBus;
    private GoodsReceiptDetailBus goodsReceiptDetailBus;
    
    private void initBusClasses() {
        if (supplierBus == null) supplierBus = new SupplierBus();
        if (ingredientBus == null) ingredientBus = new IngredientBus();
        if (goodsReceiptBus == null) goodsReceiptBus = new GoodsReceiptBus();
        if (goodsReceiptDetailBus == null) goodsReceiptDetailBus = new GoodsReceiptDetailBus();
    }
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

    @FXML
    private TextField tfIngredientName;

    private GoodsReceipt getGoodsReceiptbyForm() {
        GoodsReceipt receipt = new GoodsReceipt();
        receipt.setReceivedDate(LocalDateTime.of(dpDate.getValue(), java.time.LocalTime.now()));
        receipt.setSupplierId(cbSupplier.getSelectionModel().getSelectedItem().getId());
        receipt.setId(Integer.valueOf(goodsReceiptBus.getNextId()));
        return receipt;
    }

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
        initBusClasses();
        try {
            RenderData();
            setTable();
            setComboBoxData();
            setInputData();
        } catch (Exception e) {
            System.err.println("Warning: Database not available during initialization. Error: " + e.getMessage());
            e.printStackTrace();
        }
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
            if (!controller.getArrayIngredients().contains(ing)) {
                vbIngredientItems.getChildren().add(node);
                controller.setData(ing);
                controller.setDeleteEvent((e) -> {
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
    private void btnUpdateIngredient(ActionEvent event) {
        try {
            LoadFileGUI loadingitem = new LoadFileGUI("/fxml/admin/component/ingredient/goodsreceiptdetails.fxml");
            Node node = loadingitem.getNode();
            GoodsReceiveItems controller = (GoodsReceiveItems) loadingitem.getLoader().getController();
            handleIngredientControllForm(controller, node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAddIngredient() {
        Ingredient ing = new Ingredient();
        ing.setIngredientName(tfIngredientName.getText());
        ing.setUnitPrice(BigDecimal.ZERO);
        ing.setQuantity(0);
        ing.setNetWeight(0);
        ingredientBus.create(ing);
        tfIngredientName.clear();
    }

    @FXML
    private void btnAddIngredient(ActionEvent event) {
        if (tfIngredientName.getText().isEmpty()) {
            AlertInformation.showErrorAlert("Lỗi", "Vui lòng nhập tên nguyên liệu", "Bạn cần nhập tên nguyên liệu để thêm vào phiếu nhập.");
            return;
        }
        Boolean isConfirmed = AlertInformation.showConfirmationAlert("Xác nhận", "Thêm nguyên liệu", "Bạn có chắc muốn thêm nguyên liệu này vào phiếu nhập không?");
        if (isConfirmed) {
            handleAddIngredient();
        } else {
            AlertInformation.showInfoAlert("Hủy bỏ", "Thêm nguyên liệu đã bị hủy", "Nguyên liệu không được thêm vào phiếu nhập.");
        }
    }

    private void EmptyIngredient() {
        vbIngredientItems.getChildren().clear();
        GoodsReceiveItems.getArrayIngredients().clear();
    }

    @FXML
    private void handleCanceIngredient(ActionEvent event) {
        RenderData();
        setComboBoxData();
        dpDate.setValue(java.time.LocalDate.now());
        txtNote.clear();
        txtEmployee.setText("Nhân viên 1");
        tfIngredientName.clear();
        EmptyIngredient();
        System.out.println(GoodsReceiveItems.getArrayIngredients().size());
    }

    @FXML
    private void btnHandleSave(ActionEvent event) {

        if (GoodsReceiveItems.getArrayIngredients().isEmpty()) {
            AlertInformation.showErrorAlert("Lỗi", "Vui lòng thêm nguyên liệu", "Bạn cần thêm ít nhất một nguyên liệu vào phiếu nhập.");
            return;
        }
        GoodsReceipt receipt = getGoodsReceiptbyForm();
        goodsReceiptBus.create(receipt);
        GoodsReceiveItems.getArrayIngredients().forEach(ing -> {
            try {
                ing.validateData();
                goodsReceiptDetailBus.create(ing.getGoodsReceiptDetail(receipt.getId()));
                System.out.println("Đã lưu nguyên liệu: " + ing.getIngredient().getIngredientName() + ing.getIngredient().getUnitPrice() + ing.getIngredient().getQuantity() + ing.getIngredient().getNetWeight());
            } catch (IllegalArgumentException e) {
                AlertInformation.showErrorAlert("Lỗi", "Dữ liệu không hợp lệ", e.getMessage());
            }
        });
        handleCanceIngredient(null);
        AlertInformation.showInfoAlert("Thành công", "Lưu phiếu nhập thành công", "Phiếu nhập đã được lưu vào hệ thống.");
    }
}
