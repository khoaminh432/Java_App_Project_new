package my_app.controller.component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.beans.property.SimpleStringProperty;

import my_app.bus.InvoiceBus;
import my_app.dao.InvoiceDetailDao;
import my_app.dao.ProductDao;
import my_app.model.Invoice;
import my_app.model.InvoiceDetail;
import my_app.model.Product;

public class InvoiceDetailController {

    @FXML private Label lblInvoiceHeader;
    @FXML private Label lblInvoiceStatus;
    @FXML private Label lblInvoiceTotal;

    @FXML private TableView<InvoiceDetail> detailTable;
    @FXML private TableColumn<InvoiceDetail, Integer> colProductId;
    @FXML private TableColumn<InvoiceDetail, String> colProductName;
    @FXML private TableColumn<InvoiceDetail, Integer> colQuantity;
    @FXML private TableColumn<InvoiceDetail, String> colUnitPrice;
    @FXML private TableColumn<InvoiceDetail, String> colLineTotal;

    @FXML private Button btnAddLine;
    @FXML private Button btnEditLine;
    @FXML private Button btnDeleteLine;
    @FXML private Button btnDeleteInvoice;
    @FXML private Button btnClose;

    private Invoice invoice;

    private final InvoiceBus invoiceBus = new InvoiceBus();
    private final InvoiceDetailDao invoiceDetailDao = new InvoiceDetailDao();
    private final ProductDao productDao = new ProductDao();

    private final Map<Integer, String> productNameMap = new HashMap<>();
    private final ObservableList<InvoiceDetail> detailItems = FXCollections.observableArrayList();

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
        refreshView();
    }

    @FXML
    private void initialize() {
        // Không dùng nếu setInvoice chưa được gọi.
        setupTableColumns();
    }

    private void setupTableColumns() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colProductName.setCellValueFactory(cd -> {
            InvoiceDetail d = cd.getValue();
            return new SimpleStringProperty(productNameMap.getOrDefault(d.getProductId(), ""));
        });

        colUnitPrice.setCellValueFactory(cd -> {
            InvoiceDetail d = cd.getValue();
            return new SimpleStringProperty(formatMoney(d.getUnitPrice()));
        });

        colLineTotal.setCellValueFactory(cd -> {
            InvoiceDetail d = cd.getValue();
            return new SimpleStringProperty(formatMoney(d.getLineTotal()));
        });

        detailTable.setItems(detailItems);
    }

    private void refreshView() {
        if (invoice == null) return;

        lblInvoiceHeader.setText("Hóa đơn #" + invoice.getId());
        lblInvoiceStatus.setText("Trạng thái: " + invoice.getStatus());
        lblInvoiceTotal.setText("Tổng: " + formatMoney(invoice.getTotalAmount()));

        loadProductNameMap();
        loadInvoiceDetails();
    }

    private void loadProductNameMap() {
        productNameMap.clear();
        try {
            for (Product p : productDao.findAll()) {
                if (p.getId() != null) {
                    productNameMap.put(p.getId(), p.getProductName());
                }
            }
        } catch (Exception ignore) {
            // Nếu fail thì vẫn hiển thị productId
        }
    }

    private void loadInvoiceDetails() {
        detailItems.clear();
        try {
            List<InvoiceDetail> details = invoiceDetailDao.findByInvoiceId(invoice.getId());
            detailItems.setAll(details);
        } catch (Exception ex) {
            // Chưa tồn tại bảng invoice_detail -> hiển thị trống và cảnh báo
            System.err.println("Cannot load invoice_detail: " + ex.getMessage());
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Thiếu dữ liệu");
            a.setHeaderText(null);
            a.setContentText("Không thể tải invoice_detail. Vui lòng kiểm tra DB.");
            a.showAndWait();
        }
        recalcAndUpdateInvoice();
    }

    private void recalcAndUpdateInvoice() {
        try {
            BigDecimal total = detailItems.stream()
                    .map(InvoiceDetail::getLineTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            invoice.setTotalAmount(total);
            invoiceBus.updateInvoice(invoice);
            lblInvoiceTotal.setText("Tổng: " + formatMoney(invoice.getTotalAmount()));
        } catch (Exception ignore) {
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAddLine() {
        if (invoice == null) return;

        try {
            List<Product> products = productDao.findAll();
            if (products.isEmpty()) {
                showWarning("Thiếu dữ liệu", "Không có sản phẩm để thêm.");
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

            cbProduct.setOnAction(e -> {
                Integer pid = displayToProductId.get(cbProduct.getValue());
                if (pid != null && idToPrice.containsKey(pid)) {
                    BigDecimal up = idToPrice.get(pid);
                    if (up != null) tfUnitPrice.setText(up.toPlainString());
                }
            });

            cbProduct.getSelectionModel().selectFirst();
            Integer pid = displayToProductId.get(cbProduct.getValue());
            BigDecimal up = idToPrice.get(pid);
            if (up != null) tfUnitPrice.setText(up.toPlainString());

            Dialog<InvoiceDetail> dialog = new Dialog<>();
            dialog.setTitle("Thêm sản phẩm vào hóa đơn #" + invoice.getId());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(12);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(16));

            grid.add(new Label("Sản phẩm:"), 0, 0);
            grid.add(cbProduct, 1, 0);
            grid.add(new Label("Số lượng:"), 0, 1);
            grid.add(tfQty, 1, 1);
            grid.add(new Label("Đơn giá:"), 0, 2);
            grid.add(tfUnitPrice, 1, 2);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn != ButtonType.OK) return null;
                try {
                    int qty = Integer.parseInt(tfQty.getText().trim());
                    if (qty <= 0) throw new RuntimeException("Số lượng phải > 0");

                    Integer productId = displayToProductId.get(cbProduct.getValue());
                    if (productId == null) throw new RuntimeException("Chưa chọn sản phẩm");

                    BigDecimal unitPrice = new BigDecimal(tfUnitPrice.getText().trim());

                    return new InvoiceDetail(invoice.getId(), productId, qty, unitPrice);
                } catch (Exception ex) {
                    showError("Dữ liệu không hợp lệ", ex.getMessage());
                    return null;
                }
            });

            Optional<InvoiceDetail> result = dialog.showAndWait();
            result.ifPresent(detail -> {
                try {
                    invoiceDetailDao.create(detail);
                    loadInvoiceDetails();
                } catch (Exception ex) {
                    showError("Lỗi", "Không thể thêm dòng: " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            showError("Lỗi", ex.getMessage());
        }
    }

    @FXML
    private void handleEditLine() {
        if (invoice == null) return;

        InvoiceDetail selected = detailTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Chưa chọn", "Vui lòng chọn một dòng để sửa.");
            return;
        }

        try {
            Dialog<InvoiceDetail> dialog = new Dialog<>();
            dialog.setTitle("Sửa InvoiceDetail #" + selected.getId());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField tfQty = new TextField(String.valueOf(selected.getQuantity()));
            TextField tfUnitPrice = new TextField(
                    selected.getUnitPrice() == null ? "" : selected.getUnitPrice().toPlainString()
            );

            GridPane grid = new GridPane();
            grid.setHgap(12);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(16));

            grid.add(new Label("Số lượng:"), 0, 0);
            grid.add(tfQty, 1, 0);
            grid.add(new Label("Đơn giá:"), 0, 1);
            grid.add(tfUnitPrice, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn != ButtonType.OK) return null;
                try {
                    int qty = Integer.parseInt(tfQty.getText().trim());
                    if (qty <= 0) throw new RuntimeException("Số lượng phải > 0");

                    BigDecimal unitPrice = new BigDecimal(tfUnitPrice.getText().trim());
                    selected.setQuantity(qty);
                    selected.setUnitPrice(unitPrice);
                    return selected;
                } catch (Exception ex) {
                    showError("Dữ liệu không hợp lệ", ex.getMessage());
                    return null;
                }
            });

            Optional<InvoiceDetail> result = dialog.showAndWait();
            result.ifPresent(updated -> {
                try {
                    invoiceDetailDao.update(updated);
                    loadInvoiceDetails();
                } catch (Exception ex) {
                    showError("Lỗi", "Không thể cập nhật dòng: " + ex.getMessage());
                }
            });
        } catch (Exception ex) {
            showError("Lỗi", ex.getMessage());
        }
    }

    @FXML
    private void handleDeleteLine() {
        if (invoice == null) return;

        InvoiceDetail selected = detailTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Chưa chọn", "Vui lòng chọn một dòng để xóa.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa dòng");
        confirm.setHeaderText(null);
        confirm.setContentText("Xóa InvoiceDetail #" + selected.getId() + " khỏi hóa đơn?");

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    invoiceDetailDao.delete(selected.getId());
                    loadInvoiceDetails();
                } catch (Exception ex) {
                    showError("Lỗi", "Không thể xóa dòng: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleDeleteInvoice() {
        if (invoice == null) return;
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Xóa hóa đơn #" + invoice.getId() + " và toàn bộ invoice_detail?");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    invoiceDetailDao.deleteByInvoiceId(invoice.getId());
                    invoiceBus.deleteInvoice(invoice.getId());
                    Stage stage = (Stage) btnClose.getScene().getWindow();
                    stage.close();
                } catch (Exception ex) {
                    showError("Lỗi", ex.getMessage());
                }
            }
        });
    }

    private void showWarning(String title, String msg) {
        Alert a = new Alert(AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showError(String title, String msg) {
        Alert a = new Alert(AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0";
        return amount.stripTrailingZeros().toPlainString() + " đ";
    }
}

