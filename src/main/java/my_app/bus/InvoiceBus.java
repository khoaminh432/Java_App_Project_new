package my_app.bus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import my_app.dao.CustomerDao;
import my_app.dao.EmployeeDao;
import my_app.dao.InvoiceDao;
import my_app.model.Customer;
import my_app.model.Invoice;

public class InvoiceBus {
    private final InvoiceDao invoiceDao;

    public InvoiceBus() {
        this(new InvoiceDao());
    }

    // Constructor for testing / dependency injection.
    public InvoiceBus(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public Invoice getInvoiceById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID hoa don khong hop le");
        }
        return invoiceDao.findById(id);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceDao.findAll();
    }

    public boolean createInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Thong tin hoa don khong duoc null");
        }

        validateInvoice(invoice);

        if (invoice.getIssuedDate() == null) {
            invoice.setIssuedDate(LocalDateTime.now());
        }

        return invoiceDao.create(invoice) > 0;
    }

    /**
     * Tạo hóa đơn mới và trả về id vừa được insert.
     * Dùng khi cần ghi thêm invoice_detail ngay sau khi tạo invoice.
     */
    public Integer createInvoiceAndReturnId(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Thong tin hoa don khong duoc null");
        }

        validateInvoice(invoice);

        if (invoice.getIssuedDate() == null) {
            invoice.setIssuedDate(LocalDateTime.now());
        }

        return invoiceDao.createAndReturnId(invoice);
    }

    public boolean updateInvoice(Invoice invoice) {
        if (invoice == null || invoice.getId() == null) {
            throw new IllegalArgumentException("Thong tin hoa don khong hop le");
        }

        Invoice existingInvoice = invoiceDao.findById(invoice.getId());
        if (existingInvoice == null) {
            throw new IllegalArgumentException("Hoa don khong ton tai");
        }

        validateInvoice(invoice);
        return invoiceDao.update(invoice) > 0;
    }

    public boolean deleteInvoice(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID hoa don khong hop le");
        }

        Invoice invoice = invoiceDao.findById(id);
        if (invoice == null) {
            throw new IllegalArgumentException("Hoa don khong ton tai");
        }

        return invoiceDao.delete(id) > 0;
    }

    public List<Invoice> getInvoicesByCustomer(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("ID khach hang khong hop le");
        }
        return invoiceDao.findByCustomerId(customerId);
    }

    public List<Invoice> getInvoicesByDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Khoang thoi gian khong hop le");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Ngay bat dau phai truoc ngay ket thuc");
        }
        return invoiceDao.findByDateRange(from, to);
    }

    public List<Invoice> getCurrentMonthInvoices() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.findByDateRange(startOfMonth, endOfMonth);
    }

    public List<Invoice> getTodayInvoices() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.findByDateRange(startOfDay, endOfDay);
    }

    public boolean markInvoiceAsPaid(Integer invoiceId) {
        if (invoiceId == null || invoiceId <= 0) {
            throw new IllegalArgumentException("ID hoa don khong hop le");
        }

        Invoice invoice = invoiceDao.findById(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Hoa don khong ton tai");
        }

        return invoiceDao.markAsPaid(invoiceId) > 0;
    }

    public boolean cancelInvoice(Integer invoiceId) {
        if (invoiceId == null || invoiceId <= 0) {
            throw new IllegalArgumentException("ID hoa don khong hop le");
        }

        Invoice invoice = invoiceDao.findById(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Hoa don khong ton tai");
        }

        if ("PAID".equals(invoice.getStatus())) {
            throw new IllegalStateException("Khong the huy hoa don da thanh toan");
        }

        return invoiceDao.cancel(invoiceId) > 0;
    }

    public boolean updateInvoiceStatus(Integer invoiceId, String status) {
        if (invoiceId == null || invoiceId <= 0) {
            throw new IllegalArgumentException("ID hoa don khong hop le");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Trang thai khong duoc rong");
        }
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Trang thai khong hop le: " + status);
        }

        return invoiceDao.updateStatus(invoiceId, status) > 0;
    }

    public BigDecimal getTotalRevenue(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Khoang thoi gian khong hop le");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Ngay bat dau phai truoc ngay ket thuc");
        }
        return invoiceDao.getTotalRevenue(from, to);
    }

    public BigDecimal getCurrentMonthRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.getTotalRevenue(startOfMonth, endOfMonth);
    }

    public BigDecimal getTodayRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.getTotalRevenue(startOfDay, endOfDay);
    }

    public BigDecimal getCurrentYearRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYear = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfYear = now.withDayOfYear(now.toLocalDate().lengthOfYear())
                .withHour(23).withMinute(59).withSecond(59);
        return invoiceDao.getTotalRevenue(startOfYear, endOfYear);
    }

    private void validateInvoice(Invoice invoice) {
        if (invoice.getCustomerId() == null || invoice.getCustomerId() <= 0) {
            throw new IllegalArgumentException("ID khach hang khong hop le");
        }
        ensureCustomerExists(invoice.getCustomerId());
        ensureEmployeeExists(invoice.getEmployeeId());

        if (invoice.getTotalAmount() == null || invoice.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tong tien phai lon hon 0");
        }

        if (invoice.getStatus() == null || invoice.getStatus().isBlank()) {
            invoice.setStatus("NEW");
        } else if (!isValidStatus(invoice.getStatus())) {
            throw new IllegalArgumentException("Trang thai khong hop le: " + invoice.getStatus());
        }
    }

    private boolean isValidStatus(String status) {
        return "NEW".equals(status)
                || "PENDING".equals(status)
                || "PAID".equals(status)
                || "CANCELED".equals(status);
    }

    public boolean invoiceExists(Integer id) {
        if (id == null || id <= 0) {
            return false;
        }
        return invoiceDao.findById(id) != null;
    }

    public Integer getOrCreateCustomerId(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten khach hang khong duoc rong");
        }

        String normalizedName = customerName.trim();
        CustomerDao customerDao = new CustomerDao();
        Customer existing = customerDao.findByName(normalizedName);
        if (existing != null) {
            return existing.getId();
        }

        Customer newCustomer = new Customer();
        newCustomer.setFullName(normalizedName);
        newCustomer.setStatus("ACTIVE");

        int createResult = new CustomerBus().create(newCustomer);
        if (createResult <= 0) {
            throw new RuntimeException("Khong the tao khach hang moi");
        }

        Customer created = customerDao.findByName(normalizedName);
        if (created == null || created.getId() == null || created.getId() <= 0) {
            throw new RuntimeException("Da tao khach hang nhung khong lay duoc ID");
        }

        return created.getId();
    }

    private void ensureCustomerExists(Integer customerId) {
        try {
            Customer customer = new CustomerDao().findById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Khach hang khong ton tai");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Khach hang khong ton tai: " + customerId, e);
        }
    }

    private void ensureEmployeeExists(Integer employeeId) {
        if (employeeId == null) {
            return;
        }
        if (employeeId <= 0) {
            throw new IllegalArgumentException("ID nhan vien khong hop le");
        }
        try {
            if (new EmployeeDao().findById(employeeId) == null) {
                throw new IllegalArgumentException("Nhan vien khong ton tai");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Nhan vien khong ton tai: " + employeeId, e);
        }
    }
}
