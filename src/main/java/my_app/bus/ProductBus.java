package my_app.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.ProductDao;
import my_app.model.Product;

public class ProductBus implements GeneralConfig<Product> {

    private static final ProductDao productDao = new ProductDao();
    public static ArrayList<Product> listProducts = new ArrayList<>();
    private final ObservableList<Product> products;

    public ObservableList<Product> getProducts() {
        return products;
    }

    public ProductBus() {
        this.products = FXCollections.observableArrayList();

    }

    public List<Product> fetchAllFromDb() {
        return productDao.findAll();
    }

    public void replaceAll(List<Product> newProducts) {
        listProducts.clear();
        if (newProducts != null) {
            listProducts.addAll(newProducts);
        }
        setobser();
    }

    private void setobser() {
        products.setAll(listProducts);
    }

    @Override
    public void findAll() {
        replaceAll(productDao.findAll());
    }

    @Override
    public void findById(int id) {
        // TODO Auto-generated method stub

        Product product = productDao.findById(id);
        listProducts.clear();
        if (product != null) {
            listProducts.add(product);
        }
        setobser();
    }

    @Override
    public void searchNameByArray(String name) {
        // TODO Auto-generated method stub
        ArrayList<Product> filteredList = (ArrayList<Product>) listProducts.stream()
                .filter(p -> p.getProductName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        products.setAll(filteredList);
    }

    @Override
    public void searchNameByDB(String name) {
        // TODO Auto-generated method stub
        listProducts = productDao.findByName(name);
        setobser();
    }

    @Override
    public int create(Product obj) {
        // TODO Auto-generated method stub
        int index = productDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Product obj) {
        // TODO Auto-generated method stub
        int index = productDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        int index = productDao.delete(id);
        findAll();
        return index;
    }

}
