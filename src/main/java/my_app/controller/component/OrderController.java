package my_app.controller.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import my_app.model.CartItem;
import my_app.model.Product;
import my_app.model.Invoice;
import my_app.model.InvoiceDetail;
import my_app.model.Order;
import my_app.model.OrderDetail;
import my_app.bus.ProductBus;
import my_app.bus.InvoiceBus;
import my_app.bus.InvoiceDetailBus;
import my_app.bus.OrderBus;
import my_app.bus.OrderDetailBus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> colProductId;
    @FXML
    private TableColumn<Product, String> colProductName;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colStock;

    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, String> colCartProductName;
    @FXML
    private TableColumn<CartItem, Integer> colCartQuantity;
    @FXML
    private TableColumn<CartItem, Double> colCartPrice;
    @FXML
    private TableColumn<CartItem, Double> colCartTotal;

    @FXML
    private Label lblTotalPrice;
    @FXML
    private Label lblPaymentTotal;
    @FXML
    private Button btnChangePaymentStatus;
    @FXML
    private Button btnAddToCart;
    @FXML
    private Button btnRemoveFromCart;
    @FXML
    private Button btnClearCart;
    @FXML
    private Button btnCheckout;

    private ProductBus productBus = new ProductBus();
    private InvoiceBus invoiceBus = new InvoiceBus();
    private InvoiceDetailBus invoiceDetailBus = new InvoiceDetailBus();
    private OrderBus orderBus = new OrderBus();
    private OrderDetailBus orderDetailBus = new OrderDetailBus();

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private boolean isPaid = false;

    @FXML
    public void initialize() {
        try {
            // Configure table columns for products
            colProductId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
            colProductName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("productName"));
            colPrice.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("unitPrice"));
            colStock.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantity"));

            colCartProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
            colCartQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
            colCartPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
            colCartTotal.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

            // Load products
            loadProducts();

            // Set cart table items
            cartTable.setItems(cartItems);

            // Initialize payment status button
            updatePaymentStatusButton();

        } catch (Exception e) {
            System.err.println("Error initializing OrderController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            // Create a new ProductBus instance to fetch from database
            ProductBus tempBus = new ProductBus();
            List<Product> products = tempBus.getProducts();
            if (products.isEmpty()) {
                // If empty, try using dao directly
                my_app.dao.ProductDao productDao = new my_app.dao.ProductDao();
                products = productDao.findAll();
            }
            ObservableList<Product> observableProducts = FXCollections.observableArrayList(products);
            productTable.setItems(observableProducts);
        } catch (Exception e) {
            System.err.println("Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void addToCart() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Lỗi", "Vui lòng chọn sản phẩm trước!");
            return;
        }

        // Check if product is already in cart
        for (CartItem item : cartItems) {
            if (item.getProductId() == selectedProduct.getId()) {
                // If already exists, increase quantity
                int newQuantity = item.getQuantity() + 1;
                if (newQuantity <= selectedProduct.getQuantity()) {
                    item.setQuantity(newQuantity);
                    updateTotalPrice();
                    return;
                } else {
                    showAlert("Thông báo", "Không đủ hàng trong kho!");
                    return;
                }
            }
        }

        // Add new item to cart
        CartItem newItem = new CartItem(
            selectedProduct.getId(),
            selectedProduct.getProductName(),
            1,
            selectedProduct.getUnitPrice().doubleValue()
        );
        cartItems.add(newItem);
        updateTotalPrice();
    }

    @FXML
    private void removeFromCart() {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Lỗi", "Vui lòng chọn sản phẩm trong giỏ để xóa!");
            return;
        }
        cartItems.remove(selectedItem);
        updateTotalPrice();
    }

    @FXML
    private void clearCart() {
        if (!cartItems.isEmpty()) {
            cartItems.clear();
            updateTotalPrice();
        }
    }

    @FXML
    private void changePaymentStatus() {
        isPaid = !isPaid;
        updatePaymentStatusButton();
    }

    private void updatePaymentStatusButton() {
        if (isPaid) {
            btnChangePaymentStatus.setText("Đã thanh toán");
            btnChangePaymentStatus.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            btnChangePaymentStatus.setText("Chưa thanh toán");
            btnChangePaymentStatus.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }

    private void updateTotalPrice() {
        double total = cartItems.stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
        lblTotalPrice.setText(String.format("%.0f đ", total));
        lblPaymentTotal.setText(String.format("%.0f đ", total));
    }

    @FXML
    private void checkout() {
        if (cartItems.isEmpty()) {
            showAlert("Lỗi", "Giỏ hàng trống! Vui lòng thêm sản phẩm.");
            return;
        }

        try {
            double totalPrice = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

            // Create Invoice
            Invoice invoice = new Invoice();
            invoice.setIssuedDate(LocalDateTime.now());
            invoice.setTotalAmount(java.math.BigDecimal.valueOf(totalPrice));
            // Tạm thời gán mặc định: khách lẻ (id=1), thanh toán tiền mặt (id=1)
            // và dùng trạng thái chuẩn cho InvoiceBus: PAID / PENDING
            invoice.setCustomerId(1);
            invoice.setPaymentMethodId(1);
            invoice.setStatus(isPaid ? "PAID" : "PENDING");

            Integer invoiceId = invoiceBus.createInvoiceAndReturnId(invoice);
            if (invoiceId == null || invoiceId <= 0) {
                showAlert("Lỗi", "Không thể tạo hóa đơn!");
                return;
            }

            // Create order detail for each cart item (chỉ lưu chi tiết tạm thời, chưa gắn order_id)
            for (CartItem cartItem : cartItems) {
                OrderDetail detail = new OrderDetail();
                detail.setProductId(cartItem.getProductId());
                detail.setQuantity(cartItem.getQuantity());
                detail.setUnitPrice(java.math.BigDecimal.valueOf(cartItem.getPrice()));

                orderDetailBus.create(detail);

                // Ghi chi tiết hóa đơn theo từng sản phẩm trong giỏ hàng
                InvoiceDetail invoiceDetail = new InvoiceDetail(
                    invoiceId,
                    cartItem.getProductId(),
                    cartItem.getQuantity(),
                    java.math.BigDecimal.valueOf(cartItem.getPrice())
                );
                invoiceDetailBus.create(invoiceDetail);
            }

            // Clear cart and show success message
            cartItems.clear();
            updateTotalPrice();
            isPaid = false;
            updatePaymentStatusButton();

            showAlert("Thành công", "Đơn hàng đã được tạo thành công!\n" +
                "Tổng tiền: " + String.format("%.0f", totalPrice) + " đ\n" +
                "Trạng thái: " + (isPaid ? "Đã thanh toán" : "Chưa thanh toán"));

        } catch (Exception e) {
            System.err.println("Error during checkout: " + e.getMessage());
            e.printStackTrace();
            showAlert("Lỗi", "Có lỗi xảy ra khi tạo đơn hàng: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
