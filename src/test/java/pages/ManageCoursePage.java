package pages;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilitarios;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManageCoursePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public ManageCoursePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    public void abrir() {
        driver.get("https://testes.codefolio.com.br/manage-courses");
    }

    public void clicarBotaoGerenciarCursoDoPrimeiroCurso() {
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Gerenciar Curso']")
        ))).click();
    }

    public void clicarBotaoGerenciarCursoPorNomeDoCurso(String nome) {
        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                "//div[contains(@class, 'MuiGrid-item') and .//h6[contains(normalize-space(), '" + nome + "')]]")));
        div.findElement(By.xpath(".//button[contains(., 'Gerenciar Curso')]")).click();
    }

    public void localizarEClicarNoMenuPorNome(String nomeDoMenu) {
        WebElement menuAlunos = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
                "//button[contains(text(),'" + nomeDoMenu + "')]"))));
        Utilitarios.centralizarElementoNaTela(menuAlunos, driver);

        assert menuAlunos != null;
        menuAlunos.click();
    }

    public WebElement getInputFiltrar() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@placeholder, 'Buscar')]")));
    }

    public void inserirTextoNoFiltrar(String texto) {
        WebElement inputFiltrar = getInputFiltrar();
        assertNotNull(inputFiltrar);
        Utilitarios.centralizarElementoNaTela(inputFiltrar, driver);
        inputFiltrar.sendKeys(texto);
    }

    public void limparTextoNoFiltrar() {
        WebElement inputFiltrar = getInputFiltrar();
        assertNotNull(inputFiltrar);
        Utilitarios.centralizarElementoNaTela(inputFiltrar, driver);
        inputFiltrar.sendKeys(Keys.CONTROL + "a");
        inputFiltrar.sendKeys(Keys.DELETE);
    }

    public void filtrarAlunosPorProgresso(String opcao) {
        clicarInputFiltroPorId("progress-filter");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), '" + opcao + "')]")
        ))).click();
    }

    public void filtrarAlunosPorRole(String role) {
        clicarInputFiltroPorId("role-filter");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), '" + role + "')]")
        ))).click();
    }

    public void clicarEmOrdenarDeAZ() {
        clicarInputFiltroPorId("sort-select");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), 'Nome (A-Z)')]")
        ))).click();
    }

    public void clicarEmOrdenarDeZA() {
        clicarInputFiltroPorId("sort-select");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), 'Nome (Z-A)')]")
        ))).click();
    }

    private void clicarInputFiltroPorId(String id) {
        WebElement until = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id(id)
                )
        );
        WebElement webElement = until.findElement(By.xpath(".."));
        webElement.click();
    }

    public WebElement localizarLinhaDoAlunoPorNome(String nome) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[.//p[contains(text(),'" + nome + "')]]")
        ));
    }

    public boolean verificarSeExisteIconeDeDeletarAluno(List<WebElement> elementos) {
        for (WebElement elemento : elementos) {
            List<WebElement> buttons = elemento.findElements(By.cssSelector("button, input[type='button']"));
            if (!buttons.isEmpty()) {
                for (WebElement elemento1 : buttons) {
                    WebElement webElement = elemento1.findElement(By.cssSelector("[data-testid]"));
                    if (Objects.requireNonNull(webElement.getAttribute("data-testid")).equalsIgnoreCase("DeleteIcon"))
                        return true;
                }
            }
        }
        return false;
    }

    public void clicarIconeDeExcluirAluno(WebElement trAluno) {
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(trAluno.findElement(By.cssSelector("button"))))).click();
    }

    @NotNull
    public WebElement localizarCaixaDeTextoConfirmarCancelarExclusao() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains" +
                "(normalize-space(), " +
                "'Confirmar exclusão')]"))).findElement(By.xpath(".."));
    }

    public void clicarBotaoConfirmarExclusaoDeAluno() {
        WebElement div = localizarCaixaDeTextoConfirmarCancelarExclusao();
        div.findElements(By.cssSelector("button")).get(1).click();

    }

    public void clicarBotaoCancelarExclusaoDeAluno() {
        WebElement div = localizarCaixaDeTextoConfirmarCancelarExclusao();
        div.findElements(By.cssSelector("button")).getFirst().click();
    }

    public void irAteSecaoAvaliacoesCadastradas() {
        WebElement tituloSecao = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[contains(text(), 'Avaliações Cadastradas')]")
        ));
        Utilitarios.centralizarElementoNaTela(tituloSecao, driver);
    }

    public WebElement localizarLinhaDaAvaliacaoPorNome(String nome) {
        WebElement trAvaliacao = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[.//td[contains(text(),'" + nome + "')]]")
        ));
        List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));
        assertEquals(nome, elementosDentroDoTrAvaliacao.getFirst().getText());
        return trAvaliacao;
    }

    public void clicarBotaoDeAtribuirNota(WebElement trAvaliacao) {
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                trAvaliacao.findElement(By.xpath(
                        ".//button[contains(text(), 'Atribuir Nota')]"
                ))
        ))).click();
    }

    public WebElement localizarInputDeNota(WebElement trAluno) {
        return wait.until(ExpectedConditions.elementToBeClickable(trAluno.findElement(By.tagName("input"))
        ));
    }

    public String obterNotaDoAluno(WebElement inputNota) {
        return inputNota.getAttribute("value");
    }

    public WebElement localizarLinhaDoAlunoSemNotaAtribuida() {
        List<WebElement> inputLista = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tbody[.//tr[.//td]]")
        )).findElements(By.tagName("input"));

        inputLista = inputLista.stream().filter(webElement ->
                Objects.equals(webElement.getAttribute("value"), "")).toList();

        WebElement primeiroTrAlunoSemNota = inputLista.getFirst();

        return primeiroTrAlunoSemNota.findElement(By.xpath("./ancestor::tr"));
    }

    public String obterValorDoSvgDeConfirmacaoSeNotaFoiSalvaCorretamente(WebElement trAlunoSemNotaAtribuida) {
        WebElement tr = trAlunoSemNotaAtribuida.findElements(By.tagName("td")).get(2);
        String svg = tr.findElements(By.tagName("svg")).getFirst().getAttribute("aria-label");
        assertNotNull(svg);
        return svg;
    }

    public int obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas() {
        WebElement tbody = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.tagName("tbody")
                )
        );
        int somaTotal = 0;
        for (WebElement e : tbody.findElements(By.tagName("tr"))) {
            List<WebElement> elements = e.findElements(By.xpath("td"));
            int percentual = Integer.parseInt(elements.get(1).getText().replace("%", ""));
            somaTotal += percentual;
        }
        return somaTotal;
    }

    public String obterTextoDoTotalDaNotaFinal() {
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h6[contains(text(),'Total:')]")
                )
        ).getText();
    }

    public WebElement obterTableBodyDosAlunosExibidos() {
        WebElement trAlunos = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tbody")
        ));
        return trAlunos;
    }

    public void clicarRemoverFiltroAtivo() {
        WebElement divContainerFiltroAtivo = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@aria-label='Limpar todos os filtros']")
        ));

        divContainerFiltroAtivo.findElement(By.tagName("svg")).click();
    }
}

