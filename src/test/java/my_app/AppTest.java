package my_app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import my_app.service.UserService;
/*** Sử dụng JUnit 3 (TestCase, TestSuite).
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
     * Kiểm tra class App tồn tại và có thể khởi tạo.
     */
    public void testAppClassExists() {
        App app = new App();
        assertNotNull(app);
    }
    public void testUserServiceMethod() {
        UserService userService = new UserService();
        assertNotNull(userService);
        userService.someServiceMethod(); // Gọi phương thức để kiểm tra nó hoạt động
    }
    public void testConfigProperties() {
        
    }
    /**
     * Kiểm tra model User có thể khởi tạo bằng constructor mặc định.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.err.println("Run JUnit 3 tests in AppTest");
    }
}
