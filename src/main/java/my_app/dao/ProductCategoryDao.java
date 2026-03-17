package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.ProductCategory;
import my_app.util.QueryExecutor;

public class ProductCategoryDao implements GenericDao<ProductCategory, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM product_category";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public ProductCategory findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product category id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new ProductCategory(results.get(0));
    }

    @Override
    public ArrayList<ProductCategory> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<ProductCategory> categories = new ArrayList<>(records.size());
        records.forEach(row -> categories.add(new ProductCategory(row)));
        return categories;
    }

    @Override
    public int create(ProductCategory entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Product category entity must not be null");
        }
        final String insertSql = "INSERT INTO product_category (category_name, description) VALUES (?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getCategoryName(),
                entity.getDescription());
    }

    @Override
    public int update(ProductCategory entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Product category entity and id must not be null");
        }
        final String updateSql = "UPDATE product_category SET category_name=?, description=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getCategoryName(),
                entity.getDescription(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product category id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM product_category WHERE id=?", id);
    }
}
