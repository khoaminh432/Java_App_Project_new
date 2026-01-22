package my_app.controller.component;

import java.math.BigDecimal;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import my_app.dao.ProductDao;
import my_app.model.Product;

public class ProductController {
    private final ProductDao productDao = new ProductDao();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

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
    private TableView<Product> tableProduct;

    @FXML
    public void initialize() {
        configureColumns();
        tableProduct.setItems(products);
        loadProducts();
        System.out.println("Loaded products: " + products.size());
    }
    
    private void configureColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colPrice.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(toDouble(cell.getValue().getUnitPrice())));
    }

    private void loadProducts() {
        List<Product> allProducts = productDao.findAll();
        products.setAll(allProducts);
    }

    private double toDouble(BigDecimal price) {
        return price == null ? 0d : price.doubleValue();
    }
}
