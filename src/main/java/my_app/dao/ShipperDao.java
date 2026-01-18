package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.Shipper;
import my_app.util.QueryExecutor;

public class ShipperDao implements GenericDao<Shipper, Integer> {
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Shipper findById(Integer id) { return null; }

    @Override
    public List<Shipper> findAll() { return new ArrayList<>(); }

    @Override
    public int create(Shipper entity) { return 0; }

    @Override
    public int update(Shipper entity) { return 0; }

    @Override
    public int delete(Integer id) { return 0; }
}
