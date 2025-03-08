package QKART_SANITY_LOGIN.Module4;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
       
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
        //    
        try {
            /*
             * Click on the "Add new address" button, enter the addressString in the address text
             * box and click on the "ADD" button to save the address
             */
            WebElement addressButton = driver.findElement(By.id("add-new-btn"));
            addressButton.click();
            WebElement textArea = driver.findElement(
                    By.xpath("//textarea[@placeholder='Enter your complete address']"));
            textArea.sendKeys(addresString);
            WebElement addButton = driver.findElement(By.xpath("//button[text()='Add']"));
            addButton.click();
            Thread.sleep(3000);
            WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
            webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
                    "//div[@class='address-item not-selected MuiBox-root css-0']/div[1]/p")));
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching text,
             * addressToSelect and click on it
             */
            List<WebElement> selectAddress = driver.findElements(By
                    .xpath("//div[@class='address-item not-selected MuiBox-root css-0']/div[1]/p"));
            for (WebElement addressElement : selectAddress) {
                String actualAddress = addressElement.getText();
                if (actualAddress.equals(addressToSelect)) {
                    addressElement.click();
                    return true;
                }
            }
            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println(
                    "Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }


    }

    /*
     * Return Boolean denoting the status of place order action
     */
    // TODO: CRIO_TASK_MODULE_XPATH - M1_2 Update locators to use Xpath
    public Boolean placeOrder() {
        try {
            // Find the "PLACE ORDER" button and click on it
            List<WebElement> elements = driver.findElements(By.xpath("(//button[@type = 'button'])[3]"));
            for (WebElement element : elements) {
                if (element.getText().equals("PLACE ORDER")) {
                    Thread.sleep(3000);
                    element.click();
                    return true;
                }
            }
            return false;

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
            WebElement alertMessage = driver.findElement(By.id("notistack-snackbar"));
            if (alertMessage.isDisplayed()) {
                if (alertMessage.getText().equals("You do not have enough balance in your wallet for this purchase")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
