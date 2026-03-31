package my_app.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my_app.model.Invoice;
import my_app.util.QueryExecutor;

public class InvoiceDao implements GenericDao<Invoice, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM invoice";
    private static final String TABLE_NAME = "invoice";

    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Invoice findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id must not be null");
        }

        try {
            ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
            return results.isEmpty() ? null : new Invoice(results.get(0));
        } catch (Exception e) {
            System.err.println("Invoice lookup failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Invoice> findAll() {
        try {
            ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY + " ORDER BY issued_date DESC, id DESC");
            ArrayList<Invoice> invoices = new ArrayList<>(records.size());
            records.forEach(row -> invoices.add(new Invoice(row)));
            return invoices;
        } catch (Exception e) {
            System.err.println("Invoice query failed: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<Invoice> findAll(int limit, int page) {
        if (limit <= 0 || page < 0) {
            throw new IllegalArgumentException("Limit must be greater than 0 and page must be non-negative");
        }

        int offset = limit * page;
        ArrayList<HashMap<String, Object>> records =
                qe.ExecuteQuery(BASE_QUERY + " ORDER BY issued_date DESC, id DESC LIMIT ? OFFSET ?", limit, offset);

        ArrayList<Invoice> invoices = new ArrayList<>(records.size());
        records.forEach(row -> invoices.add(new Invoice(row)));
        return invoices;
    }

    @Override
    public int getNextID() {
        return qe.NextID(TABLE_NAME);
    }

    @Override
    public int create(Invoice entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Invoice entity must not be null");
        }

        String sql = "INSERT INTO invoice (customer_id, employee_id, order_id, payment_method_id, issued_date, total_amount, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Timestamp issuedDate = entity.getIssuedDate() != null ? Timestamp.valueOf(entity.getIssuedDate()) : null;

        try {
            return qe.ExecuteUpdate(
                    sql,
                    entity.getCustomerId(),
                    entity.getEmployeeId(),
                    entity.getOrderId(),
                    entity.getPaymentMethodId(),
                    issuedDate,
                    entity.getTotalAmount(),
                    normalizeStatus(entity.getStatus()));
        } catch (RuntimeException e) {
            String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (message.contains("foreign key")) {
                throw new RuntimeException("Du lieu tham chieu khong hop le", e);
            }
            throw e;
        }
    }

    /**
     * Tạo hóa đơn mới và trả về id vừa được insert (dùng để ghi invoice_detail).
     */
    public Integer createAndReturnId(Invoice entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Invoice entity must not be null");
        }

        String sql = "INSERT INTO invoice (customer_id, employee_id, order_id, payment_method_id, issued_date, total_amount, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Timestamp issuedDate = entity.getIssuedDate() != null ? Timestamp.valueOf(entity.getIssuedDate()) : null;

        int affected = qe.ExecuteUpdate(
                sql,
                entity.getCustomerId(),
                entity.getEmployeeId(),
                entity.getOrderId(),
                entity.getPaymentMethodId(),
                issuedDate,
                entity.getTotalAmount(),
                normalizeStatus(entity.getStatus()));

        if (affected <= 0) {
            return null;
        }

        // Lấy id vừa insert trong MySQL
        ArrayList<HashMap<String, Object>> rows = qe.ExecuteQuery("SELECT LAST_INSERT_ID() AS id");
        if (rows.isEmpty()) {
            return null;
        }

        Object val = rows.get(0).get("id");
        if (val instanceof Number n) {
            return n.intValue();
        }
        return null;
    }

    @Override
    public int update(Invoice entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Invoice entity and id must not be null");
        }

        String sql = "UPDATE invoice "
                + "SET customer_id=?, employee_id=?, order_id=?, payment_method_id=?, issued_date=?, total_amount=?, status=? "
                + "WHERE id=?";

        Timestamp issuedDate = entity.getIssuedDate() != null ? Timestamp.valueOf(entity.getIssuedDate()) : null;

        return qe.ExecuteUpdate(
                sql,
                entity.getCustomerId(),
                entity.getEmployeeId(),
                entity.getOrderId(),
                entity.getPaymentMethodId(),
                issuedDate,
                entity.getTotalAmount(),
                normalizeStatus(entity.getStatus()),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM invoice WHERE id=?", id);
    }

    public List<Invoice> findByCustomerId(Integer customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer id must not be null");
        }

        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(
                BASE_QUERY + " WHERE customer_id=? ORDER BY issued_date DESC, id DESC",
                customerId);

        List<Invoice> invoices = new ArrayList<>(records.size());
        records.forEach(row -> invoices.add(new Invoice(row)));
        return invoices;
    }

    public List<Invoice> findByDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range must not be null");
        }

        try {
            ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(
                    BASE_QUERY + " WHERE issued_date BETWEEN ? AND ? ORDER BY issued_date DESC, id DESC",
                    Timestamp.valueOf(from),
                    Timestamp.valueOf(to));

            List<Invoice> invoices = new ArrayList<>(records.size());
            records.forEach(row -> invoices.add(new Invoice(row)));
            return invoices;
        } catch (Exception e) {
            System.err.println("Invoice date-range query failed: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int updateStatus(Integer invoiceId, String status) {
        if (invoiceId == null || status == null) {
            throw new IllegalArgumentException("Invoice id and status must not be null");
        }
        return qe.ExecuteUpdate("UPDATE invoice SET status=? WHERE id=?", normalizeStatus(status), invoiceId);
    }

    public int markAsPaid(Integer invoiceId) {
        return updateStatus(invoiceId, "PAID");
    }

    public int cancel(Integer invoiceId) {
        return updateStatus(invoiceId, "CANCELED");
    }

    public BigDecimal getTotalRevenue(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range must not be null");
        }

        try {
            ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(
                    "SELECT COALESCE(SUM(total_amount), 0) AS revenue "
                            + "FROM invoice WHERE issued_date BETWEEN ? AND ? AND status = 'PAID'",
                    Timestamp.valueOf(from),
                    Timestamp.valueOf(to));

            if (results.isEmpty()) {
                return BigDecimal.ZERO;
            }

            Object revenue = results.get(0).get("revenue");
            if (revenue instanceof BigDecimal bigDecimal) {
                return bigDecimal;
            }
            if (revenue instanceof Number number) {
                return BigDecimal.valueOf(number.doubleValue());
            }
            return BigDecimal.ZERO;
        } catch (Exception e) {
            System.err.println("Revenue query failed: " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public ArrayList<Invoice> findAll1(int limit, int page) {
        return findAll(limit, page);
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "NEW";
        }
        return status.trim().toUpperCase();
    }
}
