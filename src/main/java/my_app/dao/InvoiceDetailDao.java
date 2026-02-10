package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.InvoiceDetail;
import my_app.util.QueryExecutor;


public class InvoiceDetailDao implements GenericDao<InvoiceDetail, Integer> {
    private static final String BASE_QUERY = "SELECT * FROM invoice_detail";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public InvoiceDetail findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice detail id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new InvoiceDetail(results.get(0));
    }

    @Override
    public ArrayList<InvoiceDetail> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<InvoiceDetail> details = new ArrayList<>(records.size());
        records.forEach(row -> details.add(new InvoiceDetail(row)));
        return details;
    }

    @Override
    public int create(InvoiceDetail entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Invoice detail entity must not be null");
        }
        final String insertSql = "INSERT INTO invoice_detail (invoice_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getInvoiceId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getUnitPrice());
    }

    @Override
    public int update(InvoiceDetail entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Invoice detail entity and id must not be null");
        }
        final String updateSql = "UPDATE invoice_detail SET invoice_id=?, product_id=?, quantity=?, unit_price=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getInvoiceId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice detail id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM invoice_detail WHERE id=?", id);
    }
}
