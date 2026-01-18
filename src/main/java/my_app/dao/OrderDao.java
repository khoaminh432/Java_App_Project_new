package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.Order;
import my_app.util.QueryExecutor;

public class OrderDao implements GenericDao<Order, Integer> {
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Order findById(Integer id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>();
    }

    @Override
    public int create(Order entity) {
        return 0;
    }

    @Override
    public int update(Order entity) {
        return 0;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }
}
