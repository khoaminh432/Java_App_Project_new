package my_app.service;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
public class LoadFileGUI{
    private FXMLLoader floader;
    public  LoadFileGUI(String location){
        floader = new FXMLLoader(
            getClass().getResource(location)
        );
    }
    public FXMLLoader getLoader(){
        return this.floader;
    }
    public Parent load(){
        return this.floader.load();
    }
}
