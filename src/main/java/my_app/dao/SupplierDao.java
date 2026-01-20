package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my_app.model.Supplier;
import my_app.util.QueryExecutor;

public class SupplierDao implements GenericDao<Supplier, Integer> {
    private static final String BASE_QUERY = "SELECT * FROM supplier";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Supplier findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Supplier id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Supplier(results.get(0));
    }

    @Override
    public List<Supplier> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        List<Supplier> suppliers = new ArrayList<>(records.size());
        records.forEach(row -> suppliers.add(new Supplier(row)));
        return suppliers;
    }

    @Override
    public int create(Supplier entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Supplier entity must not be null");
        }
        final String insertSql = "INSERT INTO supplier (supplier_name, address, phone_number) VALUES (?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getSupplierName(),
                entity.getAddress(),
                entity.getPhoneNumber());
    }

    @Override
    public int update(Supplier entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Supplier entity and id must not be null");
        }
        final String updateSql = "UPDATE supplier SET supplier_name=?, address=?, phone_number=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getSupplierName(),
                entity.getAddress(),
                entity.getPhoneNumber(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Supplier id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM supplier WHERE id=?", id);
    }
}
