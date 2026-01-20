package my_app.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Voucher {
    private Integer id;
    private String promotionName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    // Constructors
    public Voucher() {}
    
    public Voucher(Integer id, String promotionName, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.promotionName = promotionName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Voucher(String promotionName, LocalDateTime startDate, LocalDateTime endDate) {
        this(null, promotionName, startDate, endDate);
    }

    public Voucher(Voucher other) {
        this(other.id, other.promotionName, other.startDate, other.endDate);
    }
    public Voucher(Map<String, Object> data) {
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

        String newPromotionName = ModelMapperHelper.getString(data, "promotionName", "promotion_name");
        if (newPromotionName != null) {
            this.promotionName = newPromotionName;
        }

        LocalDateTime newStartDate = ModelMapperHelper.getLocalDateTime(data, "startDate", "start_date");
        if (newStartDate != null) {
            this.startDate = newStartDate;
        }

        LocalDateTime newEndDate = ModelMapperHelper.getLocalDateTime(data, "endDate", "end_date");
        if (newEndDate != null) {
            this.endDate = newEndDate;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getPromotionName() { return promotionName; }
    public void setPromotionName(String promotionName) { this.promotionName = promotionName; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    @Override
    public String toString() {
        return "Voucher{" +
               "id=" + id +
               ", promotionName='" + promotionName + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               '}';
    }
}