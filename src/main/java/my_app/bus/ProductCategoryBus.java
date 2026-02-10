package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.ProductCategoryDao;
import my_app.model.ProductCategory;

public class ProductCategoryBus implements GeneralConfig<ProductCategory> {

    private static final ProductCategoryDao productCategoryDao = new ProductCategoryDao();
    public static ArrayList<ProductCategory> listProductCategories = new ArrayList<>();
    private final ObservableList<ProductCategory> productCategories;

    public ProductCategoryBus() {
        this.productCategories = FXCollections.observableArrayList();
    }

    public ObservableList<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public List<ProductCategory> fetchAllFromDb() {
        return productCategoryDao.findAll();
    }

    public void replaceAll(List<ProductCategory> newCategories) {
        listProductCategories.clear();
        if (newCategories != null) {
            listProductCategories.addAll(newCategories);
        }
        syncObservable();
    }

    private void syncObservable() {
        productCategories.setAll(listProductCategories);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<ProductCategory> filterByKeyword(List<ProductCategory> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(category -> containsIgnoreCase(category.getCategoryName(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        ProductCategory category = productCategoryDao.findById(id);
        listProductCategories.clear();
        if (category != null) {
            listProductCategories.add(category);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        productCategories.setAll(filterByKeyword(listProductCategories, name));
    }

    @Override
    public void searchNameByDB(String name) {
        ArrayList<ProductCategory> refres = new ArrayList<>(filterByKeyword(fetchAllFromDb(), name));
        listProductCategories = refres;
        syncObservable();
    }

    @Override
    public int create(ProductCategory obj) {
        int index = productCategoryDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(ProductCategory obj) {
        int index = productCategoryDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = productCategoryDao.delete(id);
        findAll();
        return index;
    }
}
