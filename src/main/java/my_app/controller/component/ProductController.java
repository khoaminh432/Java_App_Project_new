package my_app.controller.component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import my_app.bus.IngredientBus;
import my_app.bus.IngredientProductBus;
import my_app.bus.ProductBus;
import my_app.bus.ProductCategoryBus;
import my_app.controller.component.product.handleIngredientController;
import my_app.controller.component.product.handleListController;
import my_app.model.Ingredient;
import my_app.model.IngredientProduct;
import my_app.model.Product;
import my_app.model.ProductCategory;
import my_app.service.AlertInformation;
import my_app.service.ConfigTextField;
import my_app.service.LoadFileGUI;
import my_app.util.DefaultValueObject;

public class ProductController {

    private final static IngredientProductBus ingredientProductBus = new IngredientProductBus();
    private final static ProductCategoryBus productCategoryBus = new ProductCategoryBus();
    private final static IngredientBus ingredientBus = new IngredientBus();
    private final static ProductBus productBus = new ProductBus();
    private static BigDecimal calTotalPriceAll = BigDecimal.ZERO;
    private static HashMap<String, Integer> statisticProduct = new HashMap<>();

    static {
        statisticProduct.put("total", 0);
        statisticProduct.put("remain", 0);
        statisticProduct.put("sell", 0);
        statisticProduct.put("warning", 0);
    }

    @FXML
    private Button btnMaxQuantity;

    // dữ liệu sản phẩm
    @FXML
    private TableColumn<Product, Integer> colID;

    @FXML
    private TableColumn<Product, String> colName;

    @FXML
    private TableColumn<Product, Double> colPrice;

    @FXML
    private TableColumn<Product, Integer> colQuantity;

    @FXML
    private TableColumn<Product, String> colSize;

    @FXML
    private TableColumn<Product, String> colStatus;

    @FXML
    private TableColumn<Product, Void> colActionHandle;

    @FXML
    private TableView<Product> tableProduct;
    // end dữ liệu sản phẩm

    // dữ liệu nguyên liệu
    @FXML
    private TableColumn<Ingredient, Integer> colIDIngredient;

    @FXML
    private TableColumn<Ingredient, String> colNameIngredient;

    @FXML
    private TableColumn<Ingredient, Integer> colTotalWeightIngredient;

    @FXML
    private TableColumn<Ingredient, Double> colUnitPriceIngredient;

    @FXML
    private TableView<Ingredient> tbIngredient;

    // end dữ liệu nguyên liệu
    @FXML
    private TextField tfSearchProduct;

    @FXML
    private TextField tfNameProduct;

    @FXML
    private TextField tfPriceProduct;

    @FXML
    private TextField tfQuantityProduct;

    @FXML
    private TextField tfUnitProduct;

    // thống kê sản phẩm
    @FXML
    private Label lbQuantityProduct;

    @FXML
    private Label lbQuantityProductRemain;

    @FXML
    private Label lbQuantityProductSell;

    @FXML
    private Label lbQuantityProductWarning;

    @FXML
    private Label lbTotalAllIngredientProruct;

    // end thống kê sản phẩm
    @FXML
    private ImageView imgFilterProduct;

    @FXML
    private VBox vbAddProduct;

    @FXML
    private VBox vbManagementProduct;

    // add product pane
    @FXML
    private ComboBox<String> cbbUnitProduct;

    @FXML
    private ComboBox<ProductCategory> cbbCategoryProduct;

    @FXML
    private ComboBox<String> cbbPriceProduct;

    // trạng thái sản phẩm
    @FXML
    private ToggleGroup grradioStatusProduct;

    @FXML
    private RadioButton rdoActiveProduct;

    @FXML
    private RadioButton rdoUnactiveProduct;

    @FXML
    private VBox vbIngredientTemp;
    private Label lbTotalPriceIngredient;

    public static ArrayList<IngredientProduct> ingredientTemp = new ArrayList<>();
    // end add product pane
    private Task<List<Product>> loadProductsTask;

    @FXML
    public void initialize() {
        LoadData();
        LoadEvent();
    }

    // tải dữ liệu
    private void LoadData() {
        productBus.findAll();
        ingredientBus.findAll();
        setTableData();
        LoadComboboxData();
        setTextFieldNumeric();
    }

    // tải sự kiện
    private void LoadEvent() {
        setMouseClickTableProductEvent();
        searchBarProducts();
        setMouseClickTableIngredientEvent();
    }

    private void setTableData() {
        configureColumnsProduct();
        tableProduct.setItems(productBus.getFilteredProducts());
        configureColumnsIngredient();
        tbIngredient.setItems(ingredientBus.getIngredients());
    }

