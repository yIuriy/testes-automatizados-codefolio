package pages;

import org.openqa.selenium.WebDriver;

public class ManageCoursePage {
    private final WebDriver driver;

    public ManageCoursePage(WebDriver driver) {
        this.driver = driver;
    }

   public void abrir() {
        driver.get("https://testes.codefolio.com.br/manage-courses");
    }
}
