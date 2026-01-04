package my_app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
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
        System.out.println();
        return new TestSuite(AppTest.class);
        
    }

    /**
     * Kiểm tra class App tồn tại và có thể khởi tạo.
     */
    public void testAppClassExists() {
        App app = new App();
        assertNotNull(app);
        System.out.println(app.checkclass());
    }
    /**
     * Kiểm tra model User có thể khởi tạo bằng constructor mặc định.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.err.println("Run JUnit 3 tests in AppTest");
    }
}
