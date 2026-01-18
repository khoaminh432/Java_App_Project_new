package my_app.model;

public class PaymentMethod {
    private Integer id;
    private String methodName;
    
    // Constructors
    public PaymentMethod() {}
    
    public PaymentMethod(Integer id, String methodName) {
        this.id = id;
        this.methodName = methodName;
    }

    public PaymentMethod(String methodName) {
        this(null, methodName);
    }

    public PaymentMethod(PaymentMethod other) {
        this(other.id, other.methodName);
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    
    @Override
    public String toString() {
        return "PaymentMethod{" +
               "id=" + id +
               ", methodName='" + methodName + '\'' +
               '}';
    }
}