package my_app.dao;

import java.util.ArrayList;

import my_app.model.Customer;
import my_app.util.QueryExecutor;

public class CustomerDao implements GenericDao<Customer, Integer> {
    private final QueryExecutor qe = new QueryExecutor();
    private final String QUERYALL = "SELECT * FROM customer "; // Example query
    @Override
    public Customer findById(Integer id) {
        Customer customer = new Customer(qe.ExecuteQuery(QUERYALL+" where id=?",id).get(0));
        return customer;
    }

    @Override
    public ArrayList<Customer> findAll() {
        ArrayList<Customer> list = new ArrayList<Customer>();
        qe.ExecuteQuery(QUERYALL).forEach(action->{
            Customer cus = new Customer(action);
            list.add(cus);
        });
        return list;
    }

    @Override
    public ArrayList<Customer> findAll(String status){
        ArrayList<Customer> list  = new ArrayList<Customer>();
        qe.ExecuteQuery(QUERYALL+" WHERE status = ?", status).forEach(action->{
            Customer cus = new Customer(action);
            list.add(cus);
        });
        return list;
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
        final String deleteSql = "DELETE FROM customer WHERE id=?";
        return qe.ExecuteUpdate(deleteSql, id);
    }
}