    private void setTextFieldNumeric() {
        ConfigTextField.AcceptOnlyNumber(tfPriceProduct);
        ConfigTextField.AcceptOnlyNumber(tfQuantityProduct);
    }

    private void clearAddFormProduct() {
        tfNameProduct.clear();
        tfPriceProduct.clear();
        tfQuantityProduct.clear();
        tfUnitProduct.clear();
        cbbCategoryProduct.getSelectionModel().select(0);
        cbbPriceProduct.getSelectionModel().select(0);
        cbbUnitProduct.getSelectionModel().select(0);
        rdoActiveProduct.setSelected(true);
    }

    private boolean checkaddFormpProduct() {
        if (tfNameProduct.getText().isBlank()) {
            AlertInformation.showWarningAlert("Chú Ý", "Tên Sản Phẩm Trống", "Vui lòng nhập tên sản phẩm.");
            return false;
        }
        if (tfPriceProduct.getText().isBlank()) {
            AlertInformation.showWarningAlert("Chú Ý", "Giá Sản Phẩm Trống", "Vui lòng nhập giá sản phẩm.");
            return false;
        }
        if (tfQuantityProduct.getText().isBlank()) {
            AlertInformation.showWarningAlert("Chú Ý", "Số Lượng Sản Phẩm Trống", "Vui lòng nhập số lượng sản phẩm.");
            return false;
        }
        if (tfUnitProduct.getText().isBlank()) {
            AlertInformation.showWarningAlert("Chú Ý", "Đơn Vị Sản Phẩm Trống", "Vui lòng nhập đơn vị sản phẩm.");
            return false;
        }
        return true;
    }

    @FXML
    private void addFormProduct() {
        if (!checkaddFormpProduct()) {
            return;
        }
    }

    private void loadlabelTotalProductAllIngredientProduct() {
        calTotalPriceAll = ingredientProductBus.calTotalPriceAll();
        lbTotalAllIngredientProruct.setText(calTotalPriceAll != null ? calTotalPriceAll.toString() : "0");
    }

    private void loadProductsAsync() {
        if (loadProductsTask != null && loadProductsTask.isRunning()) {
            return;
        }

        loadProductsTask = new Task<>() {
            @Override
            protected List<Product> call() {
                return productBus.fetchAllFromDb();
            }
        };

        loadProductsTask.setOnSucceeded(event -> {
            List<Product> data = loadProductsTask.getValue();
            productBus.replaceAll(data);
            updateStatisticProduct();
            setLabelStatisticProduct();
        });

        loadProductsTask.setOnFailed(event -> loadProductsTask.getException().printStackTrace());

        Thread worker = new Thread(loadProductsTask, "product-loader");
        worker.setDaemon(true);
        worker.start();
    }

    private void searchBarProducts() {
        tfSearchProduct.textProperty().addListener((obs, oldval, newval) -> {
            System.out.println("Search Product: " + newval);
            if (newval == null || newval.isBlank() || newval == "") {
                productBus.findAll();

            } else {
                try {
                    searchIDProducts(newval);
                } catch (NumberFormatException e) {
                    searchNameProducts(newval);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            updateStatisticProduct();
        });
    }

    @FXML
    private void handleaddIngredient() {
        try {
            Ingredient ingredient = tbIngredient.getSelectionModel().getSelectedItem();

            if (ingredient == null) {
                AlertInformation.showWarningAlert("Chú Ý", "Chưa Chọn Nguyên Liệu", "Vui lòng chọn nguyên liệu để thêm.");
                return;
            }
            IngredientProduct ingprotemp = new IngredientProduct();
            ingprotemp.setIngredientId(ingredient.getId());
            ingprotemp.setIngredient(ingredient);
            if (!ingredientProductBus.addtoList(ingprotemp)) {
                return;
            }
            LoadIngredientTemp(ingprotemp);

            tbIngredient.getSelectionModel().clearSelection();
            IngredientProduct ingredientProduct = ingredientBus.getIngredientProductByThis(ingredient);
            System.out.println(ingredientProduct + " ingredient: " + ingredientProduct.getIngredient());
            LoadIngredientTemp(ingredientProduct);
            AlertInformation.showInfoAlert("Thành Công", "Đã Thêm Nguyên Liệu", "Nguyên liệu đã được thêm thành công.");
            tbIngredient.getSelectionModel().clearSelection();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchIDProducts(String keyword) {
        int id = Integer.parseInt(keyword);
        productBus.findById(id);
    }

    private void searchNameProducts(String keyword) {
        // productBus.searchNameByDB(keyword);
        productBus.searchNameByArray(keyword);
    }

    // dữ liệu của table
    private void configureColumnsProduct() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPrice.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(toDouble(cell.getValue().getUnitPrice())));
        LoadActionButtons();
    }

    private void configureColumnsIngredient() {
        colIDIngredient.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameIngredient.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        colTotalWeightIngredient.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        colUnitPriceIngredient.setCellValueFactory(cell
                -> new ReadOnlyObjectWrapper<>(toDouble(cell.getValue().getUnitPrice())));
        colQuantityIngredient.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colWeightIngredient.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(toDouble(cell.getValue().getTotalWeight())));
    }

    // xử lí khi ấn vào table
    private void setMouseClickTableProductEvent() {
        tableProduct.setOnMouseClicked(e -> {

            Product selectedProduct = tableProduct.getSelectionModel().getSelectedItem();
            System.out.println(selectedProduct);
        });
    }

    //
    private void setMouseClickTableIngredientEvent() {
        tbIngredient.setOnMouseClicked(e -> {

            Ingredient selectedIngredient = tbIngredient.getSelectionModel().getSelectedItem();
            System.out.println(selectedIngredient);
        });
    }

    private void setShowNameCombobox() {
        cbbCategoryProduct.setCellFactory(clbck -> new ListCell<ProductCategory>() {
            @Override
            protected void updateItem(ProductCategory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCategoryName());
                }
            }
        });
        cbbCategoryProduct.setButtonCell(new ListCell<ProductCategory>() {
            @Override
            protected void updateItem(ProductCategory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCategoryName());
                }
            }
        });
    }

    private void LoadComboboxData() {
        productCategoryBus.findAll();
        cbbUnitProduct.getItems().addAll(FXCollections.observableArrayList(DefaultValueObject.getUnitProduct()));
        cbbUnitProduct.getSelectionModel().select(0);
        cbbCategoryProduct.getItems().addAll(productCategoryBus.getProductCategories());
        cbbCategoryProduct.getSelectionModel().select(0);
        cbbPriceProduct.getItems().addAll(FXCollections.observableArrayList(DefaultValueObject.getDefaultUnitPriceProduct()));
        cbbPriceProduct.getSelectionModel().select(0);
        cbbStatusProduct.getItems().addAll(FXCollections.observableArrayList(DefaultValueObject.getStatusProduct()));
        cbbStatusProduct.getSelectionModel().select(0);
        setShowNameCombobox();
    }

    private void LoadActionButtons() {
        colActionHandle.setCellFactory(param -> new TableCell<Product, Void>() {
            private LoadFileGUI loadGUI;
            private Node node;
            private handleListController controller;

            {
                try {
                    loadGUI = new LoadFileGUI("/fxml/admin/component/product/handleproduct.fxml");
                    node = loadGUI.getNode();
                    controller = loadGUI.getLoader().getController();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Product p = getTableView().getItems().get(getIndex());
                    controller.setData(p);
                    setGraphic(node);
                }
            }
        });
    }

    private void setTotalPriceAllIngredientTemp() {
        double totalPrice = (int) ingredientTemp.stream().mapToDouble(ip -> toDouble(ip.getTotalPrice())).sum();
        lbTotalPriceIngredient.setText(String.valueOf(totalPrice));
    }

    private void LoadIngredientTemp(IngredientProduct ingredient) {
        try {
            LoadFileGUI loadGUI = new LoadFileGUI("/fxml/admin/component/product/ingredient.fxml");
            Node node = loadGUI.getNode();
            handleIngredientController controller = loadGUI.getLoader().getController();
            controller.setData(ingredient);
            controller.setChange((obj) -> {
                System.out.println("this is: " + obj);

                loadlabelTotalProductAllIngredientProduct();
                return null;
            });
            controller.setRemove((obj) -> {
                System.out.println("this is: " + obj);
            controller.setDeleteChange((obj) -> {
                removeIngredientTemp(ingredient, node);
                loadlabelTotalProductAllIngredientProduct();
                return null;
            });
            addIngredientTemp(node);
            controller.setChange((obj) -> {
                setTotalPriceAllIngredientTemp();
                updateMaxQuantityProduct();
                return null;
            });
            addIngredientTemp(ingredient, node);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addIngredientTemp(Node node) {
        vbIngredientTemp.getChildren().add(node);
    }

    private void removeIngredientTemp(IngredientProduct ingredient, Node node) {
        ingredientProductBus.removeFromList(ingredient);
        vbIngredientTemp.getChildren().remove(node);
    }

    private void setLabelStatisticProduct() {
        lbQuantityProduct.setText(String.valueOf(statisticProduct.get("total")));
        lbQuantityProductRemain.setText(String.valueOf(statisticProduct.get("remain")));
        lbQuantityProductSell.setText(String.valueOf(statisticProduct.get("sell")));
        lbQuantityProductWarning.setText(String.valueOf(statisticProduct.get("warning")));
    }

    private void calculateStatisticProduct() {
        int total = 0;
        int remain = 0;
        int sell = 0;
        int warning = 0;
        total = productBus.getProducts().size();
        for (Product p : productBus.getProducts()) {
            if (p.getQuantity() > 10) {
                remain += 1;
            }
            if (p.getStatus().toLowerCase().equals(("available"))) {
                sell += 1;
            }
            if (p.getQuantity() <= 5) {
                warning += 1;
            }
        }
        statisticProduct.put("total", total);
        statisticProduct.put("remain", remain);
        statisticProduct.put("sell", sell);
        statisticProduct.put("warning", warning);
    }

    private void updateStatisticProduct() {
        calculateStatisticProduct();
        setLabelStatisticProduct();
    }

    // convert dữ liệu
    private double toDouble(BigDecimal price) {
        return price == null ? 0d : price.doubleValue();
    }

    private Double toDouble(Integer weight) {
        return weight == null ? 0d : weight.doubleValue();
    }

    private void updateMaxQuantityProduct() {

        btnMaxQuantity.setText(productBus.getMaxQuantity(ingredientTemp) + "");
    }
    // sự kiện lọc sản phẩm

    @FXML
    private void filteredProducts(ActionEvent event) {
        System.out.println("Filter Products");
    }

    @FXML
    private void btnCancelAddProduct(ActionEvent event) {
        System.out.println("Cancel Add Product");
        productBus.findAll();
        FadeTransition ft = new FadeTransition(Duration.millis(300), this.vbManagementProduct);
        ft.setFromValue(0);
        ft.setToValue(1);

        vbManagementProduct.setVisible(true);
        vbAddProduct.setVisible(false);
        ft.play();

    }

    @FXML
    private void handleActionaddProduct(ActionEvent event) {
        System.out.println("Add Product");
        ingredientBus.findAll();

        FadeTransition ft = new FadeTransition(Duration.millis(300), this.vbAddProduct);
        ft.setFromValue(0);
        ft.setToValue(1);

        vbAddProduct.setVisible(true);
        vbManagementProduct.setVisible(false);
        ft.play();
    }

    private Product getProductFromInput() {
        String name = ((TextField) vbAddProduct.lookup("#tfNameProduct")).getText();
        String priceStr = ((TextField) vbAddProduct.lookup("#tfPriceProduct")).getText();
        String quantityStr = ((TextField) vbAddProduct.lookup("#tfQuantityProduct")).getText();
        String unit = cbbUnitProduct.getSelectionModel().getSelectedItem();
        ProductCategory categoryTemp = cbbCategoryProduct.getSelectionModel().getSelectedItem();
        String status = cbbStatusProduct.getSelectionModel().getSelectedItem();

        if (name.isBlank() || priceStr.isBlank() || quantityStr.isBlank()) {
            AlertInformation.showWarningAlert("Chú Ý", "Thiếu Thông Tin", "Vui lòng điền đầy đủ thông tin sản phẩm.");
            return null;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            AlertInformation.showWarningAlert("Chú Ý", "Thông Tin Không Hợp Lệ", "Vui lòng nhập giá và số lượng hợp lệ.");
            return null;
        }

        int categoryId = categoryTemp.getId();
        if (categoryId == -1) {
            AlertInformation.showWarningAlert("Chú Ý", "Danh Mục Không Hợp Lệ", "Vui lòng chọn danh mục hợp lệ.");
            return null;
        }

        Product product = new Product();
        product.setProductName(name);
        product.setUnitPrice(BigDecimal.valueOf(price));
        product.setQuantity(quantity);
        product.setUnit(unit);
        product.setStatus(status);
        product.setCategoryId(categoryId);

        return product;
    }

    @FXML
    private void btnAddProduct(ActionEvent event) {
        Product product = getProductFromInput();
        System.out.println("Product to add: " + product);
        try {
            ingredientProductBus.addListIngredientProducts(ingredientTemp);
            ingredientProductBus.Validate();
        } catch (Exception e) {
            AlertInformation.showErrorAlert("Lỗi", "Lỗi nguyên liệu", e.getMessage());
        }
    }
}
