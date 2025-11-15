package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class ListCursoPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ListCursoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /** Abre a lista de todos os cursos */
    public ListCursoPage abrirPaginaCursos() {
        driver.get("https://testes.codefolio.com.br/listcurso");
        return this;
    }

    /** Abre um curso pelo nome (exato ou que contenha o nome) */
    public ListCursoPage abrirCurso(String nomeCurso) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(12));

        // Tenta encontrar o título
        By tituloExato = By.xpath("//h6[contains(@class,'MuiTypography-subtitle1') and normalize-space()='" + nomeCurso + "']");
        By tituloContains = By.xpath("//h6[contains(@class,'MuiTypography-subtitle1') and contains(normalize-space(),'" + nomeCurso + "')]");

        WebElement titulo;
        List<WebElement> hits = driver.findElements(tituloExato);
        if (!hits.isEmpty()) {
            titulo = hits.get(0);
        } else {
            titulo = w.until(ExpectedConditions.visibilityOfElementLocated(tituloContains));
        }

        // Sobe pro card do curso
        By cardContainer = By.xpath(
                ".//ancestor::*[contains(@class,'MuiPaper-root') or contains(@class,'MuiCard-root') or contains(@class,'MuiGrid-item')][1]"
        );
        WebElement card = titulo.findElement(cardContainer);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", card);

        // Clica no botão para entrar
        By botaoNoCard = By.xpath(".//button[contains(normalize-space(.),'Começar') or contains(normalize-space(.),'Acessar') or contains(normalize-space(.),'Ver Vídeo')]");
        WebElement botao = w.until(ExpectedConditions.elementToBeClickable(card.findElement(botaoNoCard)));
        botao.click();

        return this;
    }

    public boolean videoEstaNaTela() {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(20));
        By iframePlayer = By.cssSelector(
            "iframe[src*='youtube'], iframe[src*='player'], iframe[src*='embed']"
        );
        try {
            WebElement iframe = w.until(ExpectedConditions.visibilityOfElementLocated(iframePlayer));
            return iframe.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Dentro do curso, clica em "Ver Vídeo" */
    public ListCursoPage clicarVerVideoPorTitulo(String tituloDoVideo) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(12));

        // Versão simples e robusta: clicar no botão "Ver Vídeo" que aparece na tela
        By botaoVerVideo = By.xpath("//button[contains(normalize-space(.),'Ver Vídeo')]");
        WebElement btn = w.until(ExpectedConditions.elementToBeClickable(botaoVerVideo));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();

        return this;
    }

    /** Confirma que o vídeo realmente carregou */
    public boolean videoCarregou() {
        By iframePlayer = By.cssSelector("iframe[src*='youtube'], iframe[src*='player'], iframe[src*='embed']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(iframePlayer)).isDisplayed();
    }
}