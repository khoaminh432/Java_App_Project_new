package my_app.service;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
public class SwitchPage {
    
    public static void switchPage(AnchorPane apLoadPane, Parent page){
        apLoadPane.getChildren().setAll(page);
    }   
}
