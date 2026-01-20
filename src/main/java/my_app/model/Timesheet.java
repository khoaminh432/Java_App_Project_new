package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Timesheet {
    private Integer id;
    private Integer employeeId;
    private BigDecimal hoursWorked;
    private LocalDate workDate;
    private Employee employee; // Reference to Employee object
    
    // Constructors
    public Timesheet() {}
    
    public Timesheet(Integer id, Integer employeeId, BigDecimal hoursWorked, LocalDate workDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.hoursWorked = hoursWorked;
        this.workDate = workDate;
    }

    public Timesheet(Integer employeeId, BigDecimal hoursWorked, LocalDate workDate) {
        this(null, employeeId, hoursWorked, workDate);
    }

    public Timesheet(Timesheet other) {
        this(other.id, other.employeeId, other.hoursWorked, other.workDate);
        this.employee = other.employee;
    }
    public Timesheet(Map<String, Object> data) {
        applyFromMap(data);
    }
    public void applyFromMap(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }

        Integer newId = ModelMapperHelper.getInteger(data, "id");
        if (newId != null) {
            this.id = newId;
        }

        Integer newEmployeeId = ModelMapperHelper.getInteger(data, "employeeId", "employee_id");
        if (newEmployeeId != null) {
            this.employeeId = newEmployeeId;
        }

        BigDecimal newHoursWorked = ModelMapperHelper.getBigDecimal(data, "hoursWorked", "hours_worked");
        if (newHoursWorked != null) {
            this.hoursWorked = newHoursWorked;
        }

        LocalDate newWorkDate = ModelMapperHelper.getLocalDate(data, "workDate", "work_date");
        if (newWorkDate != null) {
            this.workDate = newWorkDate;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    
    public BigDecimal getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(BigDecimal hoursWorked) { this.hoursWorked = hoursWorked; }
    
    public LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    @Override
    public String toString() {
        return "Timesheet{" +
               "id=" + id +
               ", employeeId=" + employeeId +
               ", hoursWorked=" + hoursWorked +
               ", workDate=" + workDate +
               '}';
    }
}