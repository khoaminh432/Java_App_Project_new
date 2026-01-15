package my_app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import my_app.service.ConfigProperties;
import my_app.util.DBConnection;
public class App extends Application {

    private TextArea console;

    @Override
    public void start(Stage stage) {
        DBConnection dbConn = new DBConnection();
        System.out.println("DB Connection: " + dbConn.getConn());
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
                log("Bạn đã nhập: " + cmd+
                "\n(Chức năng khác chưa được triển khai)"+
                ConfigProperties.getDbUrl()
            + "\n"+ConfigProperties.getDbUser()
            + "\n"+ConfigProperties.getDbPassword()
                );
            }
            input.clear();
        });

        VBox root = new VBox(10, console, input, btnSend);
        root.setPadding(new Insets(10));

        stage.setTitle("JavaFX Console");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
        dbConn.close();
    }

    private void log(String text) {
        console.appendText(text + "\n"
    );
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
