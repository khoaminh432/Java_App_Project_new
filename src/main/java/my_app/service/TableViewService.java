package my_app.service;

import javafx.scene.control.TableView;

public class TableViewService<T> {

    private TableView<T> tbw;

    public TableViewService(TableView<T> tbw) {
        this.tbw = tbw;
    }

    public T getSelectedItem() {
        return tbw.getSelectionModel().getSelectedItem();
    }
}
