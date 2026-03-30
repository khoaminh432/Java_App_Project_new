package my_app.controller.component.product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import my_app.model.Product;

public class handleListController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnView;

    private Product product;

    public void setData(Product product) {
        this.product = product;
    }

    @FXML
    private void handleEdit() {
        System.out.println("Edit product: " + product.getProductName());
    }

    @FXML
    private void handleDelete() {
        System.out.println("Delete product: " + product.getProductName());
    }

    @FXML
    private void handleView() {
        System.out.println("View product: " + product.getProductName());
    }
}
