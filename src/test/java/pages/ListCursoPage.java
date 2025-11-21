package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class ListCursoPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public ListCursoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
    }

    public ListCursoPage abrirPaginaCursos() {
    By botaoCursos = By.xpath("//a[@href='/listcurso']");
    WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(botaoCursos));
    js.executeScript("arguments[0].scrollIntoView({block:'center'});", botao);
    js.executeScript("arguments[0].click();", botao);
    return this;
}

public ListCursoPage abaEmAndamento() {
    By botaoEmAndamento = By.xpath("//button[contains(@class,'MuiTab-root') and normalize-space(text())='Em Andamento']");
    WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(botaoEmAndamento));
    js.executeScript("arguments[0].scrollIntoView({block:'center'});", botao);
    botao.click();
    return this;
}

    public ListCursoPage abaConcluido() {
        WebElement botao = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[3]/div[1]/div/div/button[3]"));
        botao.click();
        return this;
    }

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

        By cardContainer = By.xpath(
                ".//ancestor::*[contains(@class,'MuiPaper-root') or contains(@class,'MuiCard-root') or contains(@class,'MuiGrid-item')][1]"
        );
        WebElement card = titulo.findElement(cardContainer);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", card);

        // Clica no botão para entrar
        By botaoNoCard = By.xpath(".//button[contains(normalize-space(.),'Começar') or contains(normalize-space(.),'Continuar') or contains(normalize-space(.),'Ver Curso')]");
        WebElement botao = w.until(ExpectedConditions.elementToBeClickable(card.findElement(botaoNoCard)));
        botao.click();

        return this;
    }


    public ListCursoPage clicarVerVideoPorTitulo(String tituloDoVideo) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(12));

        // clicar no botão "Ver Vídeo" que aparece na tela
        By botaoVerVideo = By.xpath("//button[contains(normalize-space(.),'Ver Vídeo')]");
        WebElement btn = w.until(ExpectedConditions.elementToBeClickable(botaoVerVideo));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();

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

    public boolean videoCarregou() {
        By iframePlayer = By.cssSelector("iframe[src*='youtube'], iframe[src*='player'], iframe[src*='embed']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(iframePlayer)).isDisplayed();
    }

    public ListCursoPage avancarVideo() {
        By botaoAvancarBy = By.xpath("//button[.//svg[@data-testid='ArrowForwardIcon']]");
        WebElement botaoAvancar = wait.until(ExpectedConditions.presenceOfElementLocated(botaoAvancarBy));
        js.executeScript("arguments[0].click();", botaoAvancar);
        return this;
    }

    public ListCursoPage voltarVideo() {
        By botaoVoltarBy = By.xpath("//button[.//svg[@data-testid='ArrowBackIcon']]");
        WebElement botaoVoltar = wait.until(ExpectedConditions.presenceOfElementLocated(botaoVoltarBy));
        js.executeScript("arguments[0].click();", botaoVoltar);
        return this;
    }

    public String pegarSrcVideo() {
        By iframePlayer = By.cssSelector("iframe[src*='youtube'], iframe[src*='player'], iframe[src*='embed']");
        WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(iframePlayer));
        return iframe.getAttribute("src");
    }

    public ListCursoPage materiaisExtra() {
        By botaoMateriaisExtra = By.xpath("//button[normalize-space()='Materiais Extras']");
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(botaoMateriaisExtra));
        js.executeScript("arguments[0].click();", botao);
        return this;
    }

    public boolean mensagemSemMateriaisExtraEstaNaTela() {
        By mensagem = By.xpath("//p[normalize-space()='Não existem materiais extras relacionados a esta aula.']");
        try {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(mensagem));
            return elemento.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean verificarSeCurosAparece(String nomeCurso){

        try{

            WebElement div = driver.findElement(
                By.xpath("//div[//h6[contains(normalize-space(), '"+nomeCurso+"')]]")
            );

            return true;
        }catch(Exception e){
            return false;
        }
    }

}
