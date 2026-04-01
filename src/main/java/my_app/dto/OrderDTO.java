package my_app.dto;

public class OrderDTO {
    private int id;
    private String customer;
    private String date;
    private String status;
    private String total;

    public OrderDTO() {}

    public OrderDTO(int id, String customer, String date, String status, String total) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.status = status;
        this.total = total;
    }

    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getTotal() { return total; }

    public void setId(int id) { this.id = id; }
    public void setCustomer(String customer) { this.customer = customer; }
    public void setDate(String date) { this.date = date; }
    public void setStatus(String status) { this.status = status; }
    public void setTotal(String total) { this.total = total; }
}