package my_app.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.Timesheet;
import my_app.util.QueryExecutor;

public class TimesheetDao implements GenericDao<Timesheet, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM timesheet";
    private final QueryExecutor qe = new QueryExecutor();

    @Override
    public Timesheet findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Timesheet id must not be null");
        }
        ArrayList<HashMap<String, Object>> results = qe.ExecuteQuery(BASE_QUERY + " WHERE id=?", id);
        return results.isEmpty() ? null : new Timesheet(results.get(0));
    }

    @Override
    public ArrayList<Timesheet> findAll() {
        ArrayList<HashMap<String, Object>> records = qe.ExecuteQuery(BASE_QUERY);
        ArrayList<Timesheet> timesheets = new ArrayList<>(records.size());
        records.forEach(row -> timesheets.add(new Timesheet(row)));
        return timesheets;
    }

    @Override
    public int create(Timesheet entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Timesheet entity must not be null");
        }
        final String insertSql = "INSERT INTO timesheet (employee_id, hours_worked, work_date) VALUES (?,?,?)";
        Date workDate = entity.getWorkDate() != null ? Date.valueOf(entity.getWorkDate()) : null;
        return qe.ExecuteUpdate(insertSql,
                entity.getEmployeeId(),
                entity.getHoursWorked(),
                workDate);
    }

    @Override
    public int update(Timesheet entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Timesheet entity and id must not be null");
        }
        final String updateSql = "UPDATE timesheet SET employee_id=?, hours_worked=?, work_date=? WHERE id=?";
        Date workDate = entity.getWorkDate() != null ? Date.valueOf(entity.getWorkDate()) : null;
        return qe.ExecuteUpdate(updateSql,
                entity.getEmployeeId(),
                entity.getHoursWorked(),
                workDate,
                entity.getId());
    }

    @Override
    public int delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Timesheet id must not be null");
        }
        return qe.ExecuteUpdate("DELETE FROM timesheet WHERE id=?", id);
    }
}
