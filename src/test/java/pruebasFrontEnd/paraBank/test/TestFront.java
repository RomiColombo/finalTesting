package pruebasFrontEnd.paraBank.test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pruebasFrontEnd.base.BasePage;
import pruebasFrontEnd.extentReports.ExtentFactory;
import pruebasFrontEnd.paraBank.pages.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFront {

    private static BasePage basePage;
    private static WebDriver webDriver;
    static ExtentSparkReporter sparkReporter = new ExtentSparkReporter("./target/Spark.html");
    static ExtentReports extentReports;

    private String successMsgAccount = "You are now logged in";
    private String successMsgCreateAccount = "Congratulations, your account is now open";
    private String successMsgAccountSummary = "Balance includes deposits that may be subject to holds";
    private String titleTransfer = "Transfer Funds";
    private String successMsgTransfer = "Transfer Complete!";

    private String userName = "romicolombo";
    private String password = "12345";

    @BeforeAll
    static void setUp(){
        basePage = new BasePage();
        webDriver = basePage.getWebDriver();
        extentReports = ExtentFactory.getInstance();
        extentReports.attachReporter(sparkReporter);
    }

    @BeforeEach
    public void initialPage(){
        basePage.openApp();
    }

    @Order(1)
    @Test
    @Tag("Smoke")
    @DisplayName("Proceso de registro")
    public void testRegistry() {

        Registry registry = new Registry();
        registry.goToRegister();
        registry.doRegister("Romina", "Colombo", "Mi casa 123","Corrientes", "Argentina", "3400", "123456", "123", "romicolombo", "12345", "12345");
        registry.sendCredentials();
        String result = registry.confirmRegistry();

        Boolean confirm = result.contains(successMsgAccount);
        Assertions.assertEquals(true, confirm);
        System.out.println("Test pass: " + confirm);
    }

    @Order(2)
    @Test
    @Tag("Regression")
    @DisplayName("Proceso de login")
    public void testLogin(){
        Login login = new Login();
        login.login(userName, password);
        String result = login.confirmLogin();
        Boolean confirm = result.contains(successMsgAccount);
        System.out.println("Test pass: " + confirm );
    }

    @Order(3)
    @Test
    @Tag("Regression")
    @DisplayName("Apertura de una nueva cuenta")
    public void testNewAccount() {
        NewAccount newAccount = new NewAccount();
        newAccount.goToCreateAccount();

        newAccount.createAccount();

        String result = newAccount.confirmAccount();

        Boolean confirm = result.contains(successMsgCreateAccount);
        System.out.println("Test pass: "+confirm);
    }

    @Test
    @Order(4)
    @Tag("Regression")
    @DisplayName("Visión general de la cuenta")
    public void testAccountSummary() {

        AccountSummary accountSummary = new AccountSummary();
        accountSummary.goToSummary();

        String result = accountSummary.confirm();

        Boolean confirm = result.contains(successMsgAccountSummary);
        System.out.println("Test pass: "+confirm);
    }

    @Test
    @Order(5)
    @Tag("Regression")
    @DisplayName("Transferencia de fondos")
    public void testTransferFounds() {

        TransferFounds transferFounds = new TransferFounds();
        String confirmTitle = transferFounds.goToTransfer();
        Boolean titleCorrect = confirmTitle.contains(titleTransfer);
        System.out.println("Transfer page: "+titleCorrect);

        transferFounds.doTransfer("3245");

        transferFounds.sendTransfer();

        String result = transferFounds.confirmTransfer();

        Boolean confirm = result.contains(successMsgTransfer);
        System.out.println("Test pass: "+confirm);
    }

    @Test
    @Order(6)
    @Tag("Regression")
    @DisplayName("Actividad de la cuenta")
    public void testAccountActivity() {

        AccountActivity accountActivity = new AccountActivity();
        testAccountSummary();

        accountActivity.goToAccountDetail();
        accountActivity.checkActivity();
    }

    @AfterAll
    static void tearDown(){
        extentReports.flush();
        webDriver.quit();
    }
}
