package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.OrderDetail;
import my_app.util.QueryExecutor;

public class OrderDetailDao implements GenericDao<OrderDetail, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM order_detail";
    private final QueryExecutor qe = new QueryExecutor();
    private final static String TABLE_NAME = "order_detail";

    @Override
    public int getNextID() {
        return qe.NextID(TABLE_NAME);
    }

    @Override
    public OrderDetail findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Order detail id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new OrderDetail(results.get(0));
    }

    @Override
    public ArrayList<OrderDetail> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<OrderDetail> details = new ArrayList<>(records.size());
        records.forEach(row -> details.add(new OrderDetail(row)));
        return details;
    }

    @Override
    public ArrayList<OrderDetail> findAll(int limit, int page) {
        if (limit <= 0 || page < 0) {
            throw new IllegalArgumentException("Limit must be greater than 0 and page must be non-negative");
        }
        int offset = limit * page;
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY + " WHERE id > ? LIMIT ?", offset, limit);
        ArrayList<OrderDetail> details = new ArrayList<>(records.size());
        records.forEach(row -> details.add(new OrderDetail(row)));
        return details;
    }

    @Override
    public int create(OrderDetail entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Order detail entity must not be null");
        }
        final String insertSql = "INSERT INTO order_detail (order_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getOrderId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getUnitPrice());
    }

    @Override
    public int update(OrderDetail entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Order detail entity and id must not be null");
        }
        final String updateSql = "UPDATE order_detail SET order_id=?, product_id=?, quantity=?, unit_price=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getOrderId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Order detail id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM order_detail WHERE id=?", id);
    }
}
