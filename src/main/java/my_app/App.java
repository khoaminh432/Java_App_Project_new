package my_app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import my_app.service.LoadFileGUI;
import my_app.util.DBConnection;

public class App extends Application {

    private static final String APP_NAME = "JavaFX Console Application";
    private static final String APP_PATH = "/fxml/employee.fxml";
    private LoadFileGUI loader;
    private DBConnection dbConn;

    private void initDB() {
        dbConn = DBConnection.getInstance();
    }

    private void DesktopRoot() {
        loader = new LoadFileGUI(APP_PATH);
    }

    @Override
    public void start(Stage stage) {
        initDB();
        DesktopRoot();
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order.fxml"));
            Scene scene = new Scene(loader.load());

            stage.setTitle("JavaFX App");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e
                -> {
            dbConn.close();
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
