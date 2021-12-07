package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class ExceptionTests {

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
        //driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        //
    }

    @Test (priority = 1)
    public void notVisibleTest(){

        System.out.println("<==== Starting login test");

        // open test page
        String url = "https://the-internet.herokuapp.com/dynamic_loading/1";
        driver.get(url);
        System.out.println("<==== The page is opened");

        //explicit waiter
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait.until(ExpectedConditions.elementToBeClickable(startButton));

        // click on start button
        WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
        startButton.click();

        //find title element and test 'Hello World!'
        WebElement title = driver.findElement(By.xpath("//div[@id='finish']/h4[.='Hello World!']"));
        wait.until(ExpectedConditions.visibilityOf(title));
        String actualTitle = title.getText();

        //  check result
        String expectedMessage = "Hello World!";
        Assert.assertEquals(actualTitle,expectedMessage,"Actual title is not the same as expected");

    }

    @Test(priority = 2)
    public void timeOutTest(){

        System.out.println("<==== Starting login test");

        // open test page
        String url = "https://the-internet.herokuapp.com/dynamic_loading/1";
        driver.get(url);
        System.out.println("<==== The page is opened");

        //explicit waiter
        WebDriverWait wait = new WebDriverWait(driver, 2);
        //wait.until(ExpectedConditions.elementToBeClickable(startButton));

        // click on start button
        WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
        startButton.click();

        //find title element and test 'Hello World!'
        WebElement title = driver.findElement(By.xpath("//div[@id='finish']/h4[.='Hello World!']"));
        try {
            wait.until(ExpectedConditions.visibilityOf(title));
        }catch (TimeoutException e){
            e.printStackTrace();
            System.out.println("Exception caught: "+ e.getMessage());
            sleep(3000);
        }
        String actualTitle = title.getText();

        //  check result
        String expectedMessage = "Hello World!";
        Assert.assertEquals(actualTitle,expectedMessage,"Actual title is not the same as expected");

    }

    @Test(priority = 3)
    public void noSuchElement(){

        System.out.println("<==== Starting login test");

        // open test page
        String url = "https://the-internet.herokuapp.com/dynamic_loading/2";
        driver.get(url);
        System.out.println("<==== The page is opened");

        //explicit waiter
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait.until(ExpectedConditions.elementToBeClickable(startButton));

        // click on start button
        WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
        startButton.click();

        //find title element and text 'Hello World!'
        Assert.assertTrue(wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), "Hello World!")),
                "Could not verify expected test 'Hello World!'");
//        WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='finish']/h4[.='Hello World!']")));
//        String actualTitle = title.getText();

        //  check result
//        String expectedMessage = "Hello World!";
//        Assert.assertEquals(actualTitle,expectedMessage,"Actual title is not the same as expected");

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
