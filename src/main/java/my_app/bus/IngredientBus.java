package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.IngredientDao;
import my_app.dao.IngredientProductDao;
import my_app.model.Ingredient;
import my_app.model.IngredientProduct;

public class IngredientBus implements GeneralConfig<Ingredient> {

    private static final IngredientDao ingredientDao = new IngredientDao();
    private static final IngredientProductDao ingredientProductDao = new IngredientProductDao();
    public static ArrayList<Ingredient> listIngredients = new ArrayList<>();
    private final ObservableList<Ingredient> ingredients;

    public IngredientBus() {
        this.ingredients = FXCollections.observableArrayList();
    }

    public ObservableList<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Ingredient> fetchAllFromDb() {
        return ingredientDao.findAll();
    }

    public void replaceAll(List<Ingredient> newIngredients) {
        listIngredients.clear();
        if (newIngredients != null) {
            listIngredients.addAll(newIngredients);
        }
        syncObservable();
    }

    private void syncObservable() {
        ingredients.setAll(listIngredients);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<Ingredient> filterByKeyword(List<Ingredient> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(ingredient -> containsIgnoreCase(ingredient.getIngredientName(), needle)
                || containsIgnoreCase(ingredient.getId(), needle)
                || containsIgnoreCase(ingredient.getNetWeight(), needle)
                || containsIgnoreCase(ingredient.getQuantity(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        Ingredient ingredient = ingredientDao.findById(id);
        listIngredients.clear();
        if (ingredient != null) {
            listIngredients.add(ingredient);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        ingredients.setAll(filterByKeyword(listIngredients, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<Ingredient> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listIngredients = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(Ingredient obj) {
        int index = ingredientDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Ingredient obj) {
        int index = ingredientDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = ingredientDao.delete(id);
        findAll();
        return index;
    }

    public IngredientProduct getIngredientProductByThis(Ingredient ingredienttemp) {
        IngredientProduct IngProTemp = ingredientProductDao.findByIngredientId(ingredienttemp.getId());
        if (IngProTemp == null) {
            return null;
        }
        IngProTemp.setIngredient(ingredienttemp);

        return IngProTemp;
    }
}
