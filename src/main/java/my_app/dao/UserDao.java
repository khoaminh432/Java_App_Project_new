package my_app.dao;

import java.util.ArrayList;
import java.util.List;

import my_app.model.User;
import my_app.util.QueryExecutor;

public class UserDao implements GenericDao<User, Integer> {
	private final QueryExecutor qe = new QueryExecutor();

	@Override
	public User findById(Integer id) {
		// TODO: implement query to load User by id
		return null;
	}

	@Override
	public List<User> findAll() {
		// TODO: implement query to return all users
		return new ArrayList<>();
	}

	@Override
	public int create(User entity) {
		// TODO: implement insert
		return 0;
	}

	@Override
	public int update(User entity) {
		// TODO: implement update
		return 0;
	}

	@Override
	public int delete(Integer id) {
		// TODO: implement delete
		return 0;
	}
}
