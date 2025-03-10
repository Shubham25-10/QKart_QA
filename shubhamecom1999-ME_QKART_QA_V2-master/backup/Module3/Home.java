package QKART_SANITY_LOGIN.Module1;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() throws InterruptedException {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
            Thread.sleep(2000);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            // SLEEP_STMT_10: Wait for Logout to complete
            // Wait for Logout to Complete
            Thread.sleep(3000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any errors
     */
    public Boolean searchForProduct(String product) {
        try {
            Thread.sleep(2000);
            WebElement searchBox = driver.findElement(
                    By.xpath("//body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/input[1]"));
            searchBox.clear();
            searchBox.sendKeys(product);
            Thread.sleep(3000);

            // WebDriverWait webDriverWait = new WebDriverWait(driver, 30);
            // webDriverWait.until(ExpectedConditions.or(
            //         ExpectedConditions.visibilityOfElementLocated(
            //                 By.xpath("//h4[text()='No products found']")),
            //         ExpectedConditions.visibilityOfElementLocated(
            //                 By.xpath("//div[@class='MuiCardContent-root css-1qw96cp']"))));
            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {};
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            searchResults = driver
                    .findElements(By.xpath("//div[@class='MuiCardContent-root css-1qw96cp']"));

            // search results
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // WebElement searchBar = driver.findElement(By.xpath("///div[2]/div[1]/input[1]"));
            // searchBar.clear();
            // searchBar.sendKeys("Gesundheit");
            // searchBar.sendKeys(Keys.ENTER);
            // = true if the element is *displayed* else set status = false
            WebElement noProductFound =
                    driver.findElement(By.xpath("//h4[text()='No products found']"));
            status = noProductFound.isDisplayed();
            WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
            webDriverWait.until(ExpectedConditions.visibilityOfAllElements(noProductFound));
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding to the
             * matching productName /* Iterate through each product on the page to find the
             * WebElement corresponding to the matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
            List<WebElement> product = driver.findElements(
                    By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 css-yg30e6']"));
            List<WebElement> addToCartButtons =
                    driver.findElements(By.xpath("//button[text()='Add to cart']"));

            for (int i = 0; i < product.size(); i++) {
                WebElement titleElement = product.get(i);
                String productText = titleElement.getText();
                if (productText.equals(productName)) {
                    WebElement addToCartButton = addToCartButtons.get(i);
                    addToCartButton.click();
                    Thread.sleep(5000);
                    return true;
                }
            }

            System.out.println("Unable to find the given product");
            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            WebElement checkoutButton = driver.findElement(By.xpath("//button[text()='Checkout']"));
            checkoutButton.click();
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName

            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)
            // List<WebElement> parentElemnts =
            //         driver.findElements(By.xpath("//div[@class='MuiBox-root css-1gjj37g']"));

            // for (WebElement parentElement : parentElemnts) {
            //     WebElement text = parentElement.findElement(By.xpath("./div[1]"));
            //     String productTitle = text.getText();
            //     if (productTitle.equals(productName)) {
            //         while (true) {
            //             WebElement countOfProduct =
            //                     parentElement.findElement(By.xpath("./div[2]/div[1]/div"));
            //             String countText = countOfProduct.getText();
            //             int actualCount = Integer.parseInt(countText);
            //             WebDriverWait webDriverWait = new WebDriverWait(driver, 8);
            //             if (quantity > actualCount) {
            //                 WebElement plusButton = parentElement
            //                         .findElement(By.xpath("./div[2]/div[1]/button[2]"));
            //                 plusButton.click();
            //                 //Thread.sleep(3000);
            //                 webDriverWait.until(ExpectedConditions.textToBePresentInElement(
            //                         parentElement.findElement(By.xpath("./div[2]/div[1]/div")),
            //                         String.valueOf(actualCount + 1)));
            //             } else if (quantity < actualCount) {
            //                 WebElement minusButton = parentElement
            //                         .findElement(By.xpath("./div[2]/div[1]/button[1]"));
            //                 minusButton.click();
            //                 //Thread.sleep(3000);
            //                 webDriverWait.until(ExpectedConditions.textToBePresentInElement(
            //                         parentElement.findElement(By.xpath("./div[2]/div[1]/div")),
            //                         String.valueOf(actualCount - 1)));
            //             } else if (quantity == actualCount) {
            //                 break;
            //             }
            //         }
            //     }
            // }
            // return false;

            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            int currentQty;
            for (WebElement item : cartContents) {
                if (productName.contains(item.findElement(By.xpath("//*[@class='MuiBox-root css-1gjj37g']/div[1]")).getText())) {
                    currentQty = Integer.valueOf(item.findElement(By.className("css-olyig7")).getText());

                    while (currentQty != quantity) {
                        if (currentQty < quantity) {
                            item.findElements(By.tagName("button")).get(1).click();
                         
                        } else {
                            item.findElements(By.tagName("button")).get(0).click();
                        }

                        synchronized (driver){
                            driver.wait(2000);
                        }

                        currentQty = Integer
                                .valueOf(item.findElement(By.xpath("//div[@data-testid=\"item-qty\"]")).getText());
                    }

                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {};
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(
                        cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                if (!actualCartContents.contains(expected)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
