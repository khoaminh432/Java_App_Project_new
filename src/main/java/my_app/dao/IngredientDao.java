package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.Ingredient;
import my_app.util.QueryExecutor;

public class IngredientDao implements GenericDao<Ingredient, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM ingredient";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Ingredient findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Ingredient id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Ingredient(results.get(0));
    }

    @Override
    public ArrayList<Ingredient> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Ingredient> ingredients = new ArrayList<>(records.size());
        records.forEach(row -> ingredients.add(new Ingredient(row)));
        return ingredients;
    }

    @Override
    public int create(Ingredient entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Ingredient entity must not be null");
        }
        final String insertSql = "INSERT INTO ingredient (ingredient_name, net_weight, quantity) VALUES (?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getIngredientName(),
                entity.getNetWeight(),
                entity.getQuantity());
    }

    @Override
    public int update(Ingredient entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Ingredient entity and id must not be null");
        }
        final String updateSql = "UPDATE ingredient SET ingredient_name=?, net_weight=?, quantity=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getIngredientName(),
                entity.getNetWeight(),
                entity.getQuantity(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Ingredient id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM ingredient WHERE id=?", id);
    }
}
