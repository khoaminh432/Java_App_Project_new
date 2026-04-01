package my_app.controller.component;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import my_app.bus.InvoiceBus;
import my_app.model.Customer;
import my_app.model.Employee;
import my_app.model.Invoice;

public class InvoiceController implements Initializable {

    // ── FXML ──────────────────────────────────────────────────────────────────

    @FXML private TextField        tfSearchInvoice;
    @FXML private ComboBox<String> cbStatusFilter;
    @FXML private Button           btnAdd;

    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;
    @FXML private Button     btnFilterDate;
    @FXML private Button     btnToday;
    @FXML private Button     btnThisMonth;
    @FXML private Button     btnReset;

    @FXML private Label lblTotalInvoices;
    @FXML private Label lblTodayRevenue;
    @FXML private Label lblMonthRevenue;
    @FXML private Label lblYearRevenue;

    @FXML private TableView<Invoice>                  invoiceTable;
    @FXML private TableColumn<Invoice, Integer>       colId;
    @FXML private TableColumn<Invoice, Integer>       colCustomerId;
    @FXML private TableColumn<Invoice, Integer>       colEmployeeId;
    @FXML private TableColumn<Invoice, LocalDateTime> colIssuedDate;
    @FXML private TableColumn<Invoice, BigDecimal>    colTotalAmount;
    @FXML private TableColumn<Invoice, String>        colStatus;
    @FXML private TableColumn<Invoice, Void>          colActions;

    // ── Business ──────────────────────────────────────────────────────────────
    private final InvoiceBus invoiceBus = new InvoiceBus();
    private ObservableList<Invoice> masterList = FXCollections.observableArrayList();

