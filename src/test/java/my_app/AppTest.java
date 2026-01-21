package my_app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import my_app.dao.CustomerDao;
import my_app.dao.InvoiceDao;
import my_app.util.DBConnection;
import my_app.util.QueryExecutor;
/*** Sử dụng JUnit 3 (TestCase, TestSuite).
 * 
 */
public class AppTest extends TestCase {
    private QueryExecutor qe = new QueryExecutor();
    
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Kiểm tra class App tồn tại và có thể khởi tạo.
     */
    public void testAppClassExists() {
        App app = new App();
        assertNotNull(app);
    }
    // public void testUserServiceMethod() {
    //     UserService userService = new UserService();
    //     assertNotNull(userService);
    //     userService.someServiceMethod(); // Gọi phương thức để kiểm tra nó hoạt động
    // }
    // public void testcheckConnectDB(){
    //     DBConnection dbConnection = DBConnection.getInstance();
    //     assertNotNull(dbConnection);
    //     dbConnection.close();
    // }
    
    // public void testDBConnectionSingleton() {
    //     List<Object> instances = new ArrayList<>();
    //     QueryExecutor qe = new QueryExecutor();
    //     assertNotNull(qe);
    //     ArrayList<HashMap<String,Object>> results = qe.ExecuteQuery("SELECT * from customer where id=?"   ,1);
    //     assertNotNull(results);
    //     results.forEach(record -> {
    //         System.out.println(record);
    //     });
    //     DBConnection.close();
    // }
    public void testMethodModel(){
        new QueryExecutor();
        new QueryExecutor();
        CustomerDao customerDao = new CustomerDao();
        assertNotNull(customerDao);
        System.out.println(customerDao.findById(1));
        InvoiceDao invoiceDao = new InvoiceDao();
        assertNotNull(invoiceDao);
        System.out.println(invoiceDao.findAll());
        
        DBConnection.close();
    }
    
    /**
     * Kiểm tra model User có thể khởi tạo bằng constructor mặc định.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.err.println("Run JUnit 3 tests in AppTest");
    }
}
