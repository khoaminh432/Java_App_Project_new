
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SupplierApp extends Application {
 @Override
 public void start(Stage main) throws Exception {
  Parent root = FXMLLoader.load( getClass().getResource("resource/fxml/admin/supplier/demo.fxml"));
  main.setTitle("SupplierManager");
  main.setScene( new Scene(root,400,200));
  main.show();
 }
 public static void main(String[] args)  {
  launch(args);
 }
}
