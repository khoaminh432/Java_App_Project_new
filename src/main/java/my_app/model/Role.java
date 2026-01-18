package my_app.model;

import java.math.BigDecimal;

public class Role {
    private Integer id;
    private String roleName;
    private BigDecimal hourlyRate;
    
    // Constructors
    public Role() {}
    
    public Role(Integer id, String roleName, BigDecimal hourlyRate) {
        this.id = id;
        this.roleName = roleName;
        this.hourlyRate = hourlyRate;
    }

    public Role(String roleName, BigDecimal hourlyRate) {
        this(null, roleName, hourlyRate);
    }

    public Role(Role other) {
        this(other.id, other.roleName, other.hourlyRate);
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    
    @Override
    public String toString() {
        return "Role{" +
               "id=" + id +
               ", roleName='" + roleName + '\'' +
               ", hourlyRate=" + hourlyRate +
               '}';
    }
}