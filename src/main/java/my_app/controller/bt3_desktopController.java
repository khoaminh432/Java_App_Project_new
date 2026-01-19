package my_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import my_app.service.SettingFormatField;

public class bt3_desktopController {
    private static final double PI = Math.PI;
    @FXML
    private Button btnCalc;

    @FXML
    private Label lbArea;

    @FXML
    private Label lbPerimeter;

    @FXML
    private TextField tfRadius;

    @FXML
    private void initialize() {
        SettingFormatField.setdoublefield(tfRadius);
    }

    @FXML
    void handleCalc(ActionEvent event) {
        try {
            double radius = Double.parseDouble(tfRadius.getText());
            double area = PI * radius * radius;
            double perimeter = 2 * PI * radius;
            lbArea.setText(String.format("%.2f", area));
            lbPerimeter.setText(String.format("%.2f", perimeter));
        } catch (Exception e) {
            lbArea.setText("empty input");
            lbPerimeter.setText("empty input");
        }
    }

}