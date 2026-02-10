package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.IngredientProduct;
import my_app.util.QueryExecutor;

public class IngredientProductDao implements GenericDao<IngredientProduct, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM ingredient_product";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public IngredientProduct findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Ingredient product id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new IngredientProduct(results.get(0));
    }

    @Override
    public ArrayList<IngredientProduct> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<IngredientProduct> mappings = new ArrayList<>(records.size());
        records.forEach(row -> mappings.add(new IngredientProduct(row)));
        return mappings;
    }

    @Override
    public int create(IngredientProduct entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Ingredient product entity must not be null");
        }
        final String insertSql = "INSERT INTO ingredient_product (product_id, ingredient_id, estimate, unit_price) VALUES (?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getProductId(),
                entity.getIngredientId(),
                entity.getEstimate(),
                entity.getUnitPrice());
    }

    @Override
    public int update(IngredientProduct entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Ingredient product entity and id must not be null");
        }
        final String updateSql = "UPDATE ingredient_product SET product_id=?, ingredient_id=?, estimate=?, unit_price=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getProductId(),
                entity.getIngredientId(),
                entity.getEstimate(),
                entity.getUnitPrice(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Ingredient product id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM ingredient_product WHERE id=?", id);
    }

    public IngredientProduct findByIngredientId(Integer ingredientId) {
        final String query = "select grd.ingredient_id,grd.unit_price\n"
                + //
                "from goods_receipt_detail grd\n"
                + //
                "join ingredient ing on ing.id = grd.ingredient_id\n"
                + //
                "where ing.id = ?\n"
                + //
                "order by grd.ingredient_id desc\n"
                + //
                "limit 1\n"
                + //
                ";";
        if (ingredientId == null) {
            throw new IllegalArgumentException("Ingredient id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(query, ingredientId);
        return results.isEmpty() ? null : new IngredientProduct(results.get(0));
    }
}
