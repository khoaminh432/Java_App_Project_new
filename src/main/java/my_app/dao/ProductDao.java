package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my_app.model.Product;
import my_app.util.QueryExecutor;

public class ProductDao implements GenericDao<Product, Integer> {
    private static final String BASE_QUERY = "SELECT * FROM product";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Product findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Product(results.get(0));
    }

    public List<Product> findByName(String Name){
        if (Name == null || Name.isEmpty()) {
            throw new IllegalArgumentException("Product name must not be null or empty");
        }
        String searchQuery = BASE_QUERY + " WHERE product_name LIKE ?";
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(searchQuery, "%" + Name + "%");
        List<Product> products = new ArrayList<>(records.size());
        records.forEach(row -> products.add(new Product(row)));
        return products;
    }

    @Override
    public List<Product> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        List<Product> products = new ArrayList<>(records.size());
        records.forEach(row -> products.add(new Product(row)));
        return products;
    }

    @Override
    public int create(Product entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Product entity must not be null");
        }
        final String insertSql = "INSERT INTO product (product_name, unit_price, unit, quantity, status, category_id) VALUES (?,?,?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getProductName(),
                entity.getUnitPrice(),
                entity.getUnit(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getCategoryId());
    }

    @Override
    public int update(Product entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Product entity and id must not be null");
        }
        final String updateSql = "UPDATE product SET product_name=?, unit_price=?, unit=?, quantity=?, status=?, category_id=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getProductName(),
                entity.getUnitPrice(),
                entity.getUnit(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getCategoryId(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM product WHERE id=?", id);
    }
}
