package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.Role;
import my_app.util.QueryExecutor;

public class RoleDao implements GenericDao<Role, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM role";
    private final QueryExecutor qe = new QueryExecutor();
    private final static String TABLE_NAME = "role";

    @Override
    public Role findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Role id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Role(results.get(0));
    }

    @Override
    public int getNextID() {
        return qe.NextID(TABLE_NAME);
    }

    @Override
    public ArrayList<Role> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Role> roles = new ArrayList<>(records.size());
        records.forEach(row -> roles.add(new Role(row)));
        return roles;
    }

    @Override
    public ArrayList<Role> findAll(int limit, int page) {
        if (limit <= 0 || page < 0) {
            throw new IllegalArgumentException("Limit must be greater than 0 and page must be non-negative");
        }
        int offset = limit * page;
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY + " WHERE id > ? LIMIT ?", offset, limit);
        ArrayList<Role> roles = new ArrayList<>(records.size());
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
