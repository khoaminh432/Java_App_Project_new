package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.Customer;
import my_app.util.QueryExecutor;

public class CustomerDao implements GenericDao<Customer, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM customer";
    private final QueryExecutor qe = new QueryExecutor();
    private final static String TABLE_NAME = "customer";

    @Override
    public Customer findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer id must not be null");
        }
        Customer customer = new Customer(qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id).get(0));
        return customer;
    }

    @Override
    public int getNextID() {
        return qe.NextID(TABLE_NAME);
    }

    @Override
    public ArrayList<Customer> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Customer> customers = new ArrayList<>(records.size());
        records.forEach(row -> customers.add(new Customer(row)));
        return customers;
    }

    public ArrayList<Customer> findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be null or empty");
        }
        String searchQuery = BASE_QUERY + " WHERE full_name LIKE ?";
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(searchQuery, "%" + name + "%");
        ArrayList<Customer> customers = new ArrayList<>(records.size());
        records.forEach(row -> customers.add(new Customer(row)));
        return customers;
    }

    @Override
    public int create(Customer entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Customer entity must not be null");
        }
        final String insertSql = "INSERT INTO customer (full_name, phone_number, email, password, status) VALUES (?,?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getFullName(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getStatus());
    }

    @Override
    public int update(Customer entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Customer entity and id must not be null");
        }
        final String updateSql = "UPDATE customer SET full_name=?, phone_number=?, email=?, password=?, status=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getFullName(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getStatus(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM customer WHERE id=?", id);
    }
}
