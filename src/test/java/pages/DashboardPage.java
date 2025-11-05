package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {
    private final WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public void abrir(){
        driver.get("https://react-na-pratica.web.app/dashboard");
    }

    public void abrirMenuDeOpcoesPerfil(){
        driver.findElement(By.xpath("//*[@id=\":r5:\"]/div/img")).click();
    }

    public void abrirMenuGerenciamentoDeCursos(){
        driver.findElement(By.xpath("/html/body/div[2]/div[3]/ul/li[2]")).click();
    }
}

