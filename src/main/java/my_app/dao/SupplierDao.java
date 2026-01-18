package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.Supplier;
import my_app.util.QueryExecutor;

public class SupplierDao implements GenericDao<Supplier, Integer> {
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Supplier findById(Integer id) { return null; }

    @Override
    public List<Supplier> findAll() { return new ArrayList<>(); }

    @Override
    public int create(Supplier entity) { return 0; }

    @Override
    public int update(Supplier entity) { return 0; }

    @Override
    public int delete(Integer id) { return 0; }
}
