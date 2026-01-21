package my_app.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import my_app.service.LoadFileGUI;
import java.io.IOException;
public class indexController {

    @FXML
    private VBox mainVBox;

    @FXML
    private AnchorPane apLoadPane;

    @FXML
    public void initialize() {
        // Initialization code here
        LoadFileGUI loadFileGUI = new LoadFileGUI("/fxml/homepage.fxml");
        try {
            apLoadPane.getChildren().add(loadFileGUI.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}