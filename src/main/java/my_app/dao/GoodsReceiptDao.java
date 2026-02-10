package my_app.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.GoodsReceipt;
import my_app.util.QueryExecutor;

public class GoodsReceiptDao implements GenericDao<GoodsReceipt, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM goods_receipt";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public GoodsReceipt findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Goods receipt id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new GoodsReceipt(results.get(0));
    }

    @Override
    public ArrayList<GoodsReceipt> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<GoodsReceipt> receipts = new ArrayList<>(records.size());
        records.forEach(row -> receipts.add(new GoodsReceipt(row)));
        return receipts;
    }

    @Override
    public int create(GoodsReceipt entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Goods receipt entity must not be null");
        }
        final String insertSql = "INSERT INTO goods_receipt (received_date, supplier_id, total_quantity, total_price) VALUES (?,?,?,?)";
        Timestamp receivedDate = entity.getReceivedDate() != null ? Timestamp.valueOf(entity.getReceivedDate()) : null;
        return qe.ExecuteUpdate(insertSql,
                receivedDate,
                entity.getSupplierId(),
                entity.getTotalQuantity(),
                entity.getTotalPrice());
    }

    @Override
    public int update(GoodsReceipt entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Goods receipt entity and id must not be null");
        }
        final String updateSql = "UPDATE goods_receipt SET received_date=?, supplier_id=?, total_quantity=?, total_price=? WHERE id=?";
        Timestamp receivedDate = entity.getReceivedDate() != null ? Timestamp.valueOf(entity.getReceivedDate()) : null;
        return qe.ExecuteUpdate(updateSql,
                receivedDate,
                entity.getSupplierId(),
                entity.getTotalQuantity(),
                entity.getTotalPrice(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Goods receipt id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM goods_receipt WHERE id=?", id);
    }
}
