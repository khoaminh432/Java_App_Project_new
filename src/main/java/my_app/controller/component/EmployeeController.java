package my_app.controller.component;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import my_app.bus.EmployeeBus;
import my_app.dao.RoleDao;
import my_app.model.Employee;
import my_app.model.Role;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeController implements Initializable {

    // ==================== FXML COMPONENTS ====================

    // -- Top: Search & Filter --
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRefresh;
    @FXML
    private ComboBox<String> cbFilterRole;
    @FXML
    private ComboBox<String> cbFilterStatus;
    @FXML
    private Button btnAdd;

    // -- Center: Statistics --
    @FXML
    private Label lblTotalEmployees;
    @FXML
    private Label lblActiveEmployees;
    @FXML
    private Label lblInactiveEmployees;
    @FXML
    private Label lblTotalSalary;

    // -- Center: Table --
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> colSTT;
    @FXML
    private TableColumn<Employee, Integer> colId;
    @FXML
    private TableColumn<Employee, String> colFullName;
    @FXML
    private TableColumn<Employee, String> colPhone;
    @FXML
    private TableColumn<Employee, String> colDateOfBirth;
    @FXML
    private TableColumn<Employee, String> colAddress;
    @FXML
    private TableColumn<Employee, String> colRole;
    @FXML
    private TableColumn<Employee, String> colSalary;
    @FXML
    private TableColumn<Employee, String> colStatus;
    @FXML
    private TableColumn<Employee, Void> colActions;

    // -- Center: Pagination --
    @FXML
    private Label lblShowingRecords;
    @FXML
    private Label lblPageInfo;
    @FXML
    private Button btnPrevPage;
    @FXML
    private Button btnNextPage;

    // -- Right: Detail Panel --
    @FXML
    private VBox detailPanel;
    @FXML
    private Label lblPanelTitle;
    @FXML
    private Button btnClosePanel;
    @FXML
    private TextField txtEmployeeId;
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
    private ComboBox<String> cbRole;
    @FXML
    private TextField txtSalary;
    @FXML
    private ComboBox<String> cbStatus;
    @FXML
    private Label lblError;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDelete;

    // ==================== PROPERTIES ====================

    private EmployeeBus employeeBus;
    private ObservableList<Employee> employeeList;
    private List<Employee> allEmployees;
    private List<Employee> filteredEmployees;

    // Role mapping: roleName -> roleId
    private Map<String, Integer> roleNameToId = new HashMap<>();
    private Map<Integer, String> roleIdToName = new HashMap<>();

    private boolean isEditMode = false;
    private Employee selectedEmployee = null;

    // Pagination
    private int currentPage = 1;
    private int pageSize = 20;
    private int totalPages = 1;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DecimalFormat currencyFormat = new DecimalFormat("#,###");

    // ==================== INITIALIZATION ====================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            employeeBus = new EmployeeBus();
            employeeList = FXCollections.observableArrayList();
            allEmployees = new ArrayList<>();
            filteredEmployees = new ArrayList<>();

            loadRoles();
            setupTable();
            setupDatePickers();
            setupRoleComboBox();
            setupFilters();
            loadEmployeeData();
        } catch (Exception e) {
            System.err.println("Warning: Error during Employee Controller initialization: " + e.getMessage());
            e.printStackTrace();
            // Hiển thị bảng trống thay vì crash
            employeeList = FXCollections.observableArrayList();
        }
    }

    /**
     * Load roles from DB for mapping roleId <-> roleName
     */
    private void loadRoles() {
        try {
            RoleDao roleDao = new RoleDao();
            List<Role> roles = roleDao.findAll();
            for (Role r : roles) {
                if (r.getRoleName() != null) {
                    roleNameToId.put(r.getRoleName(), r.getId());
                    roleIdToName.put(r.getId(), r.getRoleName());
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể tải danh sách chức vụ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get role name from roleId
     */
    private String getRoleNameById(Integer roleId) {
        if (roleId == null)
            return "";
        return roleIdToName.getOrDefault(roleId, "");
    }

    /**
     * Check if employee status is "active"
     */
    private boolean isActive(Employee emp) {
        return emp.getStatus() != null && emp.getStatus().equalsIgnoreCase("active");
    }

    // ==================== TABLE SETUP ====================

    private void setupTable() {
        // Cột STT
        colSTT.setCellValueFactory(cellData -> {
            int index = employeeTable.getItems().indexOf(cellData.getValue());
            int stt = (currentPage - 1) * pageSize + index + 1;
            return new SimpleIntegerProperty(stt).asObject();
        });

        // Cột Mã NV
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Cột Họ và Tên
        colFullName.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            String fullName = (emp.getLastName() != null ? emp.getLastName() : "")
                    + " "
                    + (emp.getFirstName() != null ? emp.getFirstName() : "");
            return new SimpleStringProperty(fullName.trim());
        });

        // Cột SĐT
        colPhone.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            return new SimpleStringProperty(emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "");
        });

        // Cột Ngày sinh
        colDateOfBirth.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            if (emp.getDob() != null) {
                return new SimpleStringProperty(emp.getDob().format(dateFormatter));
            }
            return new SimpleStringProperty("");
        });

        // Cột Địa chỉ
        colAddress.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            return new SimpleStringProperty(emp.getAddress() != null ? emp.getAddress() : "");
        });

        // Cột Chức vụ
        colRole.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            String roleName = getRoleNameById(emp.getRoleId());
            return new SimpleStringProperty(roleName);
        });

        // Cột Lương
        colSalary.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            if (emp.getBasicSalary() != null) {
                return new SimpleStringProperty(currencyFormat.format(emp.getBasicSalary()));
            }
            return new SimpleStringProperty("0");
        });

        // Cột Trạng thái (có badge màu)
        colStatus.setCellFactory(column -> new TableCell<Employee, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Employee emp = getTableRow().getItem();
                Label badge = new Label();
                if (isActive(emp)) {
                    badge.setText("Đang làm việc");
                    badge.setStyle(
                            "-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-padding: 3 8; -fx-background-radius: 10; -fx-font-size: 11px; -fx-font-weight: bold;");
                } else {
                    badge.setText("Đã nghỉ việc");
                    badge.setStyle(
                            "-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-padding: 3 8; -fx-background-radius: 10; -fx-font-size: 11px; -fx-font-weight: bold;");
                }
                setGraphic(badge);
                setAlignment(Pos.CENTER);
            }
        });
        colStatus.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            return new SimpleStringProperty(isActive(emp) ? "Đang làm việc" : "Đã nghỉ việc");
        });

        // Cột Thao tác (nút Sửa, Xóa)
        colActions.setCellFactory(column -> new TableCell<Employee, Void>() {
            private final Button btnEdit = new Button("✏ Sửa");
            private final Button btnDel = new Button("🗑 Xóa");
            private final HBox container = new HBox(8, btnEdit, btnDel);

            {
                btnEdit.setStyle(
                        "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 4; -fx-cursor: hand; -fx-padding: 4 10;");
                btnDel.setStyle(
                        "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 4; -fx-cursor: hand; -fx-padding: 4 10;");
                container.setAlignment(Pos.CENTER);

                btnEdit.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    handleEditEmployee(emp);
                });

                btnDel.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    handleDeleteEmployee(emp);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        // Double-click để xem chi tiết
        employeeTable.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    handleEditEmployee(row.getItem());
                }
            });
            return row;
        });

        employeeTable.setItems(employeeList);
    }

    /**
     * Thiết lập DatePicker format
     */
    private void setupDatePickers() {
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        };
        dpDateOfBirth.setConverter(converter);
    }

    /**
     * Thiết lập ComboBox chức vụ (từ DB)
     */
    private void setupRoleComboBox() {
        ObservableList<String> roleNames = FXCollections.observableArrayList(roleNameToId.keySet());
        cbRole.setItems(roleNames);
    }

    /**
     * Thiết lập bộ lọc
     */
    private void setupFilters() {
        // Khởi tạo items cho ComboBox bộ lọc chức vụ
        List<String> roleFilterItems = new ArrayList<>();
        roleFilterItems.add("Tất cả");
        roleFilterItems.addAll(roleNameToId.keySet());
        cbFilterRole.setItems(FXCollections.observableArrayList(roleFilterItems));

        // Khởi tạo items cho ComboBox bộ lọc trạng thái
        cbFilterStatus.setItems(FXCollections.observableArrayList(
                "Tất cả", "Đang làm việc", "Đã nghỉ việc"));

        // Khởi tạo items cho ComboBox trạng thái trong form
        cbStatus.setItems(FXCollections.observableArrayList(
                "active", "inactive"));

        // Lọc khi thay đổi ComboBox chức vụ
        cbFilterRole.setOnAction(event -> applyFilters());
        // Lọc khi thay đổi ComboBox trạng thái
        cbFilterStatus.setOnAction(event -> applyFilters());
        // Tìm kiếm realtime khi gõ
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    // ==================== DATA LOADING ====================

    /**
     * Tải dữ liệu nhân viên từ DB
     */
    private void loadEmployeeData() {
        try {
            allEmployees = employeeBus.fetchAllFromDb();
            if (allEmployees == null) {
                allEmployees = new ArrayList<>();
            }
            filteredEmployees = new ArrayList<>(allEmployees);
            currentPage = 1;
            updateTable();
            updateStatistics();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật bảng theo trang hiện tại
     */
    private void updateTable() {
        totalPages = Math.max(1, (int) Math.ceil((double) filteredEmployees.size() / pageSize));
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredEmployees.size());

        List<Employee> pageData;
        if (fromIndex >= filteredEmployees.size()) {
            pageData = new ArrayList<>();
        } else {
            pageData = filteredEmployees.subList(fromIndex, toIndex);
        }

        employeeList.setAll(pageData);

        // Cập nhật pagination labels
        int total = filteredEmployees.size();
        if (total == 0) {
            lblShowingRecords.setText("Hiển thị 0 - 0 trên tổng 0 nhân viên");
        } else {
            lblShowingRecords.setText(String.format("Hiển thị %d - %d trên tổng %d nhân viên",
                    fromIndex + 1, toIndex, total));
        }
        lblPageInfo.setText(String.format("Trang %d / %d", currentPage, totalPages));

        btnPrevPage.setDisable(currentPage <= 1);
        btnNextPage.setDisable(currentPage >= totalPages);
    }

    /**
     * Cập nhật thẻ thống kê
     */
    private void updateStatistics() {
        int total = allEmployees.size();
        long active = allEmployees.stream()
                .filter(this::isActive)
                .count();
        long inactive = total - active;

        BigDecimal totalSalary = allEmployees.stream()
                .filter(this::isActive)
                .filter(e -> e.getBasicSalary() != null)
                .map(Employee::getBasicSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        lblTotalEmployees.setText(String.valueOf(total));
        lblActiveEmployees.setText(String.valueOf(active));
        lblInactiveEmployees.setText(String.valueOf(inactive));
        lblTotalSalary.setText(currencyFormat.format(totalSalary) + " VNĐ");
    }

    // ==================== FILTER / SEARCH ====================

    /**
     * Áp dụng bộ lọc và tìm kiếm
     */
    private void applyFilters() {
        String keyword = txtSearch.getText() != null ? txtSearch.getText().trim().toLowerCase() : "";
        String roleFilter = cbFilterRole.getValue();
        String statusFilter = cbFilterStatus.getValue();

        filteredEmployees = allEmployees.stream()
                .filter(emp -> {
                    // Lọc theo từ khóa
                    if (!keyword.isEmpty()) {
                        String fullName = ((emp.getLastName() != null ? emp.getLastName() : "")
                                + " " + (emp.getFirstName() != null ? emp.getFirstName() : "")).toLowerCase();
                        String phone = emp.getPhoneNumber() != null ? emp.getPhoneNumber().toLowerCase() : "";
                        String idStr = String.valueOf(emp.getId());

                        boolean matchKeyword = fullName.contains(keyword)
                                || phone.contains(keyword)
                                || idStr.contains(keyword);
                        if (!matchKeyword)
                            return false;
                    }
                    return true;
                })
                .filter(emp -> {
                    // Lọc theo chức vụ
                    if (roleFilter == null || roleFilter.isEmpty() || roleFilter.equals("Tất cả")) {
                        return true;
                    }
                    String empRoleName = getRoleNameById(emp.getRoleId());
                    return roleFilter.equals(empRoleName);
                })
                .filter(emp -> {
                    // Lọc theo trạng thái
                    if (statusFilter == null || statusFilter.isEmpty() || statusFilter.equals("Tất cả")) {
                        return true;
                    }
                    if (statusFilter.equals("Đang làm việc")) {
                        return isActive(emp);
                    } else if (statusFilter.equals("Đã nghỉ việc")) {
                        return !isActive(emp);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        currentPage = 1;
        updateTable();
    }

    // ==================== EVENT HANDLERS ====================

    @FXML
    private void handleSearch() {
        applyFilters();
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        cbFilterRole.setValue(null);
        cbFilterRole.setPromptText("-- Chức vụ --");
        cbFilterStatus.setValue(null);
        cbFilterStatus.setPromptText("-- Trạng thái --");
        handleClosePanel();
        loadEmployeeData();
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateTable();
        }
    }

    @FXML
    private void handleNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            updateTable();
        }
    }

    // ==================== PANEL: ADD / EDIT ====================

    @FXML
    private void handleAddEmployee() {
        isEditMode = false;
        selectedEmployee = null;
        clearForm();
        lblPanelTitle.setText("Thêm Nhân Viên Mới");
        btnDelete.setVisible(false);
        btnDelete.setManaged(false);
        txtEmployeeId.setText("Tự động tạo");
        cbStatus.setValue("active");
        showDetailPanel();
    }

    /**
     * Xử lý khi nhấn nút Sửa trên bảng
     */
    private void handleEditEmployee(Employee employee) {
        if (employee == null)
            return;

        isEditMode = true;
        selectedEmployee = employee;
        clearForm();
        lblPanelTitle.setText("Chỉnh Sửa Nhân Viên");
        btnDelete.setVisible(true);
        btnDelete.setManaged(true);

        populateForm(employee);
        showDetailPanel();
    }

    /**
     * Điền dữ liệu nhân viên vào form
     */
    private void populateForm(Employee emp) {
        txtEmployeeId.setText(String.valueOf(emp.getId()));
        txtLastName.setText(emp.getLastName() != null ? emp.getLastName() : "");
        txtFirstName.setText(emp.getFirstName() != null ? emp.getFirstName() : "");
        txtPhone.setText(emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "");
        dpDateOfBirth.setValue(emp.getDob());
        txtAddress.setText(emp.getAddress() != null ? emp.getAddress() : "");

        // Chức vụ - lookup role name from roleId
        String roleName = getRoleNameById(emp.getRoleId());
        cbRole.setValue(roleName.isEmpty() ? null : roleName);

        txtSalary.setText(emp.getBasicSalary() != null ? emp.getBasicSalary().toPlainString() : "");
        cbStatus.setValue(emp.getStatus() != null ? emp.getStatus() : "active");
    }

    // ==================== PANEL: SAVE ====================

    @FXML
    private void handleSave() {
        lblError.setText("");

        // Validate
        if (!validateForm())
            return;

        try {
            Employee emp;
            if (isEditMode) {
                emp = selectedEmployee;
            } else {
                emp = new Employee();
            }

            // Lấy dữ liệu từ form
            emp.setLastName(txtLastName.getText().trim());
            emp.setFirstName(txtFirstName.getText().trim());
            emp.setPhoneNumber(txtPhone.getText().trim());
            emp.setDob(dpDateOfBirth.getValue());
            emp.setAddress(txtAddress.getText() != null ? txtAddress.getText().trim() : "");

            // Chức vụ - lookup roleId from roleName
            String selectedRoleName = cbRole.getValue();
            if (selectedRoleName != null && roleNameToId.containsKey(selectedRoleName)) {
                emp.setRoleId(roleNameToId.get(selectedRoleName));
            }

            emp.setBasicSalary(new BigDecimal(txtSalary.getText().trim().replace(",", "")));
            emp.setStatus(cbStatus.getValue());

            int result;
            if (isEditMode) {
                result = employeeBus.update(emp);
                if (result > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật nhân viên thành công!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Cập nhật nhân viên thất bại!");
                    return;
                }
            } else {
                result = employeeBus.create(emp);
                if (result > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm nhân viên mới thành công!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Thêm nhân viên thất bại!");
                    return;
                }
            }

            handleClosePanel();
            loadEmployeeData();

        } catch (NumberFormatException e) {
            lblError.setText("Lương không hợp lệ. Vui lòng nhập số.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== PANEL: DELETE ====================

    @FXML
    private void handleDelete() {
        if (selectedEmployee == null)
            return;
        handleDeleteEmployee(selectedEmployee);
    }

    /**
     * Xử lý xóa nhân viên
     */
    private void handleDeleteEmployee(Employee employee) {
        if (employee == null)
            return;

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Xóa nhân viên: " + employee.getLastName() + " " + employee.getFirstName());
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa nhân viên này?\nHành động này không thể hoàn tác.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int deleteResult = employeeBus.delete(employee.getId());
                if (deleteResult > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa nhân viên thành công!");
                    handleClosePanel();
                    loadEmployeeData();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa nhân viên thất bại!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi xóa: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ==================== PANEL: OTHER ACTIONS ====================

    @FXML
    private void handleCancel() {
        handleClosePanel();
    }

    @FXML
    private void handleClosePanel() {
        detailPanel.setVisible(false);
        detailPanel.setManaged(false);
        clearForm();
        selectedEmployee = null;
        isEditMode = false;
        employeeTable.getSelectionModel().clearSelection();
    }

    // ==================== VALIDATION ====================

    /**
     * Validate form nhập liệu
     */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (txtLastName.getText() == null || txtLastName.getText().trim().isEmpty()) {
            errors.append("• Họ và tên đệm không được để trống.\n");
        }
        if (txtFirstName.getText() == null || txtFirstName.getText().trim().isEmpty()) {
            errors.append("• Tên không được để trống.\n");
        }
        if (txtPhone.getText() == null || txtPhone.getText().trim().isEmpty()) {
            errors.append("• Số điện thoại không được để trống.\n");
        } else if (!txtPhone.getText().trim().matches("0\\d{9}")) {
            errors.append("• Số điện thoại không hợp lệ (VD: 0912345678).\n");
        }
        if (cbRole.getValue() == null || cbRole.getValue().isEmpty()) {
            errors.append("• Vui lòng chọn chức vụ.\n");
        }
        if (txtSalary.getText() == null || txtSalary.getText().trim().isEmpty()) {
            errors.append("• Lương không được để trống.\n");
        } else {
            try {
                BigDecimal salary = new BigDecimal(txtSalary.getText().trim().replace(",", ""));
                if (salary.compareTo(BigDecimal.ZERO) < 0) {
                    errors.append("• Lương phải lớn hơn hoặc bằng 0.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("• Lương phải là số hợp lệ.\n");
            }
        }

        if (errors.length() > 0) {
            lblError.setText(errors.toString());
            return false;
        }
        return true;
    }

    // ==================== UTILITIES ====================

    /**
     * Hiển thị panel chi tiết
     */
    private void showDetailPanel() {
        detailPanel.setVisible(true);
        detailPanel.setManaged(true);
        lblError.setText("");
    }

    /**
     * Xóa trắng form
     */
    private void clearForm() {
        txtEmployeeId.clear();
        txtLastName.clear();
        txtFirstName.clear();
        txtPhone.clear();
        dpDateOfBirth.setValue(null);
        txtAddress.clear();
        cbRole.setValue(null);
        cbRole.setPromptText("Chọn chức vụ");
        txtSalary.clear();
        cbStatus.setValue(null);
        lblError.setText("");
    }

    /**
     * Hiển thị thông báo Alert
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}