package my_app.controller;

public class InformationOd {
    private int id;
    private String customer;
    private String date;
    private String status;
    private String total;

    public InformationOd(int id, String customer, String date, String status, String total) {
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
}