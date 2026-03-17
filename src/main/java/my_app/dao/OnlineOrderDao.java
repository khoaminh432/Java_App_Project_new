package my_app.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.OnlineOrder;
import my_app.util.QueryExecutor;

public class OnlineOrderDao implements GenericDao<OnlineOrder, Integer> {

    private static final String BASE_QUERY
            = "SELECT o.id, o.customer_id, o.order_date, o.sub_total, o.total_amount, o.status, "
            + "oo.shipper_id, oo.receiver_name, oo.phone_number, oo.address, oo.shipping_fee, "
            + "oo.estimated_delivery_time, oo.completed_time "
            + "FROM online_order oo INNER JOIN `order` o ON oo.id = o.id";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public OnlineOrder findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Online order id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE o.id=?", id);
        return results.isEmpty() ? null : new OnlineOrder(results.get(0));
    }

    @Override
    public ArrayList<OnlineOrder> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<OnlineOrder> orders = new ArrayList<>(records.size());
        records.forEach(row -> orders.add(new OnlineOrder(row)));
        return orders;
    }

    @Override
    public int create(OnlineOrder entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Online order entity must not be null");
        }
        final String insertOrderSql = "INSERT INTO `order` (customer_id, order_date, sub_total, total_amount, status) VALUES (?,?,?,?,?)";
        Timestamp orderDate = toTimestamp(entity.getOrderDate());
        qe.ExecuteUpdate(insertOrderSql,
                entity.getCustomerId(),
                orderDate,
                entity.getSubTotal(),
                entity.getTotalAmount(),
                entity.getStatus());
        Integer orderId = fetchLastInsertId();
        if (orderId == null) {
            throw new IllegalStateException("Unable to retrieve generated order id");
        }
        entity.setId(orderId);
        final String insertOnlineSql
                = "INSERT INTO online_order (id, customer_id, shipper_id, receiver_name, phone_number, address, "
                + "shipping_fee, estimated_delivery_time, completed_time, status, total_amount) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Timestamp estimated = toTimestamp(entity.getEstimatedDeliveryTime());
        Timestamp completed = toTimestamp(entity.getCompletedTime());
        return qe.ExecuteUpdate(insertOnlineSql,
                orderId,
                entity.getCustomerId(),
                entity.getShipperId(),
                entity.getReceiverName(),
                entity.getPhoneNumber(),
                entity.getAddress(),
                entity.getShippingFee(),
                estimated,
                completed,
                entity.getStatus(),
                entity.getTotalAmount());
    }

    @Override
    public int update(OnlineOrder entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Online order entity and id must not be null");
        }
        final String updateOrderSql = "UPDATE `order` SET customer_id=?, order_date=?, sub_total=?, total_amount=?, status=? WHERE id=?";
        Timestamp orderDate = toTimestamp(entity.getOrderDate());
        int orderRows = qe.ExecuteUpdate(updateOrderSql,
                entity.getCustomerId(),
                orderDate,
                entity.getSubTotal(),
                entity.getTotalAmount(),
                entity.getStatus(),
                entity.getId());
        final String updateOnlineSql
                = "UPDATE online_order SET customer_id=?, shipper_id=?, receiver_name=?, phone_number=?, address=?, "
                + "shipping_fee=?, estimated_delivery_time=?, completed_time=?, status=?, total_amount=? WHERE id=?";
        Timestamp estimated = toTimestamp(entity.getEstimatedDeliveryTime());
        Timestamp completed = toTimestamp(entity.getCompletedTime());
        int onlineRows = qe.ExecuteUpdate(updateOnlineSql,
                entity.getCustomerId(),
                entity.getShipperId(),
                entity.getReceiverName(),
                entity.getPhoneNumber(),
                entity.getAddress(),
                entity.getShippingFee(),
                estimated,
                completed,
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getId());
        return orderRows + onlineRows;
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Online order id must not be null");
        }
        int childRows = qe.ExecuteUpdate("DELETE FROM online_order WHERE id=?", id);
        int orderRows = qe.ExecuteUpdate("DELETE FROM `order` WHERE id=?", id);
        return childRows + orderRows;
    }

    private Integer fetchLastInsertId() {
        ArrayList<HashMap<String, Object>> results
                = qe.ExecuteQuery("SELECT LAST_INSERT_ID() AS generated_id");
        if (results.isEmpty()) {
            return null;
        }
        Object value = results.get(0).get("generated_id");
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt(((String) value).trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private Timestamp toTimestamp(LocalDateTime value) {
        return value == null ? null : Timestamp.valueOf(value);
    }
}
