package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.Role;
import my_app.util.QueryExecutor;

public class RoleDao implements GenericDao<Role, Integer> {
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Role findById(Integer id) { return null; }

    @Override
    public List<Role> findAll() { return new ArrayList<>(); }

    @Override
    public int create(Role entity) { return 0; }

    @Override
    public int update(Role entity) { return 0; }

    @Override
    public int delete(Integer id) { return 0; }
}
