package my_app.dao;

import java.util.ArrayList;

public interface Generic<T> {
    public ArrayList<T> findAll();
    public ArrayList<T> findbyID(int id);
    public ArrayList<T> findbyName(String search);
    public int insert(T obj);
    public int update(T obj);
    public int delete(int id);
}
