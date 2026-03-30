package my_app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import my_app.model.Customer;
import my_app.service.CustomerService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerController {
    
    private CustomerService customerService = new CustomerService();
    private List<Customer> currentCustomerList;
    
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> colSTT;
    @FXML
    private TableColumn<Customer, String> colId;
    @FXML
    private TableColumn<Customer, String> colFullName;
    @FXML
    private TableColumn<Customer, String> colPhone;
    @FXML
    private TableColumn<Customer, String> colDateOfBirth;
    @FXML
    private TableColumn<Customer, String> colAddress;
    @FXML
    private TableColumn<Customer, String> colType;
    @FXML
    private TableColumn<Customer, String> colPoints;
    @FXML
    private TableColumn<Customer, String> colStatus;
    @FXML
    private TableColumn<Customer, Void> colActions;
    
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnAdd;
    
    @FXML
    private ComboBox<String> cbFilterType;
    @FXML
    private ComboBox<String> cbFilterStatus;
    
    @FXML
    private Label lblTotalCustomers;
    @FXML
    private Label lblActiveCustomers;
    @FXML
    private Label lblInactiveCustomers;
    @FXML
    private Label lblTotalPoints;
    
    @FXML
    private Label lblShowingRecords;
    @FXML
    private Button btnPrevPage;
    @FXML
    private Button btnNextPage;
    @FXML
    private Label lblPageInfo;
    
    @FXML
    private VBox detailPanel;
    @FXML
    private Label lblPanelTitle;
    @FXML
    private Button btnClosePanel;
    
    @FXML
    private TextField txtCustomerId;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtPhone;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private TextArea txtAddress;
    @FXML
    private ComboBox<String> cbType;
    @FXML
    private TextField txtPoints;
    @FXML
    private ComboBox<String> cbStatus;
    
    @FXML
    private Label lblError;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    
    private int currentPage = 1;
    private int itemsPerPage = 10;
    private int totalPages = 1;
    
    @FXML
    public void initialize() {
        setupTableColumns();
        loadCustomerData();
        setupFilters();
        updateStatistics();
        hideDetailPanel();
    }
    
    private void setupTableColumns() {
        colSTT.setCellFactory(col -> new TableCell<Customer, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1 + (currentPage - 1) * itemsPerPage));
                }
            }
        });
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // For now, set dummy values for missing fields
        colDateOfBirth.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(""));
        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(""));
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("VIP"));
        colPoints.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("0"));
        
        colActions.setCellFactory(col -> new TableCell<Customer, Void>() {
            private final Button editBtn = new Button("Sửa");
            private final Button deleteBtn = new Button("Xóa");
            
            {
                editBtn.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    showEditForm(customer);
                });
                deleteBtn.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    handleDeleteCustomer(customer);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });
    }
    
    private void setupFilters() {
        cbFilterType.getItems().addAll("Tất cả", "VIP", "Thường");
        cbFilterType.setValue("Tất cả");
        
        cbFilterStatus.getItems().addAll("Tất cả", "active", "inactive");
        cbFilterStatus.setValue("Tất cả");
        
        cbType.getItems().addAll("VIP", "Thường");
        cbStatus.getItems().addAll("active", "inactive");
    }
    
    private void loadCustomerData() {
        try {
            currentCustomerList = customerService.getAllCustomers();
            updateTableDisplay();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu khách hàng: " + e.getMessage());
        }
    }
    
    private void updateTableDisplay() {
        if (currentCustomerList == null) return;
        
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, currentCustomerList.size());
        
        List<Customer> pageData = currentCustomerList.subList(startIndex, endIndex);
        customerTable.getItems().setAll(pageData);
        
        totalPages = (int) Math.ceil((double) currentCustomerList.size() / itemsPerPage);
        updatePaginationInfo();
    }
    
    private void updatePaginationInfo() {
        int start = (currentPage - 1) * itemsPerPage + 1;
        int end = Math.min(currentPage * itemsPerPage, currentCustomerList.size());
        lblShowingRecords.setText(String.format("Hiển thị %d - %d trên tổng %d khách hàng", 
            start, end, currentCustomerList.size()));
        lblPageInfo.setText(String.format("Trang %d / %d", currentPage, totalPages));
        
        btnPrevPage.setDisable(currentPage <= 1);
        btnNextPage.setDisable(currentPage >= totalPages);
    }
    
    private void updateStatistics() {
        if (currentCustomerList == null) return;
        
        lblTotalCustomers.setText(String.valueOf(currentCustomerList.size()));
        long activeCount = currentCustomerList.stream()
            .filter(c -> "active".equalsIgnoreCase(c.getStatus()))
            .count();
        long inactiveCount = currentCustomerList.size() - activeCount;
        
        lblActiveCustomers.setText(String.valueOf(activeCount));
        lblInactiveCustomers.setText(String.valueOf(inactiveCount));
        lblTotalPoints.setText("0"); // Placeholder
    }
    
    @FXML
    public void handleRefresh() {
        loadCustomerData();
        updateStatistics();
    }
    
    @FXML
    public void handleSearch() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            currentCustomerList = customerService.getAllCustomers();
        } else {
            currentCustomerList = customerService.searchByName(searchText);
        }
        currentPage = 1;
        updateTableDisplay();
        updateStatistics();
    }
    
    @FXML
    public void handleAddCustomer() {
        showAddForm();
    }
    
    @FXML
    public void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateTableDisplay();
        }
    }
    
    @FXML
    public void handleNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            updateTableDisplay();
        }
    }
    
    private void showAddForm() {
        clearForm();
        lblPanelTitle.setText("Thêm Khách Hàng Mới");
        btnDelete.setVisible(false);
        btnDelete.setManaged(false);
        detailPanel.setVisible(true);
        detailPanel.setManaged(true);
    }
    
    private void showEditForm(Customer customer) {
        populateForm(customer);
        lblPanelTitle.setText("Chỉnh Sửa Khách Hàng");
        btnDelete.setVisible(true);
        btnDelete.setManaged(true);
        detailPanel.setVisible(true);
        detailPanel.setManaged(true);
    }
    
    private void hideDetailPanel() {
        detailPanel.setVisible(false);
        detailPanel.setManaged(false);
    }
    
    @FXML
    public void handleClosePanel() {
        hideDetailPanel();
    }
    
    @FXML
    public void handleSave() {
        if (validateForm()) {
            Customer customer = createCustomerFromForm();
            try {
                if (txtCustomerId.getText().isEmpty()) {
                    // Add new
                    customerService.addCustomer(customer);
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm khách hàng thành công!");
                } else {
                    // Update existing
                    int id = Integer.parseInt(txtCustomerId.getText());
                    customerService.updateCustomer(id, customer);
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật khách hàng thành công!");
                }
                handleRefresh();
                hideDetailPanel();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể lưu khách hàng: " + e.getMessage());
            }
        }
    }
    
    @FXML
    public void handleDelete() {
        if (!txtCustomerId.getText().isEmpty()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Xác nhận xóa");
            confirm.setHeaderText("Bạn có chắc muốn xóa khách hàng này?");
            confirm.setContentText("Hành động này không thể hoàn tác.");
            
            if (confirm.showAndWait().get() == ButtonType.OK) {
                try {
                    int id = Integer.parseInt(txtCustomerId.getText());
                    customerService.deleteCustomer(id);
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa khách hàng thành công!");
                    handleRefresh();
                    hideDetailPanel();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa khách hàng: " + e.getMessage());
                }
            }
        }
    }
    
    @FXML
    public void handleCancel() {
        hideDetailPanel();
    }
    
    private void handleDeleteCustomer(Customer customer) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Bạn có chắc muốn xóa khách hàng " + customer.getFullName() + "?");
        confirm.setContentText("Hành động này không thể hoàn tác.");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                customerService.deleteCustomer(customer.getId());
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa khách hàng thành công!");
                handleRefresh();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa khách hàng: " + e.getMessage());
            }
        }
    }
    
    private void clearForm() {
        txtCustomerId.clear();
        txtLastName.clear();
        txtFirstName.clear();
        txtPhone.clear();
        dpDateOfBirth.setValue(null);
        txtAddress.clear();
        cbType.setValue(null);
        txtPoints.clear();
        cbStatus.setValue("active");
        lblError.setText("");
    }
    
    private void populateForm(Customer customer) {
        txtCustomerId.setText(customer.getId().toString());
        // Split full name into last and first name (simplified)
        String[] nameParts = customer.getFullName().split(" ");
        if (nameParts.length > 1) {
            txtLastName.setText(nameParts[0]);
            txtFirstName.setText(customer.getFullName().substring(nameParts[0].length()).trim());
        } else {
            txtFirstName.setText(customer.getFullName());
        }
        txtPhone.setText(customer.getPhoneNumber());
        txtAddress.clear(); // Not in model
        cbType.setValue("VIP"); // Default
        txtPoints.setText("0"); // Not in model
        cbStatus.setValue(customer.getStatus());
        lblError.setText("");
    }
    
    private Customer createCustomerFromForm() {
        String fullName = (txtLastName.getText() + " " + txtFirstName.getText()).trim();
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setPhoneNumber(txtPhone.getText());
        customer.setEmail(""); // Not in form
        customer.setPassword("password123"); // Default
        customer.setStatus(cbStatus.getValue());
        return customer;
    }
    
    private boolean validateForm() {
        if (txtFirstName.getText().trim().isEmpty()) {
            lblError.setText("Vui lòng nhập tên!");
            return false;
        }
        if (txtPhone.getText().trim().isEmpty()) {
            lblError.setText("Vui lòng nhập số điện thoại!");
            return false;
        }
        lblError.setText("");
        return true;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}