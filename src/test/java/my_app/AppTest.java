package my_app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import my_app.bus.CustomerBus;
import my_app.util.DBConnection;
import my_app.util.QueryExecutor;

/**
 * * Using JUnit 3 (TestCase, TestSuite).
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
     * Verify that the App class exists and can be instantiated.
     */
    public void testAppClassExists() {
        App app = new App();
        assertNotNull(app);
    }
    // public void testUserServiceMethod() {
    //     UserService userService = new UserService();
    //     assertNotNull(userService);
    //     userService.someServiceMethod(); // Call the method to ensure it works
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
    public void testMethodModel() {
        CustomerBus customerBus = new CustomerBus();
        assertNotNull(customerBus);
        customerBus.findAll();
        assertNotNull(customerBus.getCustomers());
        System.out.println(customerBus.getCustomers());
        DBConnection.close();
    }

    /**
     * Run the test suite from the command line.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.err.println("Run JUnit 3 tests in AppTest");
    }
}
