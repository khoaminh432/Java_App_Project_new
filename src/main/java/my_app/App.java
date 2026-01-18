package my_app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import my_app.util.DBConnection;
import my_app.util.QueryExecutor;
public class App extends Application {
    private static final String APP_NAME = "JavaFX Console Application";
    private static final String APP_PATH = "/fxml/homepage.fxml";
    private TextArea console;

    @Override
    public void start(Stage stage) {
        DBConnection dbConn = DBConnection.getInstance();
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(APP_PATH)
        );
        QueryExecutor queryExecutor = new QueryExecutor();
        System.out.println(queryExecutor.ExecuteQuery("select * from customer"));
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
        stage.setOnCloseRequest(e ->
        {
            dbConn.close();
        }
        );
    }

    private void log(String text) {
        console.appendText(text + "\n"
    );
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
