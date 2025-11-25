package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MinhasAvaliacoesPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;


    public MinhasAvaliacoesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    public WebElement obterDivDasAvaliacoesDoCursoPorNomeDoCurso(String nomeDoCurso) {
        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h5[contains(text(), '" + nomeDoCurso + "')]/ancestor::div[4]")));
        assertEquals(nomeDoCurso, div.findElement(By.tagName("h5")).getText());
        assertNotNull(div);
        return div;
    }

    public String obterNomeDoAluno(WebElement divPrincipal) {
        String nomeAluno = divPrincipal.findElement(By.tagName("div")).findElement(By.tagName("p")).getText();
        assertNotNull(nomeAluno);
        return nomeAluno;
    }

    public WebElement obterElementoQueAbreAsAvaliacoesDoCurso(WebElement divPrincipal) {
        WebElement elemento = divPrincipal.findElement(By.tagName("h3"));
        assertNotNull(elemento);
        return elemento;
    }

    public String obterQuantidadeTotalDeAvaliacoesPresentesNoCurso(WebElement h3) {
        List<WebElement> elementos = h3.findElements(By.xpath(".//*[contains(text(),'avaliações')]"));
        assertNotNull(elementos);
        return elementos.getFirst().getText();
    }

    public WebElement obterDivDaAvaliacao(WebElement divCurso, String nomeDaAvaliacao) {
        WebElement p = divCurso.findElement(
                By.xpath(".//p[contains(text(), '" + nomeDaAvaliacao + "')]")
        );
        assertEquals(nomeDaAvaliacao, p.getText());
        WebElement divAvaliacao = p.findElement(By.xpath("./ancestor::div[2]"));
        assertNotNull(divAvaliacao);
        return divAvaliacao;
    }

    public WebElement obterH5DaNotaTotal(WebElement divPrincipal){
        WebElement h5NotaTotal = divPrincipal.findElements(
                By.tagName("h5")
        ).get(1);
        assertNotNull(h5NotaTotal);
        return h5NotaTotal;
    }



    public String obterTituloDaDivAvaliacao(WebElement divAvaliacao) {
        String titulo = divAvaliacao.findElement(By.xpath(".//p")).getText();
        assertNotNull(titulo);
        return titulo;
    }

    public String obterNotaDaDivAvaliacao(WebElement divAvaliacao) {
        return divAvaliacao.findElement(
                By.xpath(".//div[contains(@class,'MuiChip-root')]//span")
        ).getText();
    }

}