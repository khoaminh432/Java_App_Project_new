package my_app.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my_app.model.Employee;
import my_app.util.QueryExecutor;

public class EmployeeDao implements GenericDao<Employee, Integer> {
    private static final String BASE_QUERY = "SELECT * FROM employee";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Employee findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee id must not be null");
        }
        Employee employee = new Employee(qe.ExecuteQuery(BASE_QUERY+ " WHERE id=?", id).get(0));
        return employee;
    }

    @Override
    public  ArrayList<Employee> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Employee> employees = new ArrayList<>(records.size());
        records.forEach(row -> employees.add(new Employee(row)));
        return employees;
    }

    @Override
    public int create(Employee entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Employee entity must not be null");
        }
        final String insertSql = "INSERT INTO employee (first_name, last_name, phone_number, dob, address, basic_salary, status, role_id) VALUES (?,?,?,?,?,?,?,?)";
        Date dob = entity.getDob() != null ? Date.valueOf(entity.getDob()) : null;
        return qe.ExecuteUpdate(insertSql,
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhoneNumber(),
                dob,
                entity.getAddress(),
                entity.getBasicSalary(),
                entity.getStatus(),
                entity.getRoleId());
    }

    @Override
    public int update(Employee entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Employee entity and id must not be null");
        }
        final String updateSql = "UPDATE employee SET first_name=?, last_name=?, phone_number=?, dob=?, address=?, basic_salary=?, status=?, role_id=? WHERE id=?";
        Date dob = entity.getDob() != null ? Date.valueOf(entity.getDob()) : null;
        return qe.ExecuteUpdate(updateSql,
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhoneNumber(),
                dob,
                entity.getAddress(),
                entity.getBasicSalary(),
                entity.getStatus(),
                entity.getRoleId(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM employee WHERE id=?", id);
    }
}
