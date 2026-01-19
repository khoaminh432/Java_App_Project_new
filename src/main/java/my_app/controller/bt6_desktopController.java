package my_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import my_app.service.SettingFormatField;

public class bt6_desktopController {
    @FXML
    private Button btSubmit;

    @FXML
    private RadioButton rbCauA;

    @FXML
    private StackPane spPage;

    @FXML
    private RadioButton rbCauB;

    @FXML
    private RadioButton rbCauC;

    @FXML
    private RadioButton rbCauD;

    @FXML
    private RadioButton rbCauE;

    @FXML
    private ToggleGroup rgroup;

    @FXML
    private TextField tfnumber;
    
    public  void LoadPage(String fxmlFile) throws Exception {
        try {
            Parent page = FXMLLoader.load( 
                getClass().getResource(fxmlFile)
            );
            spPage.getChildren().setAll(page);

        } catch (Exception e) {
            System.out.println("Lỗi tải trang: " + e.getMessage());
        }
    }
    private void clearPage() {
        spPage.getChildren().clear();
    }
    
    private void setUserData(RadioButton radio, String data) {
        radio.setUserData(data);
    }
    @FXML
    private void initialize() throws Exception{
        SettingFormatField.setdoublefield(tfnumber);
        setUserData(rbCauA, "bt1_desktop.fxml");
        setUserData(rbCauB, "bt2_desktop.fxml");
        setUserData(rbCauC, "bt3_desktop.fxml");
        setUserData(rbCauD, "bt4_desktop.fxml");
        setUserData(rbCauE, "bt5_desktop.fxml");
        rgroup.selectedToggleProperty().addListener((obs,oldtoggle,newtoggle)->{
            if(newtoggle!=null){
                System.out.println("Đã chọn: " + newtoggle.getUserData());
                try {
                    LoadPage("/fxml/" + newtoggle.getUserData().toString());
                } catch (Exception e) {
                    System.out.println("Lỗi tải trang: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleSubmit(ActionEvent event){
        try {
            System.out.println("ddang chon:"+ rgroup.getSelectedToggle().getUserData());
            int number = Integer.parseInt(tfnumber.getText());
            System.out.println("Số vừa nhập: " + number);
            if (rbCauA.isSelected()) {
                System.out.println("Câu A được chọn.");
                LoadPage("/fxml/bt1_desktop.fxml");
            } else if (rbCauB.isSelected()) {
                System.out.println("Câu B được chọn.");
                LoadPage("/fxml/bt2_desktop.fxml");
            } else if (rbCauC.isSelected()) {
                System.out.println("Câu C được chọn.");
            } else if (rbCauD.isSelected()) {
                System.out.println("Câu D được chọn.");
            } else if (rbCauE.isSelected()) {
                System.out.println("Câu E được chọn.");
            } else {
                System.out.println("Không có câu nào được chọn.");
            }
        } catch (Exception e) {
            System.out.println("Vui lòng nhập số nguyên hợp lệ!");
        }
        
    }



}
