package my_app.controller;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
public class bt1_desktopController {
    private HashMap<String,String> formCalc=new HashMap<>();

    @FXML
    private Button btnCalc;

    @FXML
    private RadioButton division;

    @FXML
    private ToggleGroup groupcalc;

    @FXML
    private Label lbResult;

    @FXML
    private RadioButton rdoAdd;

    @FXML
    private RadioButton rdoMultiplication;

    @FXML
    private RadioButton rdoSubtrac;

    @FXML
    private TextField tfNum1;

    @FXML
    private TextField tfNum2;
    private String error1="";
    private String error2="";

    @FXML
    private void initialize() {
        formCalc.put("Addition", "+");
        formCalc.put("Subtraction", "-");
        formCalc.put("Multiplication", "x");
        formCalc.put("Division", "/");
        setdoublefield(tfNum1);
        setdoublefield(tfNum2);
        rdoAdd.setUserData("Addition");
        rdoSubtrac.setUserData("Subtraction");
        rdoMultiplication.setUserData("Multiplication");
        division.setUserData("Division");
        rdoAdd.setSelected(true);
        groupcalc.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println(newValue.getUserData().toString());
            }
        });
        
    }
    private void setdoublefield(TextField textfield) {
        textfield.setTextFormatter(new TextFormatter<String>(change->{
            if  (change.getControlNewText().matches("-?\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }
    @FXML
    private void seterror(TextField textfield,String error) {
        textfield.setStyle("-fx-border-color: red;");
        textfield.setPromptText(error);
    }
    private void clearerror(TextField textfield) {
        textfield.setStyle("-fx-border-color: none;");
        textfield.setPromptText("");
    }
    private boolean vadidateform() {
        boolean isvalid=true;
        error1="";
        error2="";
        if(tfNum1.getText()==null || tfNum1.getText().isEmpty()) {
            error1="Vui lòng nhập số thứ nhất";seterror(tfNum1,error1);
            isvalid=false;
        }
        if(tfNum2.getText()==null || tfNum2.getText().isEmpty()) {
            error2="Vui lòng nhập số thứ hai";
            isvalid=false;seterror(tfNum2,error2);
        }
        return isvalid;
    }
    private double getnumfromfield(TextField textfield) {
        try {
            return Double.parseDouble(textfield.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    @FXML
    private void handlegroupcalc(ActionEvent event) {
        System.out.println(groupcalc.getSelectedToggle().getUserData().toString());
        }
        private double calculate(double num1,double num2,String operation) {
            double result=0;
            switch(operation) {
            case "Addition":
                result=num1+num2;
                break;
            case "Subtraction":
                result=num1-num2;
                break;
            case "Multiplication":
                result=num1*num2;
                break;
            case "Division":
                if(num2!=0) {
                    result=num1/num2;
                }else {
                    lbResult.setText("Lỗi: Không thể chia cho 0");
                }
                break;
            default:
                break;
            }
            return result;
        }
    @FXML
    private void handleCalc(ActionEvent event) {
        if(vadidateform()){
        clearerror(tfNum1);
        clearerror(tfNum2);
            double num1 = getnumfromfield(tfNum1);
            double num2 = getnumfromfield(tfNum2);
            double result=0;
            String operation=groupcalc.getSelectedToggle().getUserData().toString();
        result=calculate(num1,num2,operation);
        lbResult.setText("kết quả: "+num1+" "+formCalc.get(operation)+" "+num2+" = "+result);
        
        System.out.println(groupcalc.getSelectedToggle().getUserData().toString());
    }
}
}