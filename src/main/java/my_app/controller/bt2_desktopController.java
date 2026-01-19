package my_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Label;
public class bt2_desktopController {

    @FXML
    private Button btnCalc;

    @FXML
    private TextField tfLength;

    @FXML
    private TextField tfWidth;

      @FXML
    private Label lbArea;

    @FXML
    private Label lbPerimeter;

    @FXML
    private void initialize() {
        setdoublefield(tfLength);
        setdoublefield(tfWidth);
    }
    // xử lí chỉ cho nhập số thực
    private void setdoublefield(TextField textfield) {
        textfield.setTextFormatter(new TextFormatter<String>(change->{
            if  (change.getControlNewText().matches("-?\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }
    private double area(double length, double width) {
        return length*width;
    }
    private double perimeter(double length, double width) {
        return 2*(length+width);
    }
    @FXML
    private void handleCalc(ActionEvent event) {
        try{
            double length=Double.parseDouble(tfLength.getText());
            double width=Double.parseDouble(tfWidth.getText());
            double area=area(length, width);
            double perimeter=perimeter(length, width);
            lbArea.setText(String.format("%.2f", perimeter));
            lbPerimeter.setText(String.format("%.2f", area));
        }catch (Exception e){
            lbArea.setText("empty input");
            lbPerimeter.setText("empty input");
        }
    }

}
