package my_app.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;

import my_app.model.Invoice;
import my_app.util.QueryExecutor;

public class InvoiceDao implements GenericDao<Invoice, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM invoice";
    private final QueryExecutor qe = new QueryExecutor();

    // ========================
    // CRUD CƠ BẢN (GenericDao)
    // ========================

    @Override
    public Invoice findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id must not be null");
        }

        try {
            ArrayList<HashMap<String, Object>> results =
                    qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);

            return results.isEmpty() ? null : new Invoice(results.get(0));
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi khi truy vấn Invoice theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Invoice> findAll() {
        try {
            ArrayList<HashMap<String, Object>> records =
                    qe.ExecuteQuery(BASE_QUERY);

            ArrayList<Invoice> invoices = new ArrayList<>(records.size());
            records.forEach(row -> invoices.add(new Invoice(row)));

            return invoices;
        } catch (Exception e) {
            System.err.println("⚠️ Chi tiết lỗi khi truy vấn Invoice: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public int create(Invoice entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Invoice entity must not be null");
        }

        String sql = "INSERT INTO invoice (customer_id, employee_id, order_id, payment_method_id, " +
                "issued_date, total_amount, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Timestamp issuedDate = entity.getIssuedDate() != null
                ? Timestamp.valueOf(entity.getIssuedDate())
                : null;

        try {
            return qe.ExecuteUpdate(
                    sql,
                    entity.getCustomerId(),
                    entity.getEmployeeId(),
                    entity.getOrderId(),
                    entity.getPaymentMethodId(),
                    issuedDate,
                    entity.getTotalAmount(),
                    "NEW"
            );
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                System.err.println("⚠️ Lỗi ràng buộc khóa ngoài: " + e.getMessage());
                throw new RuntimeException("Dữ liệu tham chiếu không hợp lệ (Khách hàng/Nhân viên/Đơn hàng/Phương thức thanh toán không tồn tại)", e);
            }
            throw e;
        }
    }

    @Override
    public int update(Invoice entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Invoice entity and id must not be null");
        }

        String sql = "UPDATE invoice " +
                "SET customer_id=?, " +
                "    employee_id=?, " +
                "    order_id=?, " +
                "    payment_method_id=?, " +
                "    issued_date=?, " +
                "    total_amount=?, " +
                "    status=? " +
                "WHERE id=?";

        Timestamp issuedDate = entity.getIssuedDate() != null
                ? Timestamp.valueOf(entity.getIssuedDate())
                : null;

        return qe.ExecuteUpdate(
                sql,
                entity.getCustomerId(),
                entity.getEmployeeId(),
                entity.getOrderId(),
                entity.getPaymentMethodId(),
                issuedDate,
                entity.getTotalAmount(),
                entity.getStatus(),
                entity.getId()
        );
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id must not be null");
        }

        return qe.ExecuteUpdate(
                "DELETE FROM invoice WHERE id=?",
                id
        );
    }

    // ========================
    // TRUY VẤN THEO NGHIỆP VỤ
    // ========================

    // 1. Lấy danh sách hóa đơn theo khách hàng
    public List<Invoice> findByCustomerId(Integer customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer id must not be null");
        }

        ArrayList<HashMap<String, Object>> records =
                qe.ExecuteQuery(
                        BASE_QUERY + " WHERE customer_id=?",
                        customerId
                );

        List<Invoice> invoices = new ArrayList<>();
        records.forEach(row -> invoices.add(new Invoice(row)));

        return invoices;
    }

    // 2. Lấy danh sách hóa đơn theo khoảng ngày
    public List<Invoice> findByDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range must not be null");
        }

        try {
            // Convert LocalDateTime to Date for reliable date comparison
            java.sql.Date startDate = java.sql.Date.valueOf(from.toLocalDate());
            java.sql.Date endDate = java.sql.Date.valueOf(to.toLocalDate());
            
            // Use DATE() function in SQL for timezone-safe date comparison
            ArrayList<HashMap<String, Object>> records =
                    qe.ExecuteQuery(
                            BASE_QUERY + " WHERE DATE(issued_date) BETWEEN ? AND ?",
                            startDate,
                            endDate
                    );

            List<Invoice> invoices = new ArrayList<>();
            records.forEach(row -> invoices.add(new Invoice(row)));

            return invoices;
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi khi truy vấn Invoice theo ngày: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ========================
    // TRẠNG THÁI HÓA ĐƠN
    // ========================

    public int updateStatus(Integer invoiceId, String status) {
        if (invoiceId == null || status == null) {
            throw new IllegalArgumentException("Invoice id and status must not be null");
        }

        String sql = "UPDATE invoice SET status=? WHERE id=?";
        return qe.ExecuteUpdate(sql, status, invoiceId);
    }

    public int markAsPaid(Integer invoiceId) {
        return updateStatus(invoiceId, "PAID");
    }

    public int cancel(Integer invoiceId) {
        return updateStatus(invoiceId, "CANCELED");
    }

    // ========================
    // THỐNG KÊ – DOANH THU
    // ========================

    public BigDecimal getTotalRevenue(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range must not be null");
        }

        try {
            // Convert LocalDateTime to Date for reliable date comparison
            java.sql.Date startDate = java.sql.Date.valueOf(from.toLocalDate());
            java.sql.Date endDate = java.sql.Date.valueOf(to.toLocalDate());
            
            // Use DATE() function in SQL for timezone-safe date comparison
            String sql = "SELECT COALESCE(SUM(total_amount), 0) as revenue " +
                    "FROM invoice " +
                    "WHERE DATE(issued_date) BETWEEN ? AND ? " +
                    "AND status = 'PAID'";

            ArrayList<HashMap<String, Object>> results =
                    qe.ExecuteQuery(
                            sql,
                            startDate,
                            endDate
                    );

            if (results.isEmpty()) {
                return BigDecimal.ZERO;
            }

            Object revenue = results.get(0).get("revenue");
            if (revenue == null) {
                return BigDecimal.ZERO;
            }

            if (revenue instanceof BigDecimal) {
                return (BigDecimal) revenue;
            } else if (revenue instanceof Number) {
                return BigDecimal.valueOf(((Number) revenue).doubleValue());
            }

            return BigDecimal.ZERO;
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi khi tính doanh thu: " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}