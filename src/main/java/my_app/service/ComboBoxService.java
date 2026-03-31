package my_app.service;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxService<T> {

    private ComboBox<T> comboBox;

    public ComboBoxService(ComboBox<T> comboBox) {
        this.comboBox = comboBox;
    }

    public void setData(ObservableList<T> data) {
        comboBox.setItems(data);
    }

    public T getSelected() {
        return comboBox.getValue();
    }

    public void setSelected(T value) {
        comboBox.setValue(value);
    }

    public void clear() {
        comboBox.getSelectionModel().clearSelection();
    }
}
