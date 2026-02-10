package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.IngredientProductDao;
import my_app.model.IngredientProduct;

public class IngredientProductBus implements GeneralConfig<IngredientProduct> {

    private static final IngredientProductDao ingredientProductDao = new IngredientProductDao();
    public static ArrayList<IngredientProduct> listIngredientProducts = new ArrayList<>();
    private final ObservableList<IngredientProduct> ingredientProducts;

    public IngredientProductBus() {
        this.ingredientProducts = FXCollections.observableArrayList();
    }

    public ObservableList<IngredientProduct> getIngredientProducts() {
        return ingredientProducts;
    }

    public List<IngredientProduct> fetchAllFromDb() {
        return ingredientProductDao.findAll();
    }

    public void replaceAll(List<IngredientProduct> newMappings) {
        listIngredientProducts.clear();
        if (newMappings != null) {
            listIngredientProducts.addAll(newMappings);
        }
        syncObservable();
    }

    private void syncObservable() {
        ingredientProducts.setAll(listIngredientProducts);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<IngredientProduct> filterByKeyword(List<IngredientProduct> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(mapping -> containsIgnoreCase(mapping.getId(), needle)
                || containsIgnoreCase(mapping.getProductId(), needle)
                || containsIgnoreCase(mapping.getIngredientId(), needle)
                || containsIgnoreCase(mapping.getEstimate(), needle)
                || containsIgnoreCase(mapping.getUnitPrice(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        IngredientProduct mapping = ingredientProductDao.findById(id);
        listIngredientProducts.clear();
        if (mapping != null) {
            listIngredientProducts.add(mapping);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        ingredientProducts.setAll(filterByKeyword(listIngredientProducts, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<IngredientProduct> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listIngredientProducts = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(IngredientProduct obj) {
        int index = ingredientProductDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(IngredientProduct obj) {
        int index = ingredientProductDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = ingredientProductDao.delete(id);
        findAll();
        return index;
    }

}
