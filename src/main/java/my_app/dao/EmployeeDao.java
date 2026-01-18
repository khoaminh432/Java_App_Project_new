package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.Employee;
import my_app.util.QueryExecutor;

public class EmployeeDao implements GenericDao<Employee, Integer> {
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Employee findById(Integer id) { return null; }

    @Override
    public List<Employee> findAll() { return new ArrayList<>(); }

    @Override
    public int create(Employee entity) { return 0; }

    @Override
    public int update(Employee entity) { return 0; }

    @Override
    public int delete(Integer id) { return 0; }
}
