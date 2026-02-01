package my_app.service;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;

public class LoadFileGUI extends FXMLLoader {

    private final FXMLLoader fxmlLoader;

    // Constructor to initialize FXMLLoader with the given FXML file location
    public LoadFileGUI(String location) {
        this.fxmlLoader = new FXMLLoader(getClass().getResource(location));

    }

    public FXMLLoader getLoader() {
        return this.fxmlLoader;
    }

    private void setAnchor(Parent parent) {
        AnchorPane.setTopAnchor(parent, 0.0);
        AnchorPane.setBottomAnchor(parent, 0.0);
        AnchorPane.setLeftAnchor(parent, 0.0);
        AnchorPane.setRightAnchor(parent, 0.0);
    }

    public Node getNode() throws IOException {
        return (Node) fxmlLoader.load();
    }

    public Parent load() throws IOException {
        Parent parent = fxmlLoader.load();

        setAnchor(parent);

        return parent;
    }

    public Parent load(Boolean setAnchor) throws IOException {
        Parent parent = fxmlLoader.load();
        if (setAnchor) {
            setAnchor(parent);
        }
        return parent;
    }

    public Object getController() {
        return fxmlLoader.getController();
    }

    public void ShowPage(AnchorPane apLoadPane, Parent page) {
        apLoadPane.getChildren().setAll(page);
    }

    public void ShowPage(AnchorPane apLoadPane, String location) throws IOException {
        Parent page = new FXMLLoader(getClass().getResource(location)).load();
        setAnchor(page);
        apLoadPane.getChildren().setAll(page);
    }
}
