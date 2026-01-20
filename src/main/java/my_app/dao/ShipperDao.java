package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my_app.model.Shipper;
import my_app.util.QueryExecutor;

public class ShipperDao implements GenericDao<Shipper, Integer> {
    private static final String BASE_QUERY =
            "SELECT e.id, e.first_name AS first_name, e.last_name AS last_name, e.phone_number AS phone_number, " +
            "e.dob, e.address, e.basic_salary AS basic_salary, e.status, e.role_id AS role_id, " +
            "s.vehicle_plate_number AS vehicle_plate_number, s.current_status AS current_status " +
            "FROM employee e INNER JOIN shipper s ON e.id = s.id";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Shipper findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Shipper id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE e.id=?", id);
        return results.isEmpty() ? null : new Shipper(results.get(0));
    }

    @Override
    public List<Shipper> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        List<Shipper> shippers = new ArrayList<>(records.size());
        records.forEach(row -> shippers.add(new Shipper(row)));
        return shippers;
    }

    @Override
    public int create(Shipper entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Shipper entity and id must not be null (employee row required)");
        }
        final String insertSql = "INSERT INTO shipper (id, vehicle_plate_number, current_status) VALUES (?,?,?)";
        return qe.ExecuteUpdate(insertSql,
                entity.getId(),
                entity.getVehiclePlateNumber(),
                entity.getCurrentStatus());
    }

    @Override
    public int update(Shipper entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Shipper entity and id must not be null");
        }
        final String updateSql = "UPDATE shipper SET vehicle_plate_number=?, current_status=? WHERE id=?";
        return qe.ExecuteUpdate(updateSql,
                entity.getVehiclePlateNumber(),
                entity.getCurrentStatus(),
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Shipper id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM shipper WHERE id=?", id);
    }
}
