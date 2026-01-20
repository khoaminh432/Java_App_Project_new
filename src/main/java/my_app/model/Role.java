package my_app.model;

import java.math.BigDecimal;
import java.util.Map;

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
    public Role(Map<String, Object> data) {
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

        String newRoleName = ModelMapperHelper.getString(data, "roleName", "role_name");
        if (newRoleName != null) {
            this.roleName = newRoleName;
        }

        BigDecimal newHourlyRate = ModelMapperHelper.getBigDecimal(data, "hourlyRate", "hourly_rate");
        if (newHourlyRate != null) {
            this.hourlyRate = newHourlyRate;
        }
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