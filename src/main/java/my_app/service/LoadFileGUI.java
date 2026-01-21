package my_app.service;

import java.io.IOException;
import javafx.scene.layout.VBox;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class LoadFileGUI {
    private final FXMLLoader fxmlLoader;

    public LoadFileGUI(String location) {
        this.fxmlLoader = new FXMLLoader(getClass().getResource(location));
    
    }

    

    public FXMLLoader getLoader() {
        return this.fxmlLoader;
    }

    public Parent load() throws IOException {
        Parent parent = fxmlLoader.load();
        
        AnchorPane.setTopAnchor(parent, 0.0);
        AnchorPane.setBottomAnchor(parent, 0.0);
        AnchorPane.setLeftAnchor(parent, 0.0);
        AnchorPane.setRightAnchor(parent, 0.0);

        return parent;
    }
}
