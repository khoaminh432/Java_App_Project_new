package my_app.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my_app.model.Order;
import my_app.util.QueryExecutor;

public class OrderDao implements GenericDao<Order, Integer> {
    private static final String BASE_QUERY = "SELECT * FROM `order`";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Order findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Order id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Order(results.get(0));
    }

    @Override
    public List<Order> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        List<Order> orders = new ArrayList<>(records.size());
        records.forEach(row -> orders.add(new Order(row)));
        return orders;
    }

    @Override
    public int create(Order entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Order entity must not be null");
        }
        final String insertSql = "INSERT INTO `order` (customer_id, order_date, sub_total, total_amount, status) VALUES (?,?,?,?,?)";
        Timestamp orderDate = entity.getOrderDate() != null ? Timestamp.valueOf(entity.getOrderDate()) : null;
        return qe.ExecuteUpdate(insertSql,
                entity.getCustomerId(),
                orderDate,
                entity.getSubTotal(),
                entity.getTotalAmount(),
                entity.getStatus());
    }

    @Override
    public int update(Order entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Order entity and id must not be null");
        }
        final String updateSql = "UPDATE `order` SET customer_id=?, order_date=?, sub_total=?, total_amount=?, status=? WHERE id=?";
        Timestamp orderDate = entity.getOrderDate() != null ? Timestamp.valueOf(entity.getOrderDate()) : null;
        return qe.ExecuteUpdate(updateSql,
                entity.getCustomerId(),
                orderDate,
                entity.getSubTotal(),
                entity.getTotalAmount(),
                entity.getStatus(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Order id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM `order` WHERE id=?", id);
    }
}
