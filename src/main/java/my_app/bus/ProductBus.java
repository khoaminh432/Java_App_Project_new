package my_app.bus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import my_app.dao.ProductDao;
import my_app.model.Product;

public class ProductBus implements GeneralConfig<Product> {

    private static final ProductDao productDao = new ProductDao();
    private final ObservableList<Product> products;
    private final FilteredList<Product> filteredProducts;

    public ObservableList<Product> getProducts() {
        return products;
    }

    public FilteredList<Product> getFilteredProducts() {
        return filteredProducts;
    }

    private void ShowFilterAll() {
        filteredProducts.setPredicate(p -> {
            return true;
        });
    }

    private void ShowFilteredFromName(String name) {
        filteredProducts.setPredicate(p -> {
            if (name == null || name.isBlank()) {
                return true;
            }
            if (p.getProductName() != null && p.getProductName().toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
            return false;
        });
        System.out.println("Filtered products count: " + filteredProducts);
    }

    public ProductBus() {
        this.products = FXCollections.observableArrayList();
        this.filteredProducts = new FilteredList<>(products, p -> true);
    }

    public void replaceAll(ArrayList<Product> newProducts) {
        setobser(newProducts);
        ShowFilterAll();
    }

    private void setobser(ArrayList<Product> newProducts) {
        products.setAll(newProducts);
    }

    @Override
    public void findAll() {
        replaceAll(productDao.findAll());
    }

    @Override
    public void findById(int id) {
        // TODO Auto-generated method stub

        Product product = productDao.findById(id);
        if (product != null) {
            setobser(new ArrayList<Product>() {
                {
                    add(product);
                }
            });
        } else {
            ShowFilterAll();
        }
    }

    @Override
    public void searchNameByArray(String name) {
        // TODO Auto-generated method stub
        ShowFilteredFromName(name);
    }

    @Override
    public void searchNameByDB(String name) {
        // TODO Auto-generated method stub
        ArrayList<Product> listProducts = productDao.findByName(name);
        setobser(listProducts);
    }

    private void ValidateObject(Product obj) throws Exception {
        if (obj.getProductName() == null || obj.getProductName().isBlank()) {
            throw new Exception("Product name cannot be empty");
        }

    }

    @Override
    public int create(Product obj) {
        // TODO Auto-generated method stub
        try {
            ValidateObject(obj);
            int index = productDao.create(obj);
            getProducts().add(obj);
            return index;
        } catch (Exception e) {

        }
        return -1;
    }

    @Override
    public int update(Product obj) {
        // TODO Auto-generated method stub
        int index = productDao.update(obj);
        products.set(index, obj);
        return index;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        int index = productDao.delete(id);
        products.removeIf(filter -> filter.getId() == id);
        return index;
    }

}
