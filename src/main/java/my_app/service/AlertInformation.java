package my_app.service;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertInformation {

    private static final Alert alerterror = new Alert(AlertType.ERROR);
    private static final Alert alertinfo = new Alert(AlertType.INFORMATION);
    private static final Alert alertwarning = new Alert(AlertType.WARNING);
    private static final Alert alertconfirmation = new Alert(AlertType.CONFIRMATION);

    private static void setupAlert(Alert alert, String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
    }

    public static void showErrorAlert(String title, String header, String content) {
        setupAlert(alerterror, title, header, content);
        alerterror.show();
    }

    public static void showInfoAlert(String title, String header, String content) {
        setupAlert(alertinfo, title, header, content);
        alertinfo.show();
    }

    public static void showWarningAlert(String title, String header, String content) {
        setupAlert(alertwarning, title, header, content);
        alertwarning.show();
    }

    public static void showConfirmationAlert(String title, String header, String content) {
        setupAlert(alertconfirmation, title, header, content);
        alertconfirmation.show();
    }

}
