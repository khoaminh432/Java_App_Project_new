package  my_app;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import my_app.service.ConfigProperties;
public class App extends Application {

    private TextArea console;
    private static final String FILE = "/fxml/admin/btsinhvien.fxml";
    public URL checkclass(){
        return getClass().getResource(FILE);
    }
    private void testGUI(){
        this.console = new TextArea();
        this.console.setEditable(false);
        console.setWrapText(true);
        console.setPrefHeight(300);

        TextField input = new TextField();
        input.setPromptText("Nhập lệnh...");

        Button btnSend = new Button("Chạy");

        btnSend.setOnAction(e -> {
            String cmd = input.getText();
            log("> " + cmd);

            if ("clear".equalsIgnoreCase(cmd)) {
                console.clear();
            } else {
                log("Bạn đã nhập: " + cmd);
                log("DB User from config: " + ConfigProperties.getDbUser());
                log("DB Password from config: " + ConfigProperties.getDbPassword());
                log("DB URL from config: " + ConfigProperties.getDbUrl());
            }
            input.clear();
        });

        VBox root = new VBox(10, console, input, btnSend);
        root.setPadding(new Insets(10));
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FILE)
        );

        Scene scene = new Scene(loader.load());
        
        stage.setScene(scene);
        stage.setTitle("JavaFX App");
        stage.show();

        
    }

    private void log(String text) {
        console.appendText(text + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    
    }
}