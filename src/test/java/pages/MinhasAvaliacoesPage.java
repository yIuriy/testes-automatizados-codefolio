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

/**
 * Classe que armazena métodos referentes à página de minhas avaliações.
 */
public class MinhasAvaliacoesPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;


    public MinhasAvaliacoesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Obtém a div container que armazena as avaliações presentes em um curso.
     *
     * @param nomeDoCurso o nome do curso que contém as avaliações que voçê deseja ver
     * @return a div que contém todas as avaliações do curso
     * */
    public WebElement obterDivDasAvaliacoesDoCursoPorNomeDoCurso(String nomeDoCurso) {
        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h5[contains(text(), '" + nomeDoCurso + "')]/ancestor::div[4]")));
        assertEquals(nomeDoCurso, div.findElement(By.tagName("h5")).getText());
        assertNotNull(div);
        return div;
    }

    /**
     * Obtém o nome do aluno do qual as avaliações estão sendo exibidas.
     *
     * @param divPrincipal a div que contém todas as avaliações do curso
     * @return o nome do aluno
     * */
    public String obterNomeDoAluno(WebElement divPrincipal) {
        String nomeAluno = divPrincipal.findElement(By.tagName("div")).findElement(By.tagName("p")).getText();
        assertNotNull(nomeAluno);
        return nomeAluno;
    }

    /**
     * Obtém o nome o elemento que abre a seção de avaliações do curso.
     *
     * @param divPrincipal a div que contém todas as avaliações do curso
     * @return o elemento que ao ser clicado abre a seção de avaliações do curso
     * */
    public WebElement obterElementoQueAbreAsAvaliacoesDoCurso(WebElement divPrincipal) {
        WebElement elemento = divPrincipal.findElement(By.tagName("h3"));
        assertNotNull(elemento);
        return elemento;
    }


    /**
     * Obtém a quantidade de avaliações cadastradas em certo curso.
     *
     * @param h3 o elemento que armazena as informações desejadas.
     * @return quantas avaliações aquele curso possui
     * */
    public String obterQuantidadeTotalDeAvaliacoesPresentesNoCurso(WebElement h3) {
        List<WebElement> elementos = h3.findElements(By.xpath(".//*[contains(text(),'avaliações')]"));
        assertNotNull(elementos);
        return elementos.getFirst().getText();
    }

    /**
     * Obtém a div individual da avaliação.
     *
     * @param divCurso        a div do curso que contém a avaliação desejada
     * @param nomeDaAvaliacao o nome da avaliação desejada
     * @return a div container individual da avaliação
     *
     */
    public WebElement obterDivDaAvaliacao(WebElement divCurso, String nomeDaAvaliacao) {
        WebElement p = divCurso.findElement(
                By.xpath(".//p[contains(text(), '" + nomeDaAvaliacao + "')]")
        );
        assertEquals(nomeDaAvaliacao, p.getText());
        WebElement divAvaliacao = p.findElement(By.xpath("./ancestor::div[2]"));
        assertNotNull(divAvaliacao);
        return divAvaliacao;
    }

    /**
     * Obtém o H5 que contém a nota total do aluno naquele curso.
     *
     * @param divCurso a div do curso que contém a avaliação desejada
     * @return o h5 que contém a nota total
     *
     */
    public WebElement obterH5DaNotaTotal(WebElement divCurso){
        WebElement h5NotaTotal = divCurso.findElements(
                By.tagName("h5")
        ).get(1);
        assertNotNull(h5NotaTotal);
        return h5NotaTotal;
    }

    /**
     * Obtém o título da avaliação com base na div dela.
     *
     * @param divAvaliacao a div da avaliação
     * @return o título da avaliação
     *
     */
    public String obterTituloDaDivAvaliacao(WebElement divAvaliacao) {
        String titulo = divAvaliacao.findElement(By.xpath(".//p")).getText();
        assertNotNull(titulo);
        return titulo;
    }

    /**
     * Obtém a nota da avaliação com base na div dela.
     *
     * @param divAvaliacao a div da avaliação
     * @return a nota do aluno naquela avaliação
     *
     */
    public String obterNotaDaDivAvaliacao(WebElement divAvaliacao) {
        return divAvaliacao.findElement(
                By.xpath(".//div[contains(@class,'MuiChip-root')]//span")
        ).getText();
    }

}