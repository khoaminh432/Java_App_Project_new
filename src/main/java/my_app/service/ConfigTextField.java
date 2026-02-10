package my_app.service;

import javafx.scene.control.TextField;

public class ConfigTextField {

    public static final int MAX_LENGTH_TEXTFIELD = 255;
    private TextField textField;

    public ConfigTextField(TextField textField) {
        this.textField = textField;
    }

    public static void AcceptOnlyNumber(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
