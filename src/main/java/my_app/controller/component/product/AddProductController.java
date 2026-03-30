package my_app.controller.component.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddProductController {

    @FXML
    private ComboBox<String> cbbCategoryProduct;

    @FXML
    private ComboBox<String> cbbStatusProduct;

    @FXML
    private TextField tfNameProduct;

    @FXML
    private TextField tfPriceProduct;

    @FXML
    private TextField tfQuantityProduct;

    @FXML
    private TextField tfUnitProduct;

    @FXML
    private void initialize(){
        cbbStatusProduct.getItems().addAll("Available", "Unavailable");
        cbbStatusProduct.getSelectionModel().select(0);
    }

    private boolean CheckForm(){
        
        return true;
    }
    

    @FXML
    private void handleAddProduct(ActionEvent event) {

    }

    @FXML
    private void handleCancel(ActionEvent event) {

    }

}
