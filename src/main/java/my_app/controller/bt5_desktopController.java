package my_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class bt5_desktopController {
    @FXML
    private Button btnCalc;

    @FXML
    private Label lbNumber;

    @FXML
    private Label lbNumberCheck;

    @FXML
    private TextField tfNumber;
    protected  boolean isprime(int number){
        if(number<2) return false;
        if(number==2) return true;
        if(number%2==0) return false;
        for(int i=3;i<=Math.sqrt(number);i+=2){
            if(number%i==0) return false;
        }
        return true;
    }
    @FXML
    void handleCalc(ActionEvent event) {
        try{
            int number = Integer.parseInt(tfNumber.getText());
            lbNumber.setText("Số " + number + ": ");
            System.out.println(isprime(number));
            lbNumberCheck.setText(isprime(number) ? "là số nguyên tố" : "không phải số nguyên tố");
        }catch(Exception e){
            lbNumber.setText("Số :");
            lbNumberCheck.setText("Chưa nhập số đúng yêu cầu");
        }
    }
    

}