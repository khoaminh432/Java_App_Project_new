package my_app.controller.component;

import java.math.BigDecimal;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import my_app.controller.component.product.handleListController;
import my_app.dao.ProductDao;
import my_app.model.Product;
import my_app.service.LoadFileGUI;
public class ProductController {
    private final ProductDao productDao = new ProductDao();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private static int QuantityProduct = 0;
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
    private TableColumn<Product, Void> colActionHandle;

    @FXML
    private TableView<Product> tableProduct;

    @FXML
    private TextField tfSearchProduct;

    @FXML
    private Label lbQuantiryProduct;

    @FXML
    private ImageView imgFilterProduct;

    @FXML
    public void initialize() {
        
        tableProduct.setItems(products);
        loadProducts();
        configureColumns();
        setMouseClickEvent();
        searchBarProducts();
    }
    
    private void searchBarProducts(){
        tfSearchProduct.textProperty().addListener((obs,oldval,newval)->{
            if(newval=="")
                loadProducts();
            
            else
             try{
                searchIDProducts(newval);
                }catch(NumberFormatException e){
                    searchNameProducts(newval);
                }


        });
    }
    
    private void searchIDProducts(String keyword){
            int id = Integer.parseInt(keyword);
            Product product = productDao.findById(id);
            if(product != null){
                settableProduct(List.of(product));
            } else {
                settableProduct(List.of());
            }
    }

    private void searchNameProducts(String keyword) {
        List<Product> filteredProducts = productDao.findByName(keyword);
        settableProduct(filteredProducts);
    }

    private void configureColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colPrice.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(toDouble(cell.getValue().getUnitPrice())));
        LoadActionButtons();
    }
    // xử lí khi ấn vào table
    private void setMouseClickEvent(){
        tableProduct.setOnMouseClicked(e->{
            Product selectedProduct = tableProduct.getSelectionModel().getSelectedItem();
            System.out.println(selectedProduct);
        });
    }
    
    private void LoadActionButtons(){
        colActionHandle.setCellFactory(param -> new TableCell<Product, Void>(){
            private LoadFileGUI loadGUI;
            private Node node;
            private handleListController controller;
            {
                try{
                    loadGUI = new LoadFileGUI("/fxml/admin/component/product/handleproduct.fxml");
                    node = loadGUI.getNode();
                    controller = loadGUI.getLoader().getController();
                }
                    catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item,empty);
                if(empty)
                    setGraphic(null);
                else{
                    Product p = getTableView().getItems().get(getIndex());
                    controller.setData(p);
                    setGraphic(node);
                }
            }
        });
    }
    private void settableProduct(List<Product> allProducts){
        QuantityProduct = allProducts.size();
        products.setAll(allProducts);
        updateStatisticProduct();
    }
    private void loadProducts() {
        List<Product> allProducts = productDao.findAll();
        
        settableProduct(allProducts);
        
    }
    private void updateStatisticProduct(){
        lbQuantiryProduct.setText(String.valueOf(QuantityProduct));
    }
    private double toDouble(BigDecimal price) {
        return price == null ? 0d : price.doubleValue();
    }
    @FXML
    private void filteredProducts() throws Exception {
        System.out.println("Filter Products");
    }
}
