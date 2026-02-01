package my_app.controller.component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import my_app.bus.ProductBus;
import my_app.controller.component.product.handleListController;
import my_app.model.Product;
import my_app.service.LoadFileGUI;

public class ProductController {

    private final ProductBus productBus = new ProductBus();
    private static int QuantityProduct = 0;
    private static HashMap<String, Integer> statisticProduct = new HashMap<>();

    static {
        statisticProduct.put("total", 0);
        statisticProduct.put("remain", 0);
        statisticProduct.put("sell", 0);
        statisticProduct.put("warning", 0);
    }
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

    @FXML
    private TextField tfSearchProduct;

    @FXML
    private Label lbQuantiryProduct;

    @FXML
    private Label lbQuantiryProductRemain;

    @FXML
    private Label lbQuantiryProductSell;

    @FXML
    private Label lbQuantiryProductWarning;

    @FXML
    private ImageView imgFilterProduct;

    @FXML
    private VBox vbAddProduct;

    @FXML
    private VBox vbManagementProduct;

    private Task<List<Product>> loadProductsTask;

    @FXML
    public void initialize() {

        configureColumns();
        tableProduct.setItems(productBus.getProducts());
        setMouseClickEvent();
        searchBarProducts();
        loadProductsAsync();

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

    private void searchIDProducts(String keyword) {
        int id = Integer.parseInt(keyword);
        productBus.findById(id);
    }

    private void searchNameProducts(String keyword) {
        // productBus.searchNameByDB(keyword);
        productBus.searchNameByArray(keyword);
    }

    // dữ liệu của table
    private void configureColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPrice.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(toDouble(cell.getValue().getUnitPrice())));
        LoadActionButtons();
    }

    // xử lí khi ấn vào table
    private void setMouseClickEvent() {
        tableProduct.setOnMouseClicked(e -> {
            Product selectedProduct = tableProduct.getSelectionModel().getSelectedItem();
            System.out.println(selectedProduct);
        });
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

    private void setLabelStatisticProduct() {
        lbQuantiryProduct.setText(String.valueOf(statisticProduct.get("total")));
        lbQuantiryProductRemain.setText(String.valueOf(statisticProduct.get("remain")));
        lbQuantiryProductSell.setText(String.valueOf(statisticProduct.get("sell")));
        lbQuantiryProductWarning.setText(String.valueOf(statisticProduct.get("warning")));
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
            if (p.getStatus().equals(("available").toUpperCase())) {
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

    private double toDouble(BigDecimal price) {
        return price == null ? 0d : price.doubleValue();
    }

    @FXML
    private void filteredProducts() throws Exception {
        System.out.println("Filter Products");
    }

    @FXML
    private void btnCancelAddProduct() throws Exception {
        System.out.println("Cancel Add Product");
        FadeTransition ft = new FadeTransition(Duration.millis(300), this.vbManagementProduct);
        ft.setFromValue(0);
        ft.setToValue(1);

        vbManagementProduct.setVisible(true);
        vbAddProduct.setVisible(false);
        ft.play();

    }

    @FXML
    private void handleActionaddProduct() throws Exception {
        System.out.println("Add Product"
        );
        FadeTransition ft = new FadeTransition(Duration.millis(300), this.vbAddProduct);
        ft.setFromValue(0);
        ft.setToValue(1);

        vbAddProduct.setVisible(true);
        vbManagementProduct.setVisible(false);
        ft.play();
    }

}
