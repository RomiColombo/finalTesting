package pruebasFrontEnd.paraBank.test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import pruebasFrontEnd.base.BasePage;
import pruebasFrontEnd.extentReports.ExtentFactory;
import pruebasFrontEnd.paraBank.pages.*;

import javax.swing.text.StyledEditorKit;

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
    @DisplayName("Proceso de registro")
    public void testRegistry() {

        //Registry
        Registry registry = new Registry();
        registry.goToRegister();
        registry.doRegister("El", "Firulais", "Mi casa 123","Beverly Hills", "California", "90210", "123456", "123", "elFirulais", "12345", "12345");
        registry.sendCredentials();
        String result = registry.confirmRegistry();

        Boolean confirm = result.contains(successMsgAccount);
        System.out.println("Test pass: " + confirm);

    //AGREGAR LAS SUITES POR TIPO DE TEST
    }

    @Order(2)
    @Test
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
    @Order(3)
    @DisplayName("Visión general de la cuenta")
    public void testAccountSummary() {

        AccountSummary accountSummary = new AccountSummary();
        accountSummary.goToSummary();

        String result = accountSummary.confirm();

        Boolean confirm = result.contains(successMsgAccountSummary);
        System.out.println("Test pass: "+confirm);
    }

    @Test
    @Order(4)
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
    @Order(5)
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
