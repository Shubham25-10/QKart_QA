package QKART_TESTNG.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
            WebElement addNewAddressButton = driver.findElement(By.id("add-new-btn"));
            Thread.sleep(3000);
            addNewAddressButton.click();
            Thread.sleep(5000);
            WebElement AddressBox = driver.findElement(By.className("MuiOutlinedInput-input"));
            Thread.sleep(3000);
            AddressBox.clear();
            Thread.sleep(3000);
            AddressBox.sendKeys(addresString);
            Thread.sleep(5000);
            List<WebElement> buttons = driver.findElements(By.className("css-177pwqq"));
            Thread.sleep(3000);
            for (WebElement button : buttons) {
                if (button.getText().equals("ADD")) {
                    button.click();
                    Thread.sleep(5000);
                    WebDriverWait wait = new WebDriverWait(driver, 30);
                    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(String.format(
                            "//*[@class='MuiTypography-root MuiTypography-body1 css-yg30e6' and text()='%s']",
                            addresString))));
                    Thread.sleep(2000);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */
            WebElement address = driver.findElementByXPath(
                    String.format("//p[@class = 'MuiTypography-root MuiTypography-body1 css-yg30e6' and text()= '%s']",
                            addressToSelect));
                            Thread.sleep(3000);
            address.click();
            Thread.sleep(5000);
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            WebElement placeOrder = driver.findElementByXPath("//button[text()='PLACE ORDER']");
            
            placeOrder.click();
            // Thread.sleep(2000);
            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            Thread.sleep(1000);
           WebElement alertMessage = driver.findElement(By.id("notistack-snackbar"));
            boolean alert = alertMessage.isDisplayed();
            System.out.println("Alert is appearing: "+ alert);
            if (alert) {
                System.out.println("Alert is appearing: "+ alert);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
