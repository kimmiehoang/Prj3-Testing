import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;

public class GradeTestingTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "E://chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 40);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGrades() {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("E://Prj3-Testing/test_data.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String username = data[0];
                String password = data[1];
                String score = data[2];
                String expectedLetterGrade = data[3];

                performTest(username, password, score, expectedLetterGrade);
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void performTest(String username, String password, String score, String expectedLetterGrade) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.get("https://school.moodledemo.net/?lang=en");

        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log in")));
        loginLink.click();

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameInput.clear();
        usernameInput.sendKeys(username);

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.id("loginbtn"));
        loginButton.click();

        WebElement groupingDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("groupingdropdown")));
        groupingDropdown.click();

        WebElement allOption = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("All")));
        allOption.click();

        WebElement courseLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[3]/span[2]")));
        courseLink.click();

        WebElement gradesLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Grades")));
        gradesLink.click();

        WebElement dropdownToggle = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".dropdown-toggle:nth-child(2)")));
        dropdownToggle.click();

        WebElement element = driver.findElement(By.xpath(
                "/html/body/div[2]/div[4]/div/div[3]/div/section/div/div[1]/div/div[1]/nav/div/ul/li[2]/ul/li[3]"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        WebElement displayTypeOption = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='Letter']")));
        displayTypeOption.click();

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("id_submitbutton")));
        submitButton.click();

        WebElement courseLink2 = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Course")));
        courseLink2.click();

        WebElement assignmentLink = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".modtype_assign .aalink")));
        assignmentLink.click();

        WebElement gradeLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Grade")));
        gradeLink.click();

        WebElement gradeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_grade")));
        gradeInput.clear();
        gradeInput.sendKeys(score);

        WebElement saveButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.name("savechanges")));
        saveButton.click();

        WebElement errorGradeElement = driver.findElement(By.id("id_error_grade"));
        if (errorGradeElement.isDisplayed()) {
            System.out.println("Error message displayed: " + errorGradeElement.getText());
        }

        WebElement currentGradeLetter = driver.findElement(By.xpath(
                "/html/body/div[5]/section/div[2]/div[3]/div/div[2]/form/fieldset/div[2]/div[2]/div[2]/div[1]/span/a"));
        currentGradeLetter.click();

        WebElement letterGradeElement = driver.findElement(By.xpath(
                "/html/body/div[2]/div[4]/div/div[3]/div/section/div/div[2]/table/tbody/tr[3]/td[3]/div/div[1]/div[1]/div/span"));
        String calculatedLetterGrade = letterGradeElement.getText();
        if (calculatedLetterGrade.equals(expectedLetterGrade)) {
            System.out.println("Test Passed - Expected letter grade: " + expectedLetterGrade +
                    ", Actual letter grade: " + calculatedLetterGrade);
        } else {
            System.out.println("Test Failed - Expected letter grade: " + expectedLetterGrade +
                    ", Actual letter grade: " + calculatedLetterGrade);
        }

        WebElement logoutLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/nav/div/div[2]/div[5]/div/div/a")));
        logoutLink.click();

        WebElement logout = driver
                .findElement(By.xpath("/html/body/div[2]/nav/div/div[2]/div[5]/div/div/div/div/div/div[1]/a[10]"));
        logout.click();

        System.out.println("Test complete");
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
