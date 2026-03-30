package my_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import my_app.util.DBConnection;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order.fxml"));
            Scene scene = new Scene(loader.load());

            stage.setTitle("JavaFX App");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest(e -> {
            try {
                DBConnection.close();
            } catch (Exception ex) {
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}