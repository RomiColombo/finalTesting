package pruebasBackEnd;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestBack {

    private String userName = "romicolombo";
    private String password = "12345";
    private static String sessionId;
    private static String customerId;
    private static String accountId1;
    private static String accountId2;

    private String successMsgAccount = "ParaBank | Customer Created";

    private String successMsgAccountSummary = "ParaBank | Accounts Overview";

    private String ammountToTransfer = "300";

    @Test
    @Order(1)
    @Tag("Smoke")
    @DisplayName("Proceso de registro")
     public void testRegistry(){

        sessionId = given()
                    .when()
                        .get("https://parabank.parasoft.com/parabank/register.htm").sessionId().toString();

        System.out.println("Sessión creada para el registro "+sessionId);
        given()
            .cookie("JSESSIONID",sessionId)
            .contentType("application/x-www-form-urlencoded")
            .formParam("customer.firstName","Romina")
            .formParam("customer.lastName","Colombo")
            .formParam("customer.address.street","Mi casa 123")
            .formParam("customer.address.city","Corrientes")
            .formParam("customer.address.state","Argentina")
            .formParam("customer.address.zipCode","3400")
            .formParam("customer.phoneNumber","123456")
            .formParam("customer.ssn","123")
            .formParam("customer.username",userName)
            .formParam("customer.password",password)
            .formParam("repeatedPassword",password)
        .when()
            .post("https://parabank.parasoft.com/parabank/register.htm")
        .then()
            .body("html.head.title", equalTo(successMsgAccount));
    }

    @Test
    @Order(2)
    @Tag("Regression")
    @DisplayName("Proceso de login")
    public void testLogin(){

        sessionId  = given()
                        .formParam("username",userName)
                        .formParam("password",password)
                        .accept(ContentType.JSON)
                    .when()
                        .post("https://parabank.parasoft.com/parabank/login.htm")
                    .then()
                        .statusCode(302).log().all().extract().sessionId();
        customerId = given()
                        .pathParam("username",userName)
                        .pathParam("password",password)
                        .accept(ContentType.JSON)
                    .when()
                        .get("https://parabank.parasoft.com/parabank/services/bank/login/{username}/{password}")
                    .then()
                        .statusCode(200).log().all()
                        .extract().path("id").toString();
        System.out.println("customerId: "+customerId);
        System.out.println("sessionId: "+sessionId);
    }

    @Test
    @Order(3)
    @Tag("Regression")
    @DisplayName("Apertura de una nueva cuenta")
    public void testNewAccount(){

        Response response1 = given()
            .cookie("JSESSIONID", sessionId)
            .pathParam("customerId", customerId)
            .accept(ContentType.JSON)
        .when()
            .get("https://parabank.parasoft.com/parabank/services_proxy/bank/customers/{customerId}/accounts")
        .then()
            .statusCode(200).log().all().extract().response();

        accountId1 = response1.jsonPath().getString("id[0]");
        Response response2 = given()
            .cookie("JSESSIONID", sessionId)
            .formParam("customerId",customerId)
            .formParam("newAccountType","1")
            .formParam("fromAccountId",accountId1)
            .accept(ContentType.JSON)
        .when()
            .post("https://parabank.parasoft.com/parabank/services_proxy/bank/createAccount")
        .then()
            .statusCode(200).log().all().extract().response();

        accountId2 = response2.jsonPath().getString("id");


        System.out.println("Account 1: "+accountId1);
        System.out.println("Account 2: "+accountId2);

    }

    @Test
    @Order(4)
    @Tag("Regression")
    @DisplayName("Resumen de las cuentas")
    public void testAccountSummary() {

        given()
                .cookie("JSESSIONID", sessionId)
        .when()
                .get("https://parabank.parasoft.com/parabank/overview.htm")

                .then()
                .body("html.head.title", equalTo(successMsgAccountSummary));
    }

    @Test
    @Order(5)
    @Tag("Regression")
    @DisplayName("Transferencia de fondos")
      public void testTransferFounds(){

        given()
            .cookie("JSESSIONID", sessionId)
            .formParam("fromAccountId",accountId1)
            .formParam("toAccountId",accountId2)
            .formParam("amount",ammountToTransfer)
        .when()
            .post("https://parabank.parasoft.com/parabank/services_proxy/bank/transfer")
        .then()
            .statusCode(200).log().all();
    }

    @Test
    @Order(6)
    @Tag("Regression")
    @DisplayName("Actividad de la cuenta")
    public void testAccountActivity(){

        given()
            .cookie("JSESSIONID", sessionId)
            .pathParam("account",accountId1)
            .accept(ContentType.JSON)
        .when()
            .get("https://parabank.parasoft.com/parabank/services_proxy/bank/accounts/{account}/transactions/month/All/type/All")
        .then()
            .statusCode(200).log().all();
    }

}