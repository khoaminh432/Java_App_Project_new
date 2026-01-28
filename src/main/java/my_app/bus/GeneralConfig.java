package my_app.bus;

public class GeneralConfig<T> {
    public void findAll();
    public void findById(int id);
    public void searchNameByArray(String name);
    public void searchNameByDB(String name);

    public int create(T obj);
    public int update(T obj);
    public int delete(int id);
    
}
