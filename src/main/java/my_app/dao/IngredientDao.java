package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.Ingredient;
import my_app.util.QueryExecutor;

public class IngredientDao implements GenericDao<Ingredient, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM ingredient";
    private final QueryExecutor qe = new QueryExecutor();
    private final static String TABLE_NAME = "ingredient";
    private static final String countQuery = "SELECT COUNT(*) AS total FROM ingredient";

    @Override
    public Ingredient findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Ingredient id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Ingredient(results.get(0));
    }

    @Override
    public int getNextID() {
        return qe.NextID(TABLE_NAME);
    }

    public int count() {
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(countQuery);
        if (results.isEmpty() || !results.get(0).containsKey("total")) {
            throw new IllegalStateException("Count query did not return a valid result");
        }
        Object totalObj = results.get(0).get("total");
        if (totalObj instanceof Number) {
            return ((Number) totalObj).intValue();
        } else {
            throw new IllegalStateException("Count query result is not a number");
        }
    }

    @Override
    public ArrayList<Ingredient> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Ingredient> ingredients = new ArrayList<>(records.size());
        records.forEach(row -> ingredients.add(new Ingredient(row)));
        return ingredients;
    }

    @Override
    public ArrayList<Ingredient> findAll(int limit, int page) {
        if (limit <= 0 || page < 0) {
            throw new IllegalArgumentException("Limit must be greater than 0 and page must be non-negative");
        }
        int offset = limit * page;
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY + " WHERE id > ? LIMIT ?", offset, limit);
        ArrayList<Ingredient> ingredients = new ArrayList<>(records.size());
        records.forEach(row -> ingredients.add(new Ingredient(row)));
        return ingredients;
    }

    @Override
    public int create(Ingredient entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Ingredient entity must not be null");
        }
        final String insertSql = "INSERT INTO ingredient (ingredient_name, net_weight, quantity, total_weight, unit_price) VALUES (?,?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getIngredientName(),
                entity.getNetWeight(),
                entity.getQuantity(),
                entity.getTotalWeight(),
                entity.getUnitPrice());
    }

    @Override
    public int update(Ingredient entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Ingredient entity and id must not be null");
        }
        final String updateSql = "UPDATE ingredient SET ingredient_name=?, net_weight=?, quantity=?, total_weight=?, unit_price=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getIngredientName(),
                entity.getNetWeight(),
                entity.getQuantity(),
                entity.getTotalWeight(),
                entity.getUnitPrice(),
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
