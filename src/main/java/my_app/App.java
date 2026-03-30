package my_app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import my_app.util.DBConnection;
import my_app.util.QueryExecutor;
public class App extends Application {
    private static final String APP_NAME = "JavaFX Console Application";
    private static final String APP_PATH = "/view/oder.fxml";

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(APP_PATH)
        );
        // Thử query database
        /*try {
            QueryExecutor queryExecutor = new QueryExecutor();
            System.out.println(queryExecutor.ExecuteQuery("select * from customer"));
        } catch (Exception e) {
            System.err.println("Cảnh báo: Lỗi database - " + e.getMessage());
        }*/
        
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            System.err.println("Lỗi load FXML: " + e.getMessage());
            return;
        }
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            try {
                DBConnection.close();
            } catch (Exception ex) {
                // Ignore if DB not connected
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
