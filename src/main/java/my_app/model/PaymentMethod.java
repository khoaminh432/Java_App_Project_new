package my_app.model;

import java.util.Map;

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
    public PaymentMethod(Map<String, Object> data) {
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

        String newMethodName = ModelMapperHelper.getString(data, "methodName", "method_name");
        if (newMethodName != null) {
            this.methodName = newMethodName;
        }
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