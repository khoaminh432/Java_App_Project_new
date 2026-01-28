package my_app.dao;

import java.util.ArrayList;

public interface GenericDao<T, ID> {
    T findById(ID id);
    ArrayList<T> findAll();
    int create(T entity);
    int update(T entity);
    int delete(ID id);
}