    // ── Init ──────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupStatusFilter();
        setupTableColumns();
        setupSearch();
        loadData();
    }

    // ── Setup ─────────────────────────────────────────────────────────────────

    private void setupStatusFilter() {
        cbStatusFilter.setItems(FXCollections.observableArrayList(
            "Tất cả", "NEW", "PENDING", "PAID", "CANCELED"
        ));
        cbStatusFilter.setValue("Tất cả");
        cbStatusFilter.setOnAction(e -> applyFilter());
    }

    private void setupSearch() {
        tfSearchInvoice.textProperty().addListener((obs, o, n) -> applyFilter());
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colIssuedDate.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Format cột ngày
        colIssuedDate.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); return; }
                setText(String.format("%02d/%02d/%d",
                    item.getDayOfMonth(), item.getMonthValue(), item.getYear()));
            }
        });

        // Format cột tiền
        colTotalAmount.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); return; }
                setText(formatMoney(item));
            }
        });

        // Màu trạng thái
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                String label = switch (item) {
                    case "PAID"     -> "Đã TT";
                    case "PENDING"  -> "Chờ TT";
                    case "CANCELED" -> "Đã Hủy";
                    default         -> "Mới";
                };
                setText(label);
                String color = switch (item) {
                    case "PAID"     -> "-fx-text-fill: #2e7d32;";
                    case "PENDING"  -> "-fx-text-fill: #e65100;";
                    case "CANCELED" -> "-fx-text-fill: #c62828;";
                    default         -> "-fx-text-fill: #1565c0;";
                };
                setStyle(color + " -fx-font-weight: bold;");
            }
        });

        // Cột Hành Động
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnXem    = makeBtn("👁 Xem",  "#9e9e9e");
            private final Button btnSua    = makeBtn("✏ Sửa",  "#29b6f6");
            private final Button btnXoa    = makeBtn("🗑 Xóa",  "#e91e63");
            private final Button btnPay    = makeBtn("✓ TT",   "#43a047");
            private final Button btnCancel = makeBtn("✕ Hủy",  "#ef5350");
            private final HBox   box       = new HBox(4, btnXem, btnSua, btnXoa, btnPay, btnCancel);

            {
                box.setAlignment(Pos.CENTER);
                btnXem.setOnAction(e    -> handleView(getCurrentInvoice()));
                btnSua.setOnAction(e    -> handleEdit(getCurrentInvoice()));
                btnXoa.setOnAction(e    -> handleDelete(getCurrentInvoice()));
                btnPay.setOnAction(e    -> handleMarkPaid(getCurrentInvoice()));
                btnCancel.setOnAction(e -> handleCancel(getCurrentInvoice()));
            }

            private Invoice getCurrentInvoice() {
                return getTableView().getItems().get(getIndex());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Invoice inv = getCurrentInvoice();
                boolean canAct = !"PAID".equals(inv.getStatus()) && !"CANCELED".equals(inv.getStatus());
                btnPay.setVisible(canAct);
                btnCancel.setVisible(canAct);
                setGraphic(box);
            }
        });
    }

    private Button makeBtn(String text, String bg) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + bg + "; -fx-text-fill: white;" +
            "-fx-font-size: 10.5px; -fx-font-weight: bold;" +
            "-fx-background-radius: 6px; -fx-padding: 3 7 3 7; -fx-cursor: hand;");
        return btn;
    }

    // ── Load & Filter ─────────────────────────────────────────────────────────

    private void loadData() {
        masterList = FXCollections.observableArrayList(invoiceBus.getAllInvoices());
        invoiceTable.setItems(masterList);
        updateStats();
    }

    private void applyFilter() {
        String keyword = tfSearchInvoice.getText().trim().toLowerCase();
        String status  = cbStatusFilter.getValue();
        ObservableList<Invoice> filtered = masterList.filtered(inv -> {
            boolean matchSearch = keyword.isEmpty()
                || String.valueOf(inv.getCustomerId()).contains(keyword)
                || String.valueOf(inv.getId()).contains(keyword);
            boolean matchStatus = "Tất cả".equals(status) || status == null
                || status.equals(inv.getStatus());
            return matchSearch && matchStatus;
        });
        invoiceTable.setItems(filtered);
    }

    // ── Handlers FXML ────────────────────────────────────────────────────────

    @FXML
    private void handleAdd() {
        try {
            // Tạo dialog nhập liệu
            Dialog<Invoice> dialog = buildInvoiceDialog(null);
            Optional<Invoice> result = dialog.showAndWait();
            result.ifPresent(inv -> {
                try {
                    boolean ok = invoiceBus.createInvoice(inv);
                    if (ok) {
                        showInfo("Thành công", "Đã tạo hóa đơn mới!");
                        loadData();
                    } else {
                        showError("Lỗi", "Không thể tạo hóa đơn!");
                    }
                } catch (Exception ex) {
                    showError("Lỗi tạo hóa đơn", ex.getMessage());
                }
            });
        } catch (Exception e) {
            showError("Lỗi", "Lỗi khi tạo hóa đơn: " + e.getMessage());
        }
    }

    @FXML
    private void handleFilterByDate() {
        LocalDate from = dpFrom.getValue();
        LocalDate to   = dpTo.getValue();
        if (from == null || to == null) {
            showWarning("Thiếu thông tin", "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!");
            return;
        }
        if (from.isAfter(to)) {
            showWarning("Ngày không hợp lệ", "Ngày bắt đầu phải trước ngày kết thúc!");
            return;
        }
        List<Invoice> list = invoiceBus.getInvoicesByDateRange(
            from.atStartOfDay(), to.atTime(23, 59, 59));
        invoiceTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    private void handleFilterToday() {
        List<Invoice> todayInvoices = invoiceBus.getTodayInvoices();
        invoiceTable.setItems(FXCollections.observableArrayList(todayInvoices));
        updateStats();
    }

    @FXML
    private void handleFilterThisMonth() {
        invoiceTable.setItems(
            FXCollections.observableArrayList(invoiceBus.getCurrentMonthInvoices()));
    }

    @FXML
    private void handleReset() {
        tfSearchInvoice.clear();
        cbStatusFilter.setValue("Tất cả");
        dpFrom.setValue(null);
        dpTo.setValue(null);
        loadData();
    }

    // ── Handlers nút trong bảng ───────────────────────────────────────────────

    // 👁 XEM CHI TIẾT
    private void handleView(Invoice inv) {
        try {
            // Lấy thông tin khách hàng
            my_app.dao.CustomerDao custDao = new my_app.dao.CustomerDao();
            Customer customer = custDao.findById(inv.getCustomerId());
            String custName = (customer != null) ? customer.getFullName() : "N/A";

            // Lấy thông tin nhân viên
            my_app.dao.EmployeeDao empDao = new my_app.dao.EmployeeDao();
            Employee employee = (inv.getEmployeeId() != null) ? empDao.findById(inv.getEmployeeId()) : null;
            String empName = (employee != null)
                    ? (employee.getFirstName() + " " + employee.getLastName()).trim()
                    : "N/A";

            // Lấy chi tiết đơn hàng
            String orderDetails = "—";
            if (inv.getOrderId() != null) {
                my_app.dao.OrderDao orderDao = new my_app.dao.OrderDao();
                Object order = orderDao.findById(inv.getOrderId());
                if (order != null) {
                    orderDetails = "Đơn hàng #" + inv.getOrderId();
                }
            }

            String statusLabel = switch (inv.getStatus()) {
                case "PAID" -> "✓ Đã Thanh Toán";
                case "PENDING" -> "⏳ Chờ Thanh Toán";
                case "CANCELED" -> "✕ Đã Hủy";
                default -> "◯ Mới";
            };

            String contentText = String.format(
                    "━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "Hóa Đơn #%d\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "👤 Khách Hàng: %s\n" +
                    "👨‍💼 Nhân Viên: %s\n" +
                    "📦 Đơn Hàng: %s\n" +
                    "💰 Tổng Tiền: %s\n" +
                    "📅 Ngày Xuất: %s\n" +
                    "🕐 Giờ Xuất: %s\n" +
                    "📊 Trạng Thái: %s\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━",
                    inv.getId(),
                    custName,
                    empName,
                    orderDetails,
                    formatMoney(inv.getTotalAmount()),
                    (inv.getIssuedDate() != null
                            ? String.format("%02d/%02d/%d", inv.getIssuedDate().getDayOfMonth(),
                            inv.getIssuedDate().getMonthValue(), inv.getIssuedDate().getYear())
                            : "—"),
                    (inv.getIssuedDate() != null
                            ? String.format("%02d:%02d:%02d", inv.getIssuedDate().getHour(),
                            inv.getIssuedDate().getMinute(), inv.getIssuedDate().getSecond())
                            : "—"),
                    statusLabel
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chi Tiết Hóa Đơn");
            alert.setHeaderText(null);
            alert.setContentText(contentText);
            alert.getDialogPane().setPrefWidth(400);
            alert.showAndWait();
        } catch (Exception e) {
            showError("Lỗi", "Không thể lấy chi tiết hóa đơn: " + e.getMessage());
        }
    }

    // ✏ SỬA
    private void handleEdit(Invoice inv) {
        try {
            Dialog<Invoice> dialog = buildInvoiceDialog(inv);
            Optional<Invoice> result = dialog.showAndWait();
            result.ifPresent(updated -> {
                try {
                    updated.setId(inv.getId());
                    boolean ok = invoiceBus.updateInvoice(updated);
                    if (ok) {
                        showInfo("Thành công", "Đã cập nhật hóa đơn #" + inv.getId());
                        loadData();
                    } else {
                        showError("Lỗi", "Không thể cập nhật hóa đơn!");
                    }
                } catch (Exception ex) {
                    showError("Lỗi cập nhật hóa đơn", ex.getMessage());
                }
            });
        } catch (Exception e) {
            showError("Lỗi", "Lỗi khi sửa hóa đơn: " + e.getMessage());
        }
    }

    // 🗑 XÓA
    private void handleDelete(Invoice inv) {
        try {
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setTitle("Xác nhận xóa");
            confirm.setHeaderText("Xóa hóa đơn #" + inv.getId() + "?");
            confirm.setContentText("Hành động này không thể hoàn tác!");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    try {
                        boolean ok = invoiceBus.deleteInvoice(inv.getId());
                        if (ok) {
                            showInfo("Thành công", "Đã xóa hóa đơn #" + inv.getId());
                            loadData();
                        } else {
                            showError("Lỗi", "Không thể xóa hóa đơn!");
                        }
                    } catch (Exception ex) {
                        showError("Lỗi xóa hóa đơn", ex.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            showError("Lỗi", "Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }

    // ✓ THANH TOÁN
    private void handleMarkPaid(Invoice inv) {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận thanh toán");
        confirm.setHeaderText("Đánh dấu đã thanh toán hóa đơn #" + inv.getId() + "?");
        confirm.setContentText("Tổng tiền: " + formatMoney(inv.getTotalAmount()));
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    boolean ok = invoiceBus.markInvoiceAsPaid(inv.getId());
                    if (ok) {
                        showInfo("Thành công", "Hóa đơn #" + inv.getId() + " đã được thanh toán!");
                        loadData();
                        updateStats();
                    } else {
                        showError("Lỗi", "Không thể cập nhật trạng thái!");
                    }
                } catch (Exception ex) {
                    showError("Lỗi thanh toán", ex.getMessage());
                }
            }
        });
    }

    // ✕ HỦY
    private void handleCancel(Invoice inv) {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận hủy");
        confirm.setHeaderText("Hủy hóa đơn #" + inv.getId() + "?");
        confirm.setContentText("Hóa đơn sau khi hủy không thể khôi phục!");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    boolean ok = invoiceBus.cancelInvoice(inv.getId());
                    if (ok) {
                        showInfo("Thành công", "Đã hủy hóa đơn #" + inv.getId());
                        loadData();
                    } else {
                        showError("Lỗi", "Không thể hủy hóa đơn!");
                    }
                } catch (IllegalStateException ex) {
                    showError("Không thể hủy", ex.getMessage());
                }
            }
        });
    }

    // ── Dialog Thêm / Sửa ────────────────────────────────────────────────────

    private Dialog<Invoice> buildInvoiceDialog(Invoice existing) {
        boolean isEdit = existing != null;

        Dialog<Invoice> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Sửa Hóa Đơn #" + existing.getId() : "Tạo Hóa Đơn Mới");
        dialog.setHeaderText(isEdit ? "Cập nhật thông tin hóa đơn" : "Nhập thông tin hóa đơn mới");

        ButtonType btnSave = new ButtonType(isEdit ? "Cập nhật" : "Tạo mới");
        dialog.getDialogPane().getButtonTypes().addAll(btnSave, ButtonType.CANCEL);

        // Form fields
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tfCustName = new TextField();
        TextField tfEmpId   = new TextField();
        TextField tfAmount  = new TextField();
        ComboBox<String> cbStatus = new ComboBox<>(
            FXCollections.observableArrayList("NEW", "PENDING", "PAID", "CANCELED"));
        DatePicker dpDate = new DatePicker();

        tfCustName.setPromptText("VD: Nguyễn Văn A");
        tfEmpId.setPromptText("VD: 1 (tùy chọn)");
        tfAmount.setPromptText("VD: 1500000");
        cbStatus.setValue("NEW");
        dpDate.setValue(LocalDate.now());

        // Nếu sửa thì điền sẵn dữ liệu cũ
        if (isEdit) {
            Customer cust = new my_app.dao.CustomerDao().findById(existing.getCustomerId());
            if (cust != null) {
                tfCustName.setText(cust.getFullName());
            }
            if (existing.getEmployeeId() != null)
                tfEmpId.setText(String.valueOf(existing.getEmployeeId()));
            if (existing.getTotalAmount() != null)
                tfAmount.setText(existing.getTotalAmount().toPlainString());
            cbStatus.setValue(existing.getStatus());
            if (existing.getIssuedDate() != null)
                dpDate.setValue(existing.getIssuedDate().toLocalDate());
        }

        grid.add(new Label("Tên Khách hàng *:"), 0, 0); grid.add(tfCustName,  1, 0);
        grid.add(new Label("ID Nhân viên:"),    0, 1); grid.add(tfEmpId,   1, 1);
        grid.add(new Label("Tổng tiền (₫) *:"), 0, 2); grid.add(tfAmount,  1, 2);
        grid.add(new Label("Trạng thái:"),      0, 3); grid.add(cbStatus,  1, 3);
        grid.add(new Label("Ngày xuất:"),       0, 4); grid.add(dpDate,    1, 4);

        dialog.getDialogPane().setContent(grid);

        // Validate & trả về Invoice
        dialog.setResultConverter(btn -> {
            if (btn != btnSave) return null;
            try {
                String custName = tfCustName.getText().trim();
                if (custName.isEmpty()) {
                    showError("Dữ liệu không hợp lệ", "Tên khách hàng không được rỗng!");
                    return null;
                }

                int custId = invoiceBus.getOrCreateCustomerId(custName);
                BigDecimal amt = new BigDecimal(tfAmount.getText().trim());
                Integer empId = tfEmpId.getText().trim().isEmpty()
                    ? null : Integer.parseInt(tfEmpId.getText().trim());

                Invoice inv = new Invoice();
                inv.setCustomerId(custId);
                inv.setEmployeeId(empId);
                inv.setTotalAmount(amt);
                inv.setStatus(cbStatus.getValue());
                if (dpDate.getValue() != null)
                    inv.setIssuedDate(dpDate.getValue().atStartOfDay());
                return inv;

            } catch (NumberFormatException ex) {
                showError("Dữ liệu không hợp lệ", "Tổng tiền phải là số!");
                return null;
            } catch (Exception ex) {
                showError("Lỗi", ex.getMessage());
                return null;
            }
        });

        return dialog;
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    private void updateStats() {
        lblTotalInvoices.setText(String.valueOf(masterList.size()));
        lblTodayRevenue.setText(formatMoney(invoiceBus.getTodayRevenue()));
        lblMonthRevenue.setText(formatMoney(invoiceBus.getCurrentMonthRevenue()));
        lblYearRevenue.setText(formatMoney(invoiceBus.getCurrentYearRevenue()));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0 ₫";
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format(amount) + " ₫";
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    private void showError(String title, String msg) {
        Alert a = new Alert(AlertType.ERROR);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    private void showWarning(String title, String msg) {
        Alert a = new Alert(AlertType.WARNING);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    public void refreshTable() { loadData(); }
}