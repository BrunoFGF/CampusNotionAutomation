import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CampusScraperTest {
    private WebDriver driver;
    private String username = Config.getProperty("campus.username");
    private String password = Config.getProperty("campus.password");
    private String loginUrl = Config.getProperty("campus.login.url");
    private String dashboardUrl = Config.getProperty("campus.dashboard.url");

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome", "C:\\Users\\Bruno\\Desktop\\Chrome Driver\\chrome-win64\\chrome.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLogin() {
        driver.get(loginUrl);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginbtn")).click();

        assertNotEquals(loginUrl, driver.getCurrentUrl(), "Login failed, URL didn't change.");
    }

    @Test
    public void testNavigateToDashboard() {
        driver.get(loginUrl);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginbtn")).click();

        driver.get(dashboardUrl);

        assertEquals(dashboardUrl, driver.getCurrentUrl(), "Failed to navigate to dashboard.");
    }

    @Test
    public void testScrapeTasks() {
        driver.get(loginUrl);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginbtn")).click();

        driver.get(dashboardUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-region='event-list-wrapper']")));

        List<WebElement> taskElements = driver.findElements(By.cssSelector("div[data-region='event-list-item']"));
        assertFalse(taskElements.isEmpty(), "No tasks found on the dashboard.");

        for (WebElement element : taskElements) {
            WebElement taskLink = element.findElement(By.cssSelector("div.event-name-container a"));
            taskLink.click();
            assertNotEquals(dashboardUrl, driver.getCurrentUrl(), "Failed to navigate to task details.");
            driver.navigate().back();
            assertEquals(dashboardUrl, driver.getCurrentUrl(), "Failed to return to dashboard.");
        }
    }

    @Test
    public void testScrapeTasksGetInformation() {
        driver.get(loginUrl);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginbtn")).click();

        driver.get(dashboardUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-region='event-list-wrapper']")));

        List<WebElement> taskElements = driver.findElements(By.cssSelector("div[data-region='event-list-item']"));
        assertFalse(taskElements.isEmpty(), "No tasks found on the dashboard.");

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, h:mm a", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        for (WebElement element : taskElements) {
            WebElement taskLink = element.findElement(By.cssSelector("div.event-name-container a"));
            String taskUrl = taskLink.getAttribute("href");

            taskLink.click();

            /****Get information from elements****/
            //Tast name
            WebElement taskNameElement = driver.findElement(By.cssSelector("div.page-header-headings h1.h2"));
            String taskName = taskNameElement.getText();

            //Task due date
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-region='activity-information']//div[@data-region='activity-dates']")));
            WebElement dueDateElement = driver.findElement(By.xpath("//div[@data-region='activity-information']//div[@data-region='activity-dates']//div[strong[text()='Due:']]"));
            String dueDateText = dueDateElement.getText().replace("Due:", "").trim();

            dueDateText = dueDateText.replaceAll("\\s+", " ").trim();
            System.out.println("Due Date Text: " + dueDateText); // Añadir esta línea para depurar

            LocalDateTime dueDate = LocalDateTime.parse(dueDateText, inputFormatter);
            String formattedDueDate = dueDate.format(outputFormatter);

            //Task subject
            WebElement taskSubjectElement = driver.findElement(By.id("course-header-banner-text"));
            String taskSubject = taskSubjectElement.getText();

            //Prints
            System.out.println("Task Name: " + taskName);
            System.out.println("Task Link: " + taskUrl);
            System.out.println("Task Due Date: " + formattedDueDate);
            System.out.println("Task Subject: " + taskSubject);

            assertNotEquals(dashboardUrl, driver.getCurrentUrl(), "Failed to navigate to task details.");

            driver.navigate().back();

            assertEquals(dashboardUrl, driver.getCurrentUrl(), "Failed to return to dashboard.");
        }
    }
}
