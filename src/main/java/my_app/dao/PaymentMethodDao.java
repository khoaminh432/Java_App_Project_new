package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.PaymentMethod;
import my_app.util.QueryExecutor;

public class PaymentMethodDao implements GenericDao<PaymentMethod, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM payment_method";
    private final QueryExecutor qe = new QueryExecutor();
    private final static String TABLE_NAME = "payment_method";

    @Override
    public PaymentMethod findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Payment method id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new PaymentMethod(results.get(0));
    }

    @Override
    public int getNextID() {
        return qe.NextID(TABLE_NAME);
    }

    @Override
    public ArrayList<PaymentMethod> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<PaymentMethod> methods = new ArrayList<>(records.size());
        records.forEach(row -> methods.add(new PaymentMethod(row)));
        return methods;
    }

    @Override
    public ArrayList<PaymentMethod> findAll(int limit, int page) {
        if (limit <= 0 || page < 0) {
            throw new IllegalArgumentException("Limit must be greater than 0 and page must be non-negative");
        }
        int offset = limit * page;
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY + " WHERE id > ? LIMIT ?", offset, limit);
        ArrayList<PaymentMethod> methods = new ArrayList<>(records.size());
        records.forEach(row -> methods.add(new PaymentMethod(row)));
        return methods;
    }

    @Override
    public int create(PaymentMethod entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Payment method entity must not be null");
        }
        final String insertSql = "INSERT INTO payment_method (method_name) VALUES (?)";
        return qe.ExecuteUpdate(insertSql, entity.getMethodName());
    }

    @Override
    public int update(PaymentMethod entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Payment method entity and id must not be null");
        }
        final String updateSql = "UPDATE payment_method SET method_name=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getMethodName(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Payment method id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM payment_method WHERE id=?", id);
    }
}
