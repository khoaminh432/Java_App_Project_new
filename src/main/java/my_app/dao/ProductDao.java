package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.Product;
import my_app.util.QueryExecutor;

public class ProductDao implements GenericDao<Product, Integer> {
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Product findById(Integer id) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>();
    }

    @Override
    public int create(Product entity) {
        return 0;
    }

    @Override
    public int update(Product entity) {
        return 0;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }
}
