package my_app.dao;

import java.util.List;

public interface GenericDao<T, ID> {
    T findById(ID id);
    List<T> findAll();
    int create(T entity);
    int update(T entity);
    int delete(ID id);
}
