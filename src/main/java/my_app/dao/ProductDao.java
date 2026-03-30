package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.IngredientProduct;
import my_app.model.Product;
import my_app.util.QueryExecutor;

public class ProductDao implements GenericDao<Product, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM product";
    private final QueryExecutor qe = new QueryExecutor();
    private final static String TABLE_NAME = "product";

    @Override
    public Product findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Product(results.get(0));
    }

    @Override
    public int getNextID() {
        return qe.NextID(TABLE_NAME);
    }

    public ArrayList<Product> findByName(String Name) {
        if (Name == null || Name.isEmpty()) {
            throw new IllegalArgumentException("Product name must not be null or empty");
        }
        String searchQuery = BASE_QUERY + " WHERE product_name LIKE ?";
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(searchQuery, "%" + Name + "%");
        ArrayList<Product> products = new ArrayList<>(records.size());
        records.forEach(row -> products.add(new Product(row)));
        return products;
    }

    @Override
    public ArrayList<Product> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Product> products = new ArrayList<>(records.size());
        records.forEach(row -> products.add(new Product(row)));
        return products;
    }

    private ArrayList<Integer> ParseListInt(ArrayList<IngredientProduct> ingredientProducts) {
        ArrayList<Integer> list = new ArrayList<>();
        ingredientProducts.forEach(ip -> {
            list.add(ip.getIngredientId());
            list.add(ip.getEstimate());
        });
        ingredientProducts.forEach(ip -> {
            list.add(ip.getIngredientId());
        });
        return list;
    }

    public int getMaxQuantity(ArrayList<IngredientProduct> ingredientProducts) {
        int maxQuantity = 1;
        ArrayList<Integer> list = ParseListInt(ingredientProducts);
        if (list.isEmpty()) {
            return 0;
        }

        String query = "select ing.id, round(ing.total_weight/ case \n";
        String caseStament = "";
        int size = ingredientProducts.size();
        for (int i = 0; i < size; i++) {
            caseStament += "when id = ? then ? \n";
        }
        query += caseStament;
        query += "end ,0)as totalestimate\n"
                + "from ingredient ing\n"
                + "where id in(";
        String inClause = "";
        for (int i = 0; i < size; i++) {
            inClause += "?";
            if (i < size - 1) {
                inClause += ",";
            }
        }
        query += inClause;
        query += ")\n"
                + "order by totalestimate asc";

        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(query, list.toArray());
        if (!results.isEmpty()) {
            maxQuantity = ((Number) results.get(0).get("totalestimate")).intValue();
        }
        return maxQuantity;
    }

    @Override
    public int create(Product entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Product entity must not be null");
        }
        entity.setId(qe.NextID(TABLE_NAME));
        final String insertSql = "INSERT INTO product (id,product_name, unit_price, unit, quantity, status, category_id) VALUES (?,?,?,?,?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getId(),
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
