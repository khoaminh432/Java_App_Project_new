package my_app.controller.component;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import my_app.bus.InvoiceBus;
import my_app.bus.InvoiceDetailBus;
import my_app.dao.InvoiceDetailDao;
import my_app.dao.ProductDao;
import my_app.model.Customer;
import my_app.model.Employee;
import my_app.model.Invoice;
import my_app.model.InvoiceDetail;
import my_app.model.Product;

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
        try {
            loadData();
        } catch (Exception e) {
            System.err.println("Warning: Database not available during initialization. Error: " + e.getMessage());
            e.printStackTrace();
            // Hiển thị bảng trống thay vì crash
            invoiceTable.setItems(FXCollections.observableArrayList());
        }
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
            // Mở UI chi tiết hóa đơn bằng FXML riêng
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml/admin/component/invoicedetail.fxml")
                );
                Parent root = loader.load();
                InvoiceDetailController controller = loader.getController();
                controller.setInvoice(inv);

                Stage stage = new Stage();
                stage.setTitle("Chi tiết hóa đơn #" + inv.getId());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Refresh bảng hóa đơn sau khi đóng window
                loadData();
                return;
            } catch (Exception fxmlEx) {
                // Nếu FXML bị lỗi (ví dụ thiếu bảng invoice_detail), fallback sang dialog cũ.
                System.err.println("Cannot open invoicedetail.fxml: " + fxmlEx.getMessage());
            }

            // ========= Thông tin hóa đơn =========
            my_app.dao.CustomerDao custDao = new my_app.dao.CustomerDao();
            Customer customer = custDao.findById(inv.getCustomerId());
            String custName = (customer != null) ? customer.getFullName() : "N/A";

            my_app.dao.EmployeeDao empDao = new my_app.dao.EmployeeDao();
            Employee employee = (inv.getEmployeeId() != null) ? empDao.findById(inv.getEmployeeId()) : null;
            String empName = (employee != null)
                    ? (employee.getFirstName() + " " + employee.getLastName()).trim()
                    : "N/A";

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

            Label header = new Label(
                    String.format("Hóa Đơn #%d | %s | Tổng: %s", inv.getId(), statusLabel, formatMoney(inv.getTotalAmount()))
            );
            header.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 6 0 6 0;");

            // ========= Load invoice_detail + product map =========
            InvoiceDetailDao detailDao = new InvoiceDetailDao();
            ProductDao productDao = new ProductDao();

            Map<Integer, String> productNameMap = new HashMap<>();
            try {
                for (Product p : productDao.findAll()) {
                    productNameMap.put(p.getId(), p.getProductName());
                }
            } catch (Exception ignore) {
                // Nếu không load được product thì vẫn hiển thị productId
            }

            final ObservableList<InvoiceDetail> detailItems = FXCollections.observableArrayList();
            try {
                detailItems.setAll(detailDao.findByInvoiceId(inv.getId()));
            } catch (Exception ex) {
                // Nếu DB chưa có bảng invoice_detail thì vẫn mở dialog, nhưng không có dòng chi tiết
                System.err.println("Cannot load invoice_detail: " + ex.getMessage());
                showWarning("Thiếu dữ liệu", "Không thể tải chi tiết hóa đơn (invoice_detail). Vui lòng kiểm tra DB.");
            }

            TableView<InvoiceDetail> tv = new TableView<>();
            tv.setItems(detailItems);
            tv.setPrefHeight(260);

            TableColumn<InvoiceDetail, Integer> colProductId = new TableColumn<>("Mã SP");
            colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));

            TableColumn<InvoiceDetail, String> colProductName = new TableColumn<>("Sản phẩm");
            colProductName.setCellValueFactory(cd ->
                    new javafx.beans.property.SimpleStringProperty(
                            productNameMap.getOrDefault(cd.getValue().getProductId(), "")
                    )
            );

            TableColumn<InvoiceDetail, Integer> colQty = new TableColumn<>("SL");
            colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            TableColumn<InvoiceDetail, String> colUnitPrice = new TableColumn<>("Đơn giá");
            colUnitPrice.setCellValueFactory(cd ->
                    new javafx.beans.property.SimpleStringProperty(
                            formatMoney(cd.getValue().getUnitPrice())
                    )
            );

            TableColumn<InvoiceDetail, String> colLineTotal = new TableColumn<>("Thành tiền");
            colLineTotal.setCellValueFactory(cd ->
                    new javafx.beans.property.SimpleStringProperty(
                            formatMoney(cd.getValue().getLineTotal())
                    )
            );

            tv.getColumns().addAll(colProductId, colProductName, colQty, colUnitPrice, colLineTotal);

            // ========= Helpers: reload + recalc invoice total =========
            Runnable reloadAndRecalc = () -> {
                try {
                    detailItems.setAll(detailDao.findByInvoiceId(inv.getId()));
                    BigDecimal total = detailItems.stream()
                            .map(InvoiceDetail::getLineTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    inv.setTotalAmount(total);
                    invoiceBus.updateInvoice(inv);
                    loadData(); // cập nhật lại bảng hóa đơn
                } catch (Exception ex) {
                    showError("Lỗi", "Không thể cập nhật tổng tiền/chi tiết: " + ex.getMessage());
                }
            };

            // ========= Dialog chính =========
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Chi Tiết Hóa Đơn & InvoiceDetail");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.getDialogPane().setPrefSize(820, 520);

            Button btnAdd = new Button("➕ Thêm sản phẩm");
            Button btnEdit = new Button("✏ Sửa dòng");
            Button btnRemove = new Button("🗑 Xóa dòng");
            Button btnDeleteInvoice = new Button("🗑 Xóa hóa đơn");

            HBox actionBar = new HBox(10, btnAdd, btnEdit, btnRemove, btnDeleteInvoice);
            actionBar.setStyle("-fx-padding: 8 0 0 0;");

            btnAdd.setOnAction(e -> {
                try {
                    // Dữ liệu chọn sản phẩm
                    List<Product> products = productDao.findAll();
                    if (products.isEmpty()) {
                        showWarning("Thiếu dữ liệu", "Không có sản phẩm trong DB để thêm.");
                        return;
                    }

                    Map<String, Integer> displayToProductId = new HashMap<>();
                    Map<Integer, BigDecimal> idToPrice = new HashMap<>();
                    ComboBox<String> cbProduct = new ComboBox<>();
                    for (Product p : products) {
                        if (p.getId() == null) continue;
                        String display = p.getId() + " - " + p.getProductName();
                        displayToProductId.put(display, p.getId());
                        idToPrice.put(p.getId(), p.getUnitPrice());
                        cbProduct.getItems().add(display);
                    }

                    TextField tfQty = new TextField("1");
                    TextField tfUnitPrice = new TextField();

                    cbProduct.setOnAction(ev -> {
                        Integer pid = displayToProductId.get(cbProduct.getValue());
                        if (pid != null && idToPrice.containsKey(pid)) {
                            BigDecimal up = idToPrice.get(pid);
                            if (up != null) tfUnitPrice.setText(up.toPlainString());
                        }
                    });

                    if (!cbProduct.getItems().isEmpty()) {
                        cbProduct.getSelectionModel().selectFirst();
                        Integer pid = displayToProductId.get(cbProduct.getValue());
                        BigDecimal up = idToPrice.get(pid);
                        if (up != null) tfUnitPrice.setText(up.toPlainString());
                    }

                    Dialog<InvoiceDetail> addDialog = new Dialog<>();
                    addDialog.setTitle("Thêm sản phẩm vào hóa đơn #" + inv.getId());
                    ButtonType btnSave = new ButtonType("Thêm", ButtonType.OK.getButtonData());
                    addDialog.getDialogPane().getButtonTypes().addAll(btnSave, ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(12);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(16));

                    grid.add(new Label("Chọn sản phẩm:"), 0, 0);
                    grid.add(cbProduct, 1, 0);
                    grid.add(new Label("Số lượng:"), 0, 1);
                    grid.add(tfQty, 1, 1);
                    grid.add(new Label("Đơn giá:"), 0, 2);
                    grid.add(tfUnitPrice, 1, 2);

                    addDialog.getDialogPane().setContent(grid);

                    addDialog.setResultConverter(btn -> {
                        if (btn != btnSave) return null;
                        try {
                            String display = cbProduct.getValue();
                            Integer productId = displayToProductId.get(display);
                            if (productId == null) throw new RuntimeException("Chưa chọn sản phẩm.");

                            int qty = Integer.parseInt(tfQty.getText().trim());
                            if (qty <= 0) throw new RuntimeException("Số lượng phải > 0.");

                            BigDecimal unitPrice = new BigDecimal(tfUnitPrice.getText().trim());

                            return new InvoiceDetail(inv.getId(), productId, qty, unitPrice);
                        } catch (Exception ex) {
                            showError("Dữ liệu không hợp lệ", ex.getMessage());
                            return null;
                        }
                    });

                    Optional<InvoiceDetail> result = addDialog.showAndWait();
                    result.ifPresent(newDetail -> {
                        try {
                            detailDao.create(newDetail);
                            reloadAndRecalc.run();
                        } catch (Exception ex) {
                            showError("Lỗi", "Không thể thêm sản phẩm: " + ex.getMessage());
                        }
                    });
                } catch (Exception ex) {
                    showError("Lỗi", "Không thể mở dialog thêm: " + ex.getMessage());
                }
            });

            btnEdit.setOnAction(e -> {
                InvoiceDetail selected = tv.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    showWarning("Chưa chọn", "Vui lòng chọn một dòng để sửa.");
                    return;
                }

                try {
                    Dialog<InvoiceDetail> editDialog = new Dialog<>();
                    editDialog.setTitle("Sửa dòng sản phẩm (InvoiceDetail) #" + selected.getId());
                    ButtonType btnSave = new ButtonType("Cập nhật", ButtonType.OK.getButtonData());
                    editDialog.getDialogPane().getButtonTypes().addAll(btnSave, ButtonType.CANCEL);

                    TextField tfQty = new TextField(String.valueOf(selected.getQuantity()));
                    TextField tfUnitPrice = new TextField(
                            selected.getUnitPrice() == null ? "" : selected.getUnitPrice().toPlainString()
                    );

                    GridPane grid = new GridPane();
                    grid.setHgap(12);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(16));

                    grid.add(new Label("Số lượng:"), 0, 0);
                    grid.add(tfQty, 1, 0);
                    grid.add(new Label("Đơn giá:"), 0, 1);
                    grid.add(tfUnitPrice, 1, 1);

                    editDialog.getDialogPane().setContent(grid);

                    editDialog.setResultConverter(btn -> {
                        if (btn != btnSave) return null;
                        try {
                            int qty = Integer.parseInt(tfQty.getText().trim());
                            if (qty <= 0) throw new RuntimeException("Số lượng phải > 0.");
                            BigDecimal unitPrice = new BigDecimal(tfUnitPrice.getText().trim());

                            selected.setQuantity(qty);
                            selected.setUnitPrice(unitPrice);
                            return selected;
                        } catch (Exception ex) {
                            showError("Dữ liệu không hợp lệ", ex.getMessage());
                            return null;
                        }
                    });

                    Optional<InvoiceDetail> result = editDialog.showAndWait();
                    result.ifPresent(updated -> {
                        try {
                            detailDao.update(updated);
                            reloadAndRecalc.run();
                        } catch (Exception ex) {
                            showError("Lỗi", "Không thể cập nhật dòng: " + ex.getMessage());
                        }
                    });
                } catch (Exception ex) {
                    showError("Lỗi", "Không thể mở dialog sửa: " + ex.getMessage());
                }
            });

            btnRemove.setOnAction(e -> {
                InvoiceDetail selected = tv.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    showWarning("Chưa chọn", "Vui lòng chọn một dòng để xóa.");
                    return;
                }
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setTitle("Xác nhận xóa dòng");
                confirm.setHeaderText(null);
                confirm.setContentText("Xóa dòng sản phẩm #" + selected.getId() + " khỏi hóa đơn?");
                confirm.showAndWait().ifPresent(btnType -> {
                    if (btnType == ButtonType.OK) {
                        try {
                            detailDao.delete(selected.getId());
                            reloadAndRecalc.run();
                        } catch (Exception ex) {
                            showError("Lỗi", "Không thể xóa dòng: " + ex.getMessage());
                        }
                    }
                });
            });

            btnDeleteInvoice.setOnAction(e -> {
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setTitle("Xác nhận xóa hóa đơn");
                confirm.setHeaderText(null);
                confirm.setContentText("Xóa hóa đơn #" + inv.getId() + " và tất cả invoice_detail?");
                confirm.showAndWait().ifPresent(btnType -> {
                    if (btnType == ButtonType.OK) {
                        try {
                            detailDao.deleteByInvoiceId(inv.getId());
                            invoiceBus.deleteInvoice(inv.getId());
                            loadData();
                            dialog.close();
                        } catch (Exception ex) {
                            showError("Lỗi", "Không thể xóa hóa đơn: " + ex.getMessage());
                        }
                    }
                });
            });

            VBox content = new VBox(10, header, new Label("Khách: " + custName + " | Nhân viên: " + empName + " | " + orderDetails), tv, actionBar);
            content.setStyle("-fx-padding: 12;");
            dialog.getDialogPane().setContent(content);

            dialog.showAndWait();
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
                        // Xóa invoice_detail trước để tránh lỗi khóa ngoại
                        new InvoiceDetailDao().deleteByInvoiceId(inv.getId());
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

                String amountText = tfAmount.getText().trim();
                if (amountText.isEmpty()) {
                    showError("Du lieu khong hop le", "Tong tien khong duoc rong!");
                    return null;
                }
                BigDecimal amt = new BigDecimal(amountText);

                String employeeText = tfEmpId.getText().trim();
                Integer empId = employeeText.isEmpty()
                    ? null : Integer.parseInt(employeeText);

                Invoice inv = new Invoice();
                inv.setCustomerId(custId);
                inv.setEmployeeId(empId);
                inv.setTotalAmount(amt);
                inv.setStatus(cbStatus.getValue());
                if (dpDate.getValue() != null)
                    inv.setIssuedDate(dpDate.getValue().atStartOfDay());
                return inv;

            } catch (NumberFormatException ex) {
                showError("Du lieu khong hop le", "ID nhan vien va tong tien phai la so!");
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