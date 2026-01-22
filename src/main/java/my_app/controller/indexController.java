package my_app.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import my_app.service.LoadFileGUI;
import my_app.service.SwitchPage;
public class indexController extends SwitchPage {

    @FXML
    private VBox mainVBox;

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

    private LoadFileGUI HomePage ;
    private LoadFileGUI ProductPage ;

    private void switchPage(Parent page){
        
        SwitchPage(apLoadPane, page);
    }
    private LoadFileGUI createPage(String location){
        return  new LoadFileGUI(location);
    }
    private void loadPage(Parent page) throws IOException{
        SwitchPage(apLoadPane, page);
    }

    @FXML
    private void loadHomePage() throws IOException{
        HomePage = createPage("/fxml/admin/component/homepage.fxml");
         loadPage(HomePage.load());
    }

    @FXML
    private void loadProductPage() throws IOException{
        ProductPage = createPage("/fxml/admin/component/productpage.fxml");
        loadPage(ProductPage.load());
    }

    @FXML
    public void initialize() {
        try {
            loadHomePage();
            loadProductPage();
            loadHomePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Initialization code here
        
    }

}