import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CampusScraper {
    private WebDriver driver;
    private String username = Config.getProperty("campus.username");
    private String password = Config.getProperty("campus.password");
    private String loginUrl = Config.getProperty("campus.login.url");
    private String dashboardUrl = Config.getProperty("campus.dashboard.url");
    private String chromeDriverUrl = Config.getProperty("chrome.driver.url");

    public List<Task> scrapeTasks(){
        List<Task> tasks = new ArrayList<>();

        System.setProperty("webdriver.chrome", chromeDriverUrl);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        try {
            driver.get(loginUrl);
            driver.findElement(By.id("username")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("loginbtn")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            driver.get(dashboardUrl);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-region='event-list-wrapper']")));

            List<WebElement> taskElements = driver.findElements(By.cssSelector("div[data-region='event-list-item']"));

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, h:mm a", Locale.ENGLISH);

            for (WebElement element : taskElements) {
                try {
                    WebElement taskLink = element.findElement(By.cssSelector("div.event-name-container a"));
                    String taskUrl = taskLink.getAttribute("href");

                    ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])", taskUrl);
                    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                    driver.switchTo().window(tabs.get(1));

                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.page-header-headings h1.h2")));

                    String taskName;
                    try {
                        WebElement firstTaskNameElement = driver.findElement(By.cssSelector("div.page-header-headings h1.h2"));
                        taskName = firstTaskNameElement.getText();
                    } catch (Exception e) {
                        driver.close();
                        driver.switchTo().window(tabs.get(0));
                        continue;
                    }

                    String dueDateText;
                    try {
                        WebElement dueDateElement = driver.findElement(By.xpath("//div[@data-region='activity-information']//div[@data-region='activity-dates']//div[strong[text()='Due:']]"));
                        dueDateText = dueDateElement.getText().replace("Due:", "").trim().replaceAll("\\s+", " ").trim();
                    } catch (Exception e) {
                        driver.close();
                        driver.switchTo().window(tabs.get(0));
                        continue;
                    }

                    LocalDateTime taskDueDate = LocalDateTime.parse(dueDateText, inputFormatter);

                    String taskSubject;
                    try {
                        taskSubject = driver.findElement(By.id("course-header-banner-text")).getText();
                    } catch (Exception e) {
                        driver.close();
                        driver.switchTo().window(tabs.get(0));
                        continue;
                    }

                    tasks.add(new Task(taskName, taskDueDate, taskSubject, taskUrl));

                    driver.close();
                    driver.switchTo().window(tabs.get(0));
                } catch (Exception e) {
                    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                    driver.switchTo().window(tabs.get(1));
                    driver.close();
                    driver.switchTo().window(tabs.get(0));
                }
            }
        } finally {
            driver.quit();
        }
        return tasks;
    }
}