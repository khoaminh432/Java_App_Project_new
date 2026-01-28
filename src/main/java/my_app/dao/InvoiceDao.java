package my_app.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.Invoice;
import my_app.util.QueryExecutor;

public class InvoiceDao implements GenericDao<Invoice, Integer> {
    private static final String BASE_QUERY = "SELECT * FROM invoice";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Invoice findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Invoice(results.get(0));
    }

    @Override
    public  ArrayList<Invoice> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Invoice> invoices = new ArrayList<>(records.size());
        records.forEach(row -> invoices.add(new Invoice(row)));
        return invoices;
    }

    @Override
    public int create(Invoice entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Invoice entity must not be null");
        }
        final String insertSql = "INSERT INTO invoice (customer_id, employee_id, order_id, payment_method_id, issued_date, total_amount) VALUES (?,?,?,?,?,?)";
        Timestamp issuedDate = entity.getIssuedDate() != null ? Timestamp.valueOf(entity.getIssuedDate()) : null;
        return qe.ExecuteUpdate(insertSql,
                entity.getCustomerId(),
                entity.getEmployeeId(),
                entity.getOrderId(),
                entity.getPaymentMethodId(),
                issuedDate,
                entity.getTotalAmount());
    }

    @Override
    public int update(Invoice entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Invoice entity and id must not be null");
        }
        final String updateSql = "UPDATE invoice SET customer_id=?, employee_id=?, order_id=?, payment_method_id=?, issued_date=?, total_amount=? WHERE id=?";
        Timestamp issuedDate = entity.getIssuedDate() != null ? Timestamp.valueOf(entity.getIssuedDate()) : null;
        return qe.ExecuteUpdate(updateSql,
                entity.getCustomerId(),
                entity.getEmployeeId(),
                entity.getOrderId(),
                entity.getPaymentMethodId(),
                issuedDate,
                entity.getTotalAmount(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM invoice WHERE id=?", id);
    }
}
