package my_app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import my_app.bus.OrderBUS;
import my_app.dto.OrderDTO;

public class OrderController implements Initializable {

    @FXML private TableColumn<InformationOd, Integer> id;
    @FXML private TableColumn<InformationOd, String> customer;
    @FXML private TableColumn<InformationOd, String> date;
    @FXML private TableColumn<InformationOd, String> status;
    @FXML private TableColumn<InformationOd, String> total;
    @FXML private TableView<InformationOd> table;

    @FXML private TextField txtOrderId;
    @FXML private ComboBox<String> cbCustomer;
    @FXML private DatePicker dpOrderDate;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField txtSearch;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtTotal;

    private OrderBUS bus = new OrderBUS();
    ObservableList<InformationOd> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        id.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        customer.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCustomer()));
        date.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
        status.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        total.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTotal()));

        cbCustomer.getItems().addAll("Nguyen Van A", "Nguyen Van B");
        cbCustomer.setEditable(true);
        cbStatus.getItems().addAll("pending", "completed");

        table.setOnMouseClicked(e -> fillForm());

        loadTable();
    }

    public void loadTable() {
        list.clear();
        for (OrderDTO o : bus.getAll()) {
            list.add(new InformationOd(
                    o.getId(),
                    o.getCustomer(),
                    o.getDate(),
                    o.getStatus(),
                    o.getTotal()
            ));
        }
        table.setItems(list);
    }

    public void fillForm() {
        InformationOd o = table.getSelectionModel().getSelectedItem();
        if (o != null) {
            txtOrderId.setText(String.valueOf(o.getId()));
            cbCustomer.setValue(o.getCustomer());
            dpOrderDate.setValue(java.time.LocalDate.parse(o.getDate()));
            cbStatus.setValue(o.getStatus());
        }
    }

    public void handleNew() {
        txtOrderId.clear();
        cbCustomer.setValue(null);
        dpOrderDate.setValue(null);
        cbStatus.setValue(null);
    }

    public void handleAdd() {
        OrderDTO o = new OrderDTO();
        o.setCustomer(cbCustomer.getValue());
        o.setDate(dpOrderDate.getValue().toString());
        o.setStatus(cbStatus.getValue());
        o.setTotal("100000 VND");

        bus.insert(o);
        loadTable();
    }

    public void handleUpdate() {
        if (txtOrderId.getText().isEmpty()) return;

        OrderDTO o = new OrderDTO();
        o.setId(Integer.parseInt(txtOrderId.getText()));
        o.setCustomer(cbCustomer.getValue());
        o.setDate(dpOrderDate.getValue().toString());
        o.setStatus(cbStatus.getValue());
        o.setTotal("100000 VND");

        bus.update(o);
        loadTable();
    }

    public void handleDelete() {
        InformationOd selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            bus.delete(selected.getId());
            loadTable();
        }
    }

    public void handleSearch() {
        String keyword = txtSearch.getText().toLowerCase();
        ObservableList<InformationOd> filtered = FXCollections.observableArrayList();

        for (InformationOd o : list) {
            if (o.getCustomer().toLowerCase().contains(keyword)) {
                filtered.add(o);
            }
        }
        table.setItems(filtered);
    }

    public void handleStatistic() {
        int totalOrders = list.size();
        System.out.println("Tổng đơn: " + totalOrders);
    }

    public void goOrder() {
        System.out.println("Trang Order");
    }

    public void goProduct() {
        System.out.println("Trang Product");
    }

    public void goCustomer() {
        System.out.println("Trang Customer");
    }
}
