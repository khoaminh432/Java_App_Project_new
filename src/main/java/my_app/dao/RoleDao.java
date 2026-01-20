package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my_app.model.Role;
import my_app.util.QueryExecutor;

public class RoleDao implements GenericDao<Role, Integer> {
    private static final String BASE_QUERY = "SELECT * FROM role";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Role findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Role id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Role(results.get(0));
    }

    @Override
    public List<Role> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        List<Role> roles = new ArrayList<>(records.size());
        records.forEach(row -> roles.add(new Role(row)));
        return roles;
    }

    @Override
    public int create(Role entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Role entity must not be null");
        }
        final String insertSql = "INSERT INTO role (role_name, hourly_rate) VALUES (?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getRoleName(),
                entity.getHourlyRate());
    }

    @Override
    public int update(Role entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Role entity and id must not be null");
        }
        final String updateSql = "UPDATE role SET role_name=?, hourly_rate=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getRoleName(),
                entity.getHourlyRate(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Role id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM role WHERE id=?", id);
    }
}
