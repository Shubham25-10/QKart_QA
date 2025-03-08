package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class QKART_Tests {

    static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
        driver.manage().window().maximize();
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType,
                    description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /*
     * Testcase01: Verify a new user can successfully register
     */
    @Test(priority = 1, description = "Verify registration happens correctly", groups = {"group1"})
    public void TestCase01() throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase1");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        Thread.sleep(3000);
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue("Failed to register new user", status);

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        Thread.sleep(2000);
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        Assert.assertTrue("Failed to login with registered user", status);

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

        logStatus("End TestCase", "Test Case 1: Verify user Registration : ",
                status ? "PASS" : "FAIL");
        takeScreenshot(driver, "EndTestCase", "TestCase1");
    }




    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s",
                String.valueOf(java.time.LocalDateTime.now()), type, message, status));
    }



    @Test(priority = 2, description = "Verify re-registering an already registered user fails", groups = {"group1"})
    public void TestCase02() throws InterruptedException {
        Boolean status;
        logStatus("Start Testcase",
                "Test Case 2: Verify User Registration with an existing username ", "DONE");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue("Registratiuon Failed", status);
        logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        // if (!status) {
        // logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" :
        // "FAIL");
        // return false;

        // }
        Assert.assertTrue("Registration Failed", status);
        lastGeneratedUserName = registration.lastGeneratedUsername;

        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);
        Assert.assertFalse("Re-Registratiuon is Successful", status);
        logStatus("End TestCase", "Test Case 2: Verify user Registration : ",
                status ? "FAIL" : "PASS");
        // return !status;
    }


    @Test(priority = 3, description = "Verify the functionality of search text box", groups = {"group1"})
    public void TestCase03() throws InterruptedException {
        logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the product "yonex" and assert the search status
        Thread.sleep(3000);
        boolean status = homePage.searchForProduct("yonex");
        

        List<WebElement> searchResults = homePage.getSearchResults();
        // Assert that search results are not empty

        // Verify that all search results contain the keyword "YONEX"
        for (WebElement webElement : searchResults) {
            SearchResult resultelement = new SearchResult(webElement);
            String elementText = resultelement.getTitleofResult();
            Assert.assertTrue(
                    "Test Case Failure. Search results contain unexpected values: " + elementText,
                    elementText.toUpperCase().contains("YONEX"));
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");
        Thread.sleep(2000);

        // Search for the product "Gesundheit" and assert the search status
        status = homePage.searchForProduct("Gesundheit");

        searchResults = homePage.getSearchResults();
        // Assert that no results are found
        if (searchResults.isEmpty()) {
            Assert.assertTrue(
                    "Test Case Failure. Expected 'No products found' message was not displayed",
                    homePage.isNoResultFound());
            logStatus("Step Success",
                    "Successfully validated that no products found message is displayed", "PASS");
            logStatus("TestCase 3",
                    "Test Case PASS. Verified that no search results were found for the given text",
                    "PASS");
        } else {
            Assert.fail("Test Case Failure. Expected: no results, but results were found.");
        }
    }

    @Test(priority = 4,
            description = "Verify the existence of size chart for certain items and validate contents of size chart", groups = {"group2"})
    public void TestCase04() throws InterruptedException {
        logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        Thread.sleep(4000);

        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists()) {
                logStatus("Step Success", "Successfully validated presence of Size Chart Link",
                        "PASS");

                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                logStatus("Step Success", "Validated presence of drop down",
                        status ? "PASS" : "FAIL");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody,
                            driver)) {
                        logStatus("Step Success",
                                "Successfully validated contents of Size Chart Link", "PASS");
                    } else {
                        logStatus("Step Failure",
                                "Failure while validating contents of Size Chart Link", "FAIL");
                        status = false;
                    }

                    // Close the size chart modal
                    status = result.closeSizeChart(driver);

                } else {
                    logStatus("TestCase 4", "Test Case Fail. Failure to open Size Chart", "FAIL");
                    Assert.fail("Test Case Failure: Failure to open Size Chart");
                }

            } else {
                logStatus("TestCase 4", "Test Case Fail. Size Chart Link does not exist", "FAIL");
                Assert.fail("Test Case Failure: Size Chart Link does not exist");
            }

            // Assert that the product title contains "RUNNING SHOES" in uppercase
            String productTitle = result.getTitleofResult();
            Assert.assertTrue(
                    "Test Case Failure. Product title does not contain 'RUNNING SHOES' in uppercase: "
                            + productTitle,
                    productTitle.toUpperCase().contains("RUNNING SHOES".toUpperCase()));
        }

        logStatus("TestCase 4", "End Test Case: Validated Size Chart Details",
                status ? "PASS" : "FAIL");
        Assert.assertTrue("Test Case Failure. Test did not pass successfully", status);
        // return status;
    }

    @Test(priority = 5,
            description = "Verify that a new user can add multiple products in to the cart and Checkout", groups = {"group1"})
    @Parameters({"address", "product_1", "product_2"})
    public void TestCase05(String address, String product_1, String product_2)
            throws InterruptedException {
        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        Boolean status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
        // }

        lastGeneratedUserName = registration.lastGeneratedUsername;
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" :
        // "FAIL");
        // }

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart(product_1);
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart(product_2);
        homePage.clickCheckout();
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress(address);
        checkoutPage.placeOrder();
        WebDriverWait wait = new WebDriverWait(driver, 30L);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        status = driver.getCurrentUrl().endsWith("/thanks");
        Assert.assertTrue("URL Doen Not End's with /thanks", status);
        homePage.navigateToHome();
        homePage.PerformLogout();
        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ",
                status ? "PASS" : "FAIL");
        // return status;
    }

    @Test(priority = 6, description = "Verify that the contents of the cart can be edited", groups = {"group2"})
    @Parameters({"productName1", "productName2"})
    public void TestCase06(String productName1, String productName2) throws InterruptedException {
        logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");

        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        // Register user
        registration.navigateToRegisterPage();
        Boolean status = registration.registerUser("testUser", "abc@123", true);
        // Assert.assertTrue("User registration failed", status);

        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Login user
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // Assert.assertTrue("User login failed", status);

        // Navigate to home page and add products to cart
        homePage.navigateToHome();
        status = homePage.searchForProduct("Xtend");
        homePage.addProductToCart(productName1);

        status = homePage.searchForProduct("Yarine");
        homePage.addProductToCart(productName2);

        // Edit cart
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

        // Proceed to checkout
        homePage.clickCheckout();
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.placeOrder();

        // Wait for order confirmation
        WebDriverWait wait = new WebDriverWait(driver, 30);
        try {
            wait.until(
                    ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            Assert.fail("Order confirmation URL not reached within timeout: " + e.getMessage());
        }

        // Validate order confirmation page
        status = driver.getCurrentUrl().endsWith("/thanks");
        Assert.assertTrue("Order confirmation URL does not end with '/thanks'", status);

        // Logout
        homePage.navigateToHome();
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 6: Verify that cart can be edited", "PASS");
    }

    @Test(priority = 7,
            description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", groups = {"group1"})
    @Parameters({"TC7_ProductName", "TC7_Qty"})
    public void TestCase07(String TC7_ProductName, int TC7_Qty) throws InterruptedException {
        logStatus("Start TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        Boolean status = registration.registerUser("testUser", "abc@123", true);

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and add to cart
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart(TC7_ProductName);

        // Attempt to add a high quantity to simulate insufficient balance
        homePage.changeProductQuantityinCart(TC7_ProductName, TC7_Qty);
        homePage.clickCheckout();

        // Checkout and place order
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        Thread.sleep(2000);
        checkoutPage.placeOrder();

        // Verify insufficient balance error message
        // Thread.sleep(2000);
        status = checkoutPage.verifyInsufficientBalanceMessage();
        Assert.assertTrue("Insufficient balance error message not displayed", status);

        logStatus("End TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "PASS");
    }

    @Test(priority = 8,
            description = "Verify that a product added to a cart is available when a new tab is added", groups = {"group2"})
    public void TestCase08() throws InterruptedException {
        Boolean status = false;
        logStatus("Start TestCase",
                "Test Case 8: Verify that product added to cart is available when a new tab is opened",
                "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase09");
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("TestCase 8", "Test Case Failure. Verify that product added to cart is
        // available when a new tab is opened", "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase09");
        // }

        lastGeneratedUserName = registration.lastGeneratedUsername;
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase9");
        // logStatus("End TestCase", "Test Case 8: Verify that product added to cart is available
        // when a new tab is opened", status ? "PASS" : "FAIL");
        // }

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        String currentURL = driver.getCurrentUrl();
        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(((String[]) handles.toArray(new String[handles.size()]))[1]);
        driver.get(currentURL);
        Thread.sleep(2000L);
        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);
        Assert.assertTrue("Message not found", status);
        driver.close();
        driver.switchTo().window(((String[]) handles.toArray(new String[handles.size()]))[0]);
        logStatus("End TestCase",
                "Test Case 8: Verify that product added to cart is available when a new tab is opened",
                status ? "PASS" : "FAIL");
        takeScreenshot(driver, "EndTestCase", "TestCase08");
        // return status;
    }

    @Test(priority = 9,
            description = "Verify that privacy policy and about us links are working fine", groups = {"group2"})
    public void TestCase09() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        Boolean status = false;

        logStatus("Start TestCase",
                "Test Case 09: Verify that the Privacy Policy, About Us are displayed correctly",
                "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase09");

        // Register the user and verify registration status
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);

        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Log in and verify login status
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        // Navigate to home and check Privacy Policy link behavior
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);
        softAssert.assertTrue(status, "Parent page URL changed on Privacy Policy link click");

        // Switch to new tab and verify Privacy Policy page heading
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(((String[]) handles.toArray(new String[handles.size()]))[1]);
        WebElement privacyPolicyHeading =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = privacyPolicyHeading.getText().equals("Privacy Policy");
        softAssert.assertTrue(status, "Privacy Policy page heading is incorrect");

        // Switch back and verify Terms of Service link behavior
        driver.switchTo().window(((String[]) handles.toArray(new String[handles.size()]))[0]);
        driver.findElement(By.linkText("Terms of Service")).click();
        handles = driver.getWindowHandles();
        driver.switchTo().window(((String[]) handles.toArray(new String[handles.size()]))[2]);
        WebElement tosHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = tosHeading.getText().equals("Terms of Service");
        softAssert.assertTrue(status, "Terms of Service page heading is incorrect");

        // Close windows and return to the original window
        driver.close();
        driver.switchTo().window(((String[]) handles.toArray(new String[handles.size()]))[1])
                .close();
        driver.switchTo().window(((String[]) handles.toArray(new String[handles.size()]))[0]);

        // Final test logging
        logStatus("End TestCase",
                "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly",
                "PASS");
        takeScreenshot(driver, "EndTestCase", "TestCase9");

        // Assert all soft asserts
        softAssert.assertAll();
    }

    @Test(priority = 10, description = "Verify that the contact us dialog works fine", groups = {"group2"})
    public void TestCase10() throws InterruptedException {
        logStatus("Start TestCase",
                "Test Case 10: Verify that contact us option is working correctly", "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase10");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Click on 'Contact us' option and verify element visibility
        driver.findElement(By.xpath("//*[text()='Contact us']")).click();
        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        Assert.assertTrue("Name input field is not visible", name.isDisplayed());

        // Input Name
        name.sendKeys("crio user");

        // Input Email and verify field is displayed
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        Assert.assertTrue("Email input field is not visible", email.isDisplayed());
        email.sendKeys("criouser@gmail.com");

        // Input Message and verify field is displayed
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        Assert.assertTrue("Message input field is not visible", message.isDisplayed());
        message.sendKeys("Testing the contact us page");

        // Click on the 'Contact Us' submit button and verify it becomes invisible after submission
        WebElement contactUs = driver.findElement(By.xpath(
                "/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));
        Assert.assertTrue("Contact Us button is not visible", contactUs.isDisplayed());
        contactUs.click();

        // Wait for the button to become invisible, indicating the form was submitted
        WebDriverWait wait = new WebDriverWait(driver, 30);
        boolean isButtonInvisible = wait.until(ExpectedConditions.invisibilityOf(contactUs));
        Assert.assertTrue("Contact Us submission failed, button is still visible",
                isButtonInvisible);

        logStatus("End TestCase",
                "Test Case 10: Verify that contact us option is working correctly", "PASS");
        takeScreenshot(driver, "EndTestCase", "TestCase10");
    }

    @Test(priority = 11,
            description = "Ensure that the Advertisement Links on the QKART page are clickable", groups = {"group1"})
    public void TestCase11() throws InterruptedException {
        Boolean status = false;

        logStatus("Start TestCase",
                "Test Case 11: Ensure that the links on the QKART advertisement are clickable",
                "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase11");

        // Registration
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);

        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Login
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        // Add product to cart and proceed to checkout
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("YONEX Smash Badminton Racquet");

        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.clickCheckout();

        // Checkout process
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
        Thread.sleep(3000);
        checkoutPage.placeOrder();
        Thread.sleep(3000);
        String currentURL = driver.getCurrentUrl();

        // Verify number of advertisements
        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));
        status = Advertisements.size() == 3;
        logStatus("Step", "Verify that 3 Advertisements are available", status ? "PASS" : "FAIL");
        Assert.assertTrue("Expected 3 advertisements, found: " + Advertisements.size(), status);

        // Verify Advertisement 1 is clickable
        WebElement Advertisement1 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        Thread.sleep(3000);
        driver.switchTo().parentFrame();
        Thread.sleep(3000);
        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step", "Verify that Advertisement 1 is clickable", status ? "PASS" : "FAIL");
        Assert.assertTrue("Advertisement 1 is not clickable", status);

        // Return to original page URL
        driver.get(currentURL);
        Thread.sleep(3000L);

        // Verify Advertisement 2 is clickable
        WebElement Advertisement2 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();
        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step", "Verify that Advertisement 2 is clickable", status ? "PASS" : "FAIL");
        Assert.assertTrue("Advertisement 2 is not clickable", status);

        logStatus("End TestCase",
                "Test Case 11: Ensure that the links on the QKART advertisement are clickable",
                status ? "PASS" : "FAIL");

        // Final assert to confirm all steps passed
        Assert.assertTrue("Final Test Case Status Failed", status);
    }



@AfterSuite
public static void quitDriver() {
    System.out.println("quit()");
    // if (driver != null) {
    // driver.quit();
    // }
    driver.quit();
}

}