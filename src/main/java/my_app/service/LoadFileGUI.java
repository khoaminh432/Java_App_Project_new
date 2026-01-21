package my_app.service;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoadFileGUI {
    private final FXMLLoader fxmlLoader;

    public LoadFileGUI(String location) {
        this.fxmlLoader = new FXMLLoader(getClass().getResource(location));
    
    }

    

    public FXMLLoader getLoader() {
        return this.fxmlLoader;
    }

    public Parent load() throws IOException {
        return this.fxmlLoader.load();
    }
}
