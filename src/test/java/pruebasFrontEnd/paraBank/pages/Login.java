package pruebasFrontEnd.paraBank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pruebasFrontEnd.base.BasePage;

import java.time.Duration;

public class Login extends BasePage {

    WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofMillis(1000));

    protected static final String btnLogout = "//*[@id=\"leftPanel\"]/ul/li[8]/a";

    protected static final String btnLogin = "//*[@id=\"loginPanel\"]/form/div[3]/input";

    protected static final String inputUsername = "//*[@id=\"loginPanel\"]/form/div[1]/input";

    protected static final String inputPassword = "//*[@id=\"loginPanel\"]/form/div[2]/input";

    protected static final String success = "//*[@id=\"leftPanel\"]/p";


    public void login(String user, String password) {

        try{
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(btnLogout)));
            WebElement logoutBtn = getWebElement(By.xpath(btnLogout));
            logoutBtn.click();

        }catch (Exception e){
            System.out.println("No se pudo cerrar la sesion");
        }

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(inputUsername)));
        WebElement usernameInput = getWebElement(By.xpath(inputUsername));
        usernameInput.sendKeys(user);

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(inputPassword)));
        WebElement passwordInput = getWebElement(By.xpath(inputPassword));
        passwordInput.sendKeys(password);

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(btnLogin)));
        WebElement logginBtn = getWebElement(By.xpath(btnLogin));
        logginBtn.click();
    }

    public String confirmLogin(){
        WebElement message = getWebElement(By.xpath(success));
        return message.getText();
    }

}
