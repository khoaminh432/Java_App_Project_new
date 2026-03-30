package my_app;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import my_app.service.LoadFileGUI;
import my_app.util.DBConnection;

public class App extends Application {

    private static final String APP_NAME = "JavaFX Console Application";
    private static final String APP_PATH = "/fxml/admin/index.fxml";
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
            scene = new Scene(loader.load());
        } catch (IOException e) {
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
        );
    }

    public static void main(String[] args) {
        launch(args);
    }

}
