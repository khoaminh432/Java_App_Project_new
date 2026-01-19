package my_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import my_app.service.SettingFormatField;
public class bt4_desktopController {

    @FXML
    private Button btnCalc;

    @FXML
    private Label lbNumber;

    @FXML
    private Label lbNumberCheck;

    @FXML
    private TextField tfNumber;
    
    @FXML
    private void initialize() {
        SettingFormatField.setdoublefield(tfNumber);
    }
    @FXML
    void handleCalc(ActionEvent event) {
        try {
            int number = Integer.parseInt(tfNumber.getText());

            lbNumber.setText("Số " + number + ": ");
            lbNumberCheck.setText(number % 2 == 0 ? "even number" : "odd number");
        } catch (Exception e) {
            lbNumber.setText("Số :");
            lbNumberCheck.setText("Chưa nhập số đúng yêu cầu");
        }
    }

}
