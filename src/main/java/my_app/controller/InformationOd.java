package my_app.controller;

public class InformationOd {
    int id;
    String customer,date,status,total;

    public InformationOd(int id, String customer, String date, String status, String total) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.status = status;
        this.total = total;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public String getDate() {
        return date;
    }
    public String getStatus() {
        return status;
    }
    public String getTotal() {
        return total;
    }


    
}
