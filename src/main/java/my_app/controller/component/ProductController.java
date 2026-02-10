package my_app.controller.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import my_app.service.AlertInformation;
import my_app.service.LoadFileGUI;
import my_app.util.DefaultValueObject;

public class ProductController {

    private final static IngredientProductBus ingredientProductBus = new IngredientProductBus();
    private final static ProductCategoryBus productCategoryBus = new ProductCategoryBus();
    private final static IngredientBus ingredientBus = new IngredientBus();
    private final static ProductBus productBus = new ProductBus();
    private static int QuantityProduct = 0;
    private static HashMap<String, Integer> statisticProduct = new HashMap<>();

    static {
        statisticProduct.put("total", 0);
        statisticProduct.put("remain", 0);
        statisticProduct.put("sell", 0);
        statisticProduct.put("warning", 0);
    }

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
    private TableColumn<Ingredient, Integer> colQuantityIngredient;

    @FXML
    private TableColumn<Ingredient, Double> colWeightIngredient;

    @FXML
    private TableView<Ingredient> tbIngredient;

    // end dữ liệu nguyên liệu
    @FXML
    private TextField tfSearchProduct;

    // thống kê sản phẩm
    @FXML
    private Label lbQuantityProduct;

    @FXML
    private Label lbQuantityProductRemain;

    @FXML
    private Label lbQuantityProductSell;

    @FXML
    private Label lbQuantityProductWarning;

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
    private ComboBox<String> cbbCategoryProduct;

    @FXML
    private ComboBox<String> cbbPriceProduct;

    @FXML
    private ComboBox<String> cbbStatusProduct;

    @FXML
    private VBox vbIngredientTemp;

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
    }

    // tải sự kiện
    private void LoadEvent() {
        setMouseClickTableProductEvent();
        searchBarProducts();
        loadProductsAsync();
        setMouseClickTableIngredientEvent();

    }

    private void setTableData() {
        configureColumnsProduct();
        tableProduct.setItems(productBus.getProducts());
        configureColumnsIngredient();
        tbIngredient.setItems(ingredientBus.getIngredients());
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
            if (newval == null || newval.isBlank()) {
                loadProductsAsync();
            } else
             try {
                searchIDProducts(newval);
            } catch (NumberFormatException e) {
                searchNameProducts(newval);
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

            if (!ingredientTemp.isEmpty() && ingredientTemp.stream().anyMatch(a -> a.getIngredient().getId().equals(ingredient.getId()))) {
                AlertInformation.showWarningAlert("Chú Ý", "Nguyên Liệu Đã Tồn Tại", "Nguyên liệu đã được thêm trước đó.");
                return;
            }
            LoadIngredientTemp(ingredientBus.getIngredientProductByThis(ingredient));
            AlertInformation.showInfoAlert("Thành Công", "Đã Thêm Nguyên Liệu", "Nguyên liệu đã được thêm thành công.");
            tbIngredient.getSelectionModel().clearSelection();
            for (IngredientProduct ingredientProduct : ingredientTemp) {
                System.out.println(ingredientProduct + " = " + ingredientProduct.getIngredient());
            }
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
        colQuantityIngredient.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colWeightIngredient.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(toDouble(cell.getValue().getNetWeight())));
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

    private void LoadComboboxData() {
        productCategoryBus.findAll();

        cbbUnitProduct.getItems().addAll(FXCollections.observableArrayList(DefaultValueObject.getUnitProduct()));
        cbbUnitProduct.getSelectionModel().select(0);
        cbbCategoryProduct.getItems().addAll(productCategoryBus.getProductCategories().stream().map(s -> s.getCategoryName()).toList());
        cbbCategoryProduct.getSelectionModel().select(0);
        cbbPriceProduct.getItems().addAll("Low to High", "High to Low");
        cbbPriceProduct.getSelectionModel().select(0);
        cbbStatusProduct.getItems().addAll(FXCollections.observableArrayList(DefaultValueObject.getStatusProduct()));
        cbbStatusProduct.getSelectionModel().select(0);
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

    private void LoadIngredientTemp(IngredientProduct ingredient) {
        try {
            LoadFileGUI loadGUI = new LoadFileGUI("/fxml/admin/component/product/ingredient.fxml");
            Node node = loadGUI.getNode();
            handleIngredientController controller = loadGUI.getLoader().getController();
            controller.setData(ingredient);
            controller.setChange((obj) -> {
                removeIngredientTemp(ingredient, node);
                return null;
            });
            addIngredientTemp(ingredient, node);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addIngredientTemp(IngredientProduct ingredient, Node node) {
        ingredientTemp.add(ingredient);
        vbIngredientTemp.getChildren().add(node);
    }

    private void removeIngredientTemp(IngredientProduct ingredient, Node node) {
        ingredientTemp.remove(ingredient);
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
    // sự kiện lọc sản phẩm

    @FXML
    private void filteredProducts() {
        System.out.println("Filter Products");
    }

    @FXML
    private void btnCancelAddProduct() {
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
    private void handleActionaddProduct() {
        System.out.println("Add Product");
        ingredientBus.findAll();

        FadeTransition ft = new FadeTransition(Duration.millis(300), this.vbAddProduct);
        ft.setFromValue(0);
        ft.setToValue(1);

        vbAddProduct.setVisible(true);
        vbManagementProduct.setVisible(false);
        ft.play();
    }

}
