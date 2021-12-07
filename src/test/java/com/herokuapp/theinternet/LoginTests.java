package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class LoginTests {

    private WebDriver driver;

    @Parameters({ "browser" })
    @BeforeMethod(alwaysRun = true)
    private void setUp(@Optional("chrome") String browser){
        //Create driver
        switch (browser){
            case "chrome":
                System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver");
                driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver","src/main/resources/geckodriver");
                driver = new FirefoxDriver();
                break;

            default:
                System.out.println("Do not know how to start " + browser + "started chrome instead");
                System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver");
                driver = new ChromeDriver();
                break;
        }

        //sleep for 3 second
        sleep(3000);

        //maximize full screen window
        driver.manage().window().maximize();

        //implicit wait
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //
    }

    @Test(priority = 1,groups = { "positiveTests", "smokeTests" })
    public void positiveLoginTest(){

        System.out.println("<==== Starting login test");

        // open test page
        String url = "http://the-internet.herokuapp.com/login";
        driver.get(url);
        System.out.println("<==== The page is opened");

        //sleep for 2 second
        sleep(2000);

        //enter username
        WebElement username = driver.findElement(By.xpath("//input[@id='username']"));
        username.sendKeys("tomsmith");

        // enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("SuperSecretPassword!");

        //explicit waiter
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // click on login button
        WebElement loginButton = driver.findElement(By.tagName("button"));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        // verification:

        //  new url
        String expectedUrl = "http://the-internet.herokuapp.com/secure";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl,expectedUrl,"Actual URL is not the same as expected");

        //  logout button is visible
        WebElement logoutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
        Assert.assertTrue(logoutButton.isDisplayed(),"The Logout button is not found on page");

        //  successful login message
        WebElement successMessage = driver.findElement(By.cssSelector("div#flash"));
            //By.xpath("//div[@id='flash']")
        String expectedMessage = "You logged into a secure area!";
        String actualMessage = successMessage.getText();

        //Assert.assertEquals(actualMessage, expectedMessage,"Text is wrong");
        Assert.assertTrue(actualMessage.contains(expectedMessage),"Actual message does not contain expected text.\nActualMessage: " + actualMessage + "\nExpectedMessage: " + expectedMessage);

        // close browser
    }



    @Parameters({ "username", "password", "expectedMessage" })
    @Test(priority = 2,groups = { "negativeTests", "smokeTests" })
    public void negativeLoginTest(String username, String password, String expectedMessage){
        //1. Create driver

        //2. maximize to full screen
        driver.manage().window().maximize();

        //3. open test page
        String url = "http://the-internet.herokuapp.com/login";
        driver.get(url);

        //4. Enter username
        WebElement usernameElement = driver.findElement(By.xpath("//input[@id='username']"));
        //username.sendKeys("incorrectUserName");
        usernameElement.sendKeys(username);

        //5. enter password
        WebElement passwordElement = driver.findElement(By.xpath("//input[@id='password']"));
        passwordElement.sendKeys(password);

        //6. click on Login button
        WebElement loginButton = driver.findElement(By.xpath("//button[@class='radius']"));
        loginButton.click();

        //7. check validation message
        WebElement validationMessageText = driver.findElement(By.xpath("//div[@id='flash']"));

        //8. Validate that message is incorrect
        String expectedValidationErrorMessage = expectedMessage;
        String actualValidationErrorMessage = validationMessageText.getText();
        Assert.assertTrue(actualValidationErrorMessage.contains(expectedValidationErrorMessage), "Text is not the same as actual.\nActual message :" + actualValidationErrorMessage + "\nExpected: " + expectedValidationErrorMessage);


    }

    @Parameters({ "username", "password", "expectedMessage" })
    @Test(priority = 3, groups = { "negativeTests" })
    public void negativePassword(String username, String password, String expectedMessage){
        //1. create driver

        //2. maximize to full screen
        driver.manage().window().maximize();

        //3. open test page
        String url = "http://the-internet.herokuapp.com/login";
        driver.get(url);

        //4. enter username
        WebElement usernameElement = driver.findElement(By.id("username"));
        usernameElement.sendKeys(username);

        //5. enter password
        WebElement userPassword = driver.findElement(By.name("password"));
        userPassword.sendKeys(password);

        //click on Login button
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // find error message
        WebElement errorPasswordMessage = driver.findElement(By.id("flash-messages"));

        //validate that message is incorrect
        String expectedErrorMessage = expectedMessage;
        String actualErrorMessage = errorPasswordMessage.getText();
        Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),"Error message is incorrect /nExpected message: "+ expectedErrorMessage + "\nActual message: "+ actualErrorMessage);

    }

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod(alwaysRun = true)
    private void tearDown() {
        driver.quit();
    }
}
