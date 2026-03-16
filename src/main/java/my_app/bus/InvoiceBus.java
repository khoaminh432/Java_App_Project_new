package my_app.bus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import my_app.dao.CustomerDao;
import my_app.dao.InvoiceDao;
import my_app.model.Customer;
import my_app.model.Invoice;

public class InvoiceBus {
    private InvoiceDao invoiceDao;

    public InvoiceBus() {
        this.invoiceDao = new InvoiceDao();
    }

    // Constructor cho unit testing (dependency injection)
    public InvoiceBus(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    // Lấy hóa đơn theo ID
    public Invoice getInvoiceById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID hóa đơn không hợp lệ");
        }
        return invoiceDao.findById(id);
    }

    // Lấy tất cả hóa đơn
    public List<Invoice> getAllInvoices() {
        return invoiceDao.findAll();
    }

    // Tạo mới hóa đơn
    public boolean createInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Thông tin hóa đơn không được null");
        }

        // Validate dữ liệu
        validateInvoice(invoice);

        // Tự động set issued_date nếu chưa có
        if (invoice.getIssuedDate() == null) {
            invoice.setIssuedDate(LocalDateTime.now());
        }

        int result = invoiceDao.create(invoice);
        return result > 0;
    }

    // Cập nhật hóa đơn
    public boolean updateInvoice(Invoice invoice) {
        if (invoice == null || invoice.getId() == null) {
            throw new IllegalArgumentException("Thông tin hóa đơn không hợp lệ");
        }

        // Kiểm tra hóa đơn có tồn tại không
        Invoice existingInvoice = invoiceDao.findById(invoice.getId());
        if (existingInvoice == null) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại");
        }

        // Validate dữ liệu
        validateInvoice(invoice);

        int result = invoiceDao.update(invoice);
        return result > 0;
    }

    // Xóa hóa đơn
    public boolean deleteInvoice(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID hóa đơn không hợp lệ");
        }

        // Kiểm tra hóa đơn có tồn tại không
        Invoice invoice = invoiceDao.findById(id);
        if (invoice == null) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại");
        }

        int result = invoiceDao.delete(id);
        return result > 0;
    }

    // ========================
    // TRUY VẤN THEO NGHIỆP VỤ
    // ========================

    // Lấy danh sách hóa đơn theo khách hàng
    public List<Invoice> getInvoicesByCustomer(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        return invoiceDao.findByCustomerId(customerId);
    }

    // Lấy danh sách hóa đơn theo khoảng ngày
    public List<Invoice> getInvoicesByDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Khoảng thời gian không hợp lệ");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return invoiceDao.findByDateRange(from, to);
    }

    // Lấy hóa đơn trong tháng hiện tại
    public List<Invoice> getCurrentMonthInvoices() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
                                      .withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.findByDateRange(startOfMonth, endOfMonth);
    }

    // Lấy hóa đơn trong ngày hôm nay
    public List<Invoice> getTodayInvoices() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.findByDateRange(startOfDay, endOfDay);
    }

    // Đánh dấu hóa đơn đã thanh toán
    public boolean markInvoiceAsPaid(Integer invoiceId) {
        if (invoiceId == null || invoiceId <= 0) {
            throw new IllegalArgumentException("ID hóa đơn không hợp lệ");
        }

        Invoice invoice = invoiceDao.findById(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại");
        }

        int result = invoiceDao.markAsPaid(invoiceId);
        return result > 0;
    }

    // Hủy hóa đơn
    public boolean cancelInvoice(Integer invoiceId) {
        if (invoiceId == null || invoiceId <= 0) {
            throw new IllegalArgumentException("ID hóa đơn không hợp lệ");
        }

        Invoice invoice = invoiceDao.findById(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại");
        }

        // Không cho phép hủy hóa đơn đã thanh toán
        if ("PAID".equals(invoice.getStatus())) {
            throw new IllegalStateException("Không thể hủy hóa đơn đã thanh toán");
        }

        int result = invoiceDao.cancel(invoiceId);
        return result > 0;
    }

    // Cập nhật trạng thái hóa đơn
    public boolean updateInvoiceStatus(Integer invoiceId, String status) {
        if (invoiceId == null || invoiceId <= 0) {
            throw new IllegalArgumentException("ID hóa đơn không hợp lệ");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được rỗng");
        }

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + status);
        }

        int result = invoiceDao.updateStatus(invoiceId, status);
        return result > 0;
    }

    // Tính tổng doanh thu trong khoảng thời gian
    public BigDecimal getTotalRevenue(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Khoảng thời gian không hợp lệ");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return invoiceDao.getTotalRevenue(from, to);
    }

    // Tính doanh thu tháng hiện tại
    public BigDecimal getCurrentMonthRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
                                      .withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.getTotalRevenue(startOfMonth, endOfMonth);
    }

    // Tính doanh thu ngày hôm nay
    public BigDecimal getTodayRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);
       return invoiceDao.getTotalRevenue(startOfDay, endOfDay);
    }

    // Tính doanh thu năm hiện tại
    public BigDecimal getCurrentYearRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYear = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfYear = now.withDayOfYear(now.toLocalDate().lengthOfYear())
                                     .withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.getTotalRevenue(startOfYear, endOfYear);
    }

    // ========================
    // PRIVATE HELPERS
    // ========================

    // Validate thông tin hóa đơn
    private void validateInvoice(Invoice invoice) {
        if (invoice.getCustomerId() == null || invoice.getCustomerId() <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        if (invoice.getTotalAmount() == null || invoice.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tổng tiền phải lớn hơn 0");
        }
    }

    // Kiểm tra trạng thái hợp lệ
    private boolean isValidStatus(String status) {
        return "NEW".equals(status) ||
               "PENDING".equals(status) ||
               "PAID".equals(status) ||
               "CANCELED".equals(status);
    }

    // Kiểm tra sự tồn tại của hóa đơn
    public boolean invoiceExists(Integer id) {
        if (id == null || id <= 0) {
            return false;
        }
        return invoiceDao.findById(id) != null;
    }

    // Lấy hoặc tạo mới khách hàng theo tên
    public Integer getOrCreateCustomerId(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được rỗng");
        }

        CustomerBus customerBus = new CustomerBus();
        Customer existing = new CustomerDao().findByName(customerName.trim());

        if (existing != null) {
            return existing.getId();
        }

        // Tạo khách hàng mới
        Customer newCustomer = new Customer();
        newCustomer.setFullName(customerName.trim());
        newCustomer.setStatus("ACTIVE");
        int customerId = customerBus.create(newCustomer);

        if (customerId <= 0) {
            throw new RuntimeException("Không thể tạo khách hàng mới");
        }

        return customerId;
    }
}
