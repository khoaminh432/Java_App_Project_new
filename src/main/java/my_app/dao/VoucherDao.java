package my_app.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.Voucher;
import my_app.util.QueryExecutor;

public class VoucherDao implements GenericDao<Voucher, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM voucher";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Voucher findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Voucher id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Voucher(results.get(0));
    }

    @Override
    public ArrayList<Voucher> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Voucher> vouchers = new ArrayList<>(records.size());
        records.forEach(row -> vouchers.add(new Voucher(row)));
        return vouchers;
    }

    @Override
    public int create(Voucher entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Voucher entity must not be null");
        }
        final String insertSql = "INSERT INTO voucher (promotion_name, start_date, end_date) VALUES (?,?,?)";
        Timestamp startDate = entity.getStartDate() != null ? Timestamp.valueOf(entity.getStartDate()) : null;
        Timestamp endDate = entity.getEndDate() != null ? Timestamp.valueOf(entity.getEndDate()) : null;
        return qe.ExecuteUpdate(insertSql,
                entity.getPromotionName(),
                startDate,
                endDate);
    }

    @Override
    public int update(Voucher entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Voucher entity and id must not be null");
        }
        final String updateSql = "UPDATE voucher SET promotion_name=?, start_date=?, end_date=? WHERE id=?";
        Timestamp startDate = entity.getStartDate() != null ? Timestamp.valueOf(entity.getStartDate()) : null;
        Timestamp endDate = entity.getEndDate() != null ? Timestamp.valueOf(entity.getEndDate()) : null;
        return qe.ExecuteUpdate(updateSql,
                entity.getPromotionName(),
                startDate,
                endDate,
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Voucher id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM voucher WHERE id=?", id);
    }
}
