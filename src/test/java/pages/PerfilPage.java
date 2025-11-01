package pages;

import org.openqa.selenium.WebDriver;

public class PerfilPage {
    private WebDriver driver;

    public PerfilPage(WebDriver driver) {
        this.driver = driver;
    }

    public void abrir(){
        driver.get("https://react-na-pratica.web.app/profile");
    }
}
