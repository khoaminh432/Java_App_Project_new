package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.GoodsReceiptDetail;
import my_app.util.QueryExecutor;

public class GoodsReceiptDetailDao implements GenericDao<GoodsReceiptDetail, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM goods_receipt_detail";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public GoodsReceiptDetail findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Goods receipt detail id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new GoodsReceiptDetail(results.get(0));
    }

    @Override
    public ArrayList<GoodsReceiptDetail> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<GoodsReceiptDetail> details = new ArrayList<>(records.size());
        records.forEach(row -> details.add(new GoodsReceiptDetail(row)));
        return details;
    }

    @Override
    public int create(GoodsReceiptDetail entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Goods receipt detail entity must not be null");
        }
        final String insertSql = "INSERT INTO goods_receipt_detail (receipt_id, ingredient_id, quantity, unit_price) VALUES (?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getReceiptId(),
                entity.getIngredientId(),
                entity.getQuantity(),
                entity.getUnitPrice());
    }

    @Override
    public int update(GoodsReceiptDetail entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Goods receipt detail entity and id must not be null");
        }
        final String updateSql = "UPDATE goods_receipt_detail SET receipt_id=?, ingredient_id=?, quantity=?, unit_price=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getReceiptId(),
                entity.getIngredientId(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Goods receipt detail id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM goods_receipt_detail WHERE id=?", id);
    }
}
