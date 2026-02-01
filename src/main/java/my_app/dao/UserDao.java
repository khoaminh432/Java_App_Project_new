package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import my_app.model.User;
import my_app.util.QueryExecutor;

public class UserDao implements GenericDao<User, Integer> {
	private static final String BASE_QUERY = "SELECT * FROM `user`";
	private final QueryExecutor qe = new QueryExecutor();

	@Override
	public User findById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
		return results.isEmpty() ? null : mapToUser(results.get(0));
	}

	@Override
	public 	ArrayList<User> findAll() {
		ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
		ArrayList<User> users = new ArrayList<>(records.size());
		records.forEach(row -> users.add(mapToUser(row)));
		return users;
	}

	@Override
	public int create(User entity) {
		Map<String, Object> attrs = requireAttributes(entity);
		Object username = resolveAttribute(attrs, "username", "user_name");
		Object password = resolveAttribute(attrs, "password");
		if (username == null || password == null) {
			throw new IllegalArgumentException("Username and password attributes are required");
		}
		Object email = resolveAttribute(attrs, "email");
		Object status = resolveAttribute(attrs, "status");
		Object roleId = resolveAttribute(attrs, "roleId", "role_id");
		final String insertSql = "INSERT INTO `user` (username, password, email, status, role_id) VALUES (?,?,?,?,?)";
		return qe.ExecuteUpdate(insertSql, username, password, email, status, roleId);
	}

	@Override
	public int update(User entity) {
		Map<String, Object> attrs = requireAttributes(entity);
		Object idValue = resolveAttribute(attrs, "id");
		if (!(idValue instanceof Number)) {
			throw new IllegalArgumentException("User id attribute is required for update");
		}
		Object username = resolveAttribute(attrs, "username", "user_name");
		Object password = resolveAttribute(attrs, "password");
		Object email = resolveAttribute(attrs, "email");
		Object status = resolveAttribute(attrs, "status");
		Object roleId = resolveAttribute(attrs, "roleId", "role_id");
		final String updateSql = "UPDATE `user` SET username=?, password=?, email=?, status=?, role_id=? WHERE id=?";
		return qe.ExecuteUpdate(updateSql,
				username,
				password,
				email,
				status,
				roleId,
				((Number) idValue).intValue());
	}

	@Override
	public int delete(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		return qe.ExecuteUpdate("DELETE FROM `user` WHERE id=?", id);
	}

	private User mapToUser(Map<String, Object> row) {
		User user = new User();
		user.applyFromMap(row);
		return user;
	}

	private Map<String, Object> requireAttributes(User entity) {
		if (entity == null) {
			throw new IllegalArgumentException("User entity must not be null");
		}
		Map<String, Object> attrs = entity.getAttributes();
		if (attrs == null || attrs.isEmpty()) {
			throw new IllegalArgumentException("User entity must contain attributes");
		}
		return attrs;
	}

	private Object resolveAttribute(Map<String, Object> attrs, String... keys) {
		if (attrs == null || keys == null) {
			return null;
		}
		for (String key : keys) {
			if (key != null && attrs.containsKey(key)) {
				Object value = attrs.get(key);
				if (value != null) {
					return value;
				}
			}
		}
		return null;
	}
}
