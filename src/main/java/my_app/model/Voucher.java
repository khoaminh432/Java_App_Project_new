package my_app.model;

import java.time.LocalDateTime;

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