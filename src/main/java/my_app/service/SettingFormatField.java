package my_app.service;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class SettingFormatField {
    public static String formatNumber(double number) {
        Integer intPart = (int) number;
        System.out.println(intPart);
        if (number == intPart) {
            return String.format("%,d", intPart);
        }
        return String.format("%,.2f", number);
    }
    public static void setdoublefield(TextField textfield) {
        textfield.setTextFormatter(new TextFormatter<String>(change->{
            if  (change.getControlNewText().matches("-?\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }
}
