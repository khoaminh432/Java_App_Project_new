package my_app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import my_app.bus.CustomerBus;
import my_app.dao.CustomerDao;
import my_app.util.DBConnection;
import my_app.dao.IngredientProductDao;
import my_app.dao.ProductDao;
import my_app.util.QueryExecutor;
/*** Sử dụng JUnit 3 (TestCase, TestSuite).
 * 
 */
public class AppTest extends TestCase {


/**
 * * Using JUnit 3 (TestCase, TestSuite).
 *
 */
public class AppTest extends TestCase {



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
        assertNotNull(App.class);
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
    public void testMethodModel(){
        try {
            CustomerDao customerDao = new CustomerDao();
            assertNotNull(customerDao);
            System.out.println(customerDao.findById(1));
        } catch (Exception e) {
            System.err.println("Database not ready: " + e.getMessage());
        } finally {
            DBConnection.close();
        }}
    // public void testMethodModel() {
    //     CustomerBus customerBus = new CustomerBus();
    //     assertNotNull(customerBus);
    //     customerBus.findAll();
    //     assertNotNull(customerBus.getCustomers());
    //     System.out.println(customerBus.getCustomers());
    //     DBConnection.close();
    // }
    // public void testMethodvalue() {
    //     IngredientBus ingredientBus = new IngredientBus();
    //     assertNotNull(ingredientBus);
    //     ingredientBus.findAll();
    //     System.out.println(ingredientBus.getIngredientProductByThis(new Ingredient(ingredientBus.getIngredients().get(0))).getIngredient());
    //     System.out.println();
    //     DBConnection.close();
    // }
    
    // Test database dependencies should be set up before running this test
    // To run this test, ensure the database is initialized with the required tables
    // You can run: mysql -u [user] -p < src/main/resources/database/mytable.sql
    public void testDaoMethod() {
        ProductDao productDao = new ProductDao();
        assertNotNull(productDao);
        IngredientProductDao productIngredientDao = new IngredientProductDao();
        assertNotNull(productIngredientDao);
        // Commented out - requires database setup
        // System.out.println(productDao.getMaxQuantity(productIngredientDao.findByProductId(4)));
    }

    /**
     * Run the test suite from the command line.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.err.println("Run JUnit 3 tests in AppTest");
    }
}
