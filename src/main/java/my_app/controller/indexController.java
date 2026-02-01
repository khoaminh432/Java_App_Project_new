package my_app.controller;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import my_app.service.LoadFileGUI;
import my_app.service.SwitchPage;

public class indexController extends SwitchPage {

    private final LocalDate dateNow = LocalDate.now();
    private static String ClickPage = "Trang chủ";
    @FXML
    private VBox mainVBox;

    @FXML
    private Label lbTimeNow;

    @FXML
    private AnchorPane apLoadPane;

    @FXML
    private Button btnBillPage;

    @FXML
    private Button btnCustomerPage;

    @FXML
    private Button btnEmployeePage;

    @FXML
    private Button btnHomePage;

    @FXML
    private Button btnProductPage;

    @FXML
    private Button btnSupplierPage;

    private void switchPage(Parent page) {

        SwitchPage(apLoadPane, page);
    }

    private LoadFileGUI createPage(String location) {
        return new LoadFileGUI(location);
    }

    private void loadPage(Parent page) throws IOException {
        SwitchPage(apLoadPane, page);
    }

    // private void initpage() {
    //     HomePage = createPage("/fxml/admin/component/homepage.fxml");
    //     ProductPage = createPage("/fxml/admin/component/productpage.fxml");
    //     CustomerPage = createPage("/fxml/admin/component/product/addproduct.fxml");
    // }
    private void handleSwitchPage(String pageName, String url) throws IOException {
        if (!pageName.equals(ClickPage)) {
            LoadFileGUI page = createPage(url);
            loadPage(page.load());
        }
        this.ClickPage = pageName;
    }

    @FXML
    private void loadHomePage() throws IOException {
        System.out.println("Load Home Page");
        handleSwitchPage("Trang chủ", "/fxml/admin/component/homepage.fxml");
    }

    @FXML
    private void loadProductPage() throws IOException {

        System.out.println("Load Product Page");
        handleSwitchPage("Sản phẩm", "/fxml/admin/component/productpage.fxml");
    }

    @FXML
    private void loadCustomerPage() throws IOException {

        System.out.println("Load Customer Page");
        handleSwitchPage("Khách hàng", "/fxml/admin/component/product/addproduct.fxml");
    }

    private void loadLabel() {
        lbTimeNow.setText(dateNow.toString());
    }

    @FXML
    public void initialize() {
        try {
            ClickPage = "null";
            loadHomePage();
            loadLabel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Initialization code here

    }

}
