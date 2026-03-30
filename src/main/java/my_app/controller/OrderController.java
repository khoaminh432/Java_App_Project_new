package my_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class OrderController {

    @FXML
    private TableColumn<?, ?> customer;

    @FXML
    private TableColumn<?, ?> date;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> status;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> total;
    @FXML
    public void initialize() {
    }
};