package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.InvoiceVoucherDetail;
import my_app.util.QueryExecutor;

public class InvoiceVoucherDetailDao implements GenericDao<InvoiceVoucherDetail, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM invoice_voucher_detail";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public InvoiceVoucherDetail findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice voucher detail id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new InvoiceVoucherDetail(results.get(0));
    }

    @Override
    public ArrayList<InvoiceVoucherDetail> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<InvoiceVoucherDetail> details = new ArrayList<>(records.size());
        records.forEach(row -> details.add(new InvoiceVoucherDetail(row)));
        return details;
    }

    @Override
    public int create(InvoiceVoucherDetail entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Invoice voucher detail entity must not be null");
        }
        final String insertSql = "INSERT INTO invoice_voucher_detail (invoice_id, voucher_id, discount_value) VALUES (?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getInvoiceId(),
                entity.getVoucherId(),
                entity.getDiscountValue());
    }

    @Override
    public int update(InvoiceVoucherDetail entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Invoice voucher detail entity and id must not be null");
        }
        final String updateSql = "UPDATE invoice_voucher_detail SET invoice_id=?, voucher_id=?, discount_value=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getInvoiceId(),
                entity.getVoucherId(),
                entity.getDiscountValue(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invoice voucher detail id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM invoice_voucher_detail WHERE id=?", id);
    }
}
