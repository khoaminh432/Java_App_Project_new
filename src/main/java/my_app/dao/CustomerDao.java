package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.Customer;
import my_app.util.QueryExecutor;

public class CustomerDao implements GenericDao<Customer, Integer> {
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Customer findById(Integer id) {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>();
    }

    @Override
    public int create(Customer entity) {
        return 0;
    }

    @Override
    public int update(Customer entity) {
        return 0;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }
}
