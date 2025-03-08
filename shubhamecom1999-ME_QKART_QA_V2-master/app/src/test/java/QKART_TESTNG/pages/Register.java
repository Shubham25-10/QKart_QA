package QKART_TESTNG.pages;

import java.sql.Timestamp;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Register {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/register";
    public String lastGeneratedUsername = "";

    public Register(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToRegisterPage() {
        if (!driver.getCurrentUrl().equals(this.url)) {
            driver.get(this.url);
        }
    }

    public void clearTextbox(WebElement textBox) {
        new Actions(this.driver).click(textBox).keyDown(Keys.CONTROL).sendKeys("a")
                .keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).perform();
    }

    public Boolean registerUser(String Username, String Password, Boolean makeUsernameDynamic)
            throws InterruptedException {
                
        // Find the Username Text Box
        WebElement username_txt_box = this.driver.findElement(By.id("username"));

        // Get time stamp for generating a unique username
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String test_data_username;
        if (makeUsernameDynamic)
            // Concatenate the timestamp to string to form unique timestamp
            test_data_username = Username + "_" + String.valueOf(timestamp.getTime());
        else
            test_data_username = Username;

        // Type the generated username in the username field
        this.clearTextbox(username_txt_box);
        
        username_txt_box.sendKeys(test_data_username);
        
        // Find the password Text Box

        WebElement password_txt_box = this.driver.findElement(By.id("password"));
        // Thread.sleep(3000
        String test_data_password = Password;
        // Thread.sleep(5000);
        // Enter the Password value
        this.clearTextbox(password_txt_box);
        Thread.sleep(3000);
        password_txt_box.sendKeys(test_data_password);
        Thread.sleep(5000);

        WebElement confirm_password_txt_box = this.driver.findElement(By.id("confirmPassword"));
                Thread.sleep(3000);
        // Enter the Confirm Password Value
        this.clearTextbox(confirm_password_txt_box);
        Thread.sleep(3000);
        confirm_password_txt_box.sendKeys(test_data_password);
        Thread.sleep(3000);
            // Thread.sleep(5000);
        // Find the register now button
        WebElement register_now_button = this.driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/button[1]"));
        // Click the register now button
        Thread.sleep(3000);
        register_now_button.click();
        Thread.sleep(5000);

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login")));
        } catch (TimeoutException e) {
            return false;
        }

        this.lastGeneratedUsername = test_data_username;

        return this.driver.getCurrentUrl().endsWith("/login");
    }
}
