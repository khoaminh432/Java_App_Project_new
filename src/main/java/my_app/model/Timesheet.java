package my_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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