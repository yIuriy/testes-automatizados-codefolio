package pages;

import java.time.Duration;
import java.awt.*;
import java.awt.datatransfer.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MenuHomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public MenuHomePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    public void abrir() {
        driver.get("https://testes.codefolio.com.br/");
    }

    public void clicarEmLike(String tituloVideo) {

        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[.//div[contains(normalize-space(), '" + tituloVideo + "')]]")));

        div.findElement(By.xpath(".//button[.//*[@data-testid='ThumbUpIcon']]")).click();
    }

    public void clicarEmDislike(String tituloVideo) {

        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[.//div[contains(normalize-space(), '" + tituloVideo + "')]]")));

        div.findElement(By.xpath(".//button[.//*[@data-testid='ThumbDownIcon']]")).click();
    }

    public int pegarNumeroDeLikes(String tituloVideo) {
        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[.//div[contains(normalize-space(), '" + tituloVideo + "')]]")));

        String texto = div.findElement(By.xpath(".//div[contains(@class, 'info-likes')]")).getText();
        String[] partes = texto.split(" "); 

        return Integer.parseInt(partes[0]);

    }

    public void clicarEmCompartilhar(String tituloVideo){
        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[.//div[contains(normalize-space(), '" + tituloVideo + "')]]")));
        
        div.findElement(By.xpath(".//span[normalize-space(text())='Compartilhar']/ancestor::div[1]")).click();
    }

    public String pegarTextoDaAreaDeTransferencia() {
    try {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        return (String) clipboard.getData(DataFlavor.stringFlavor);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

} 