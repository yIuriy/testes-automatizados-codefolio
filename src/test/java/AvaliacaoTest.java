import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DashboardPage;
import pages.ManageCoursePage;
import utils.Authentication;
import utils.Utilitarios;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvaliacaoTest {
    WebDriver driver;
    Authentication authentication;
    ManageCoursePage manageCoursePage;
    WebDriverWait wait;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        manageCoursePage = new ManageCoursePage(driver);
        dashboardPage = new DashboardPage(driver);
        authentication.realizarLoginViaIndexedBD();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 11/11/2025
     *
     */
    @Test
    @DisplayName("Consulta de Avaliações de Alunos com aluno já tendo nota atribuída")
    void CT19() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");

            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A1");
            manageCoursePage.clicarBotaoDeAtribuirNota(trAvaliacao);

            Thread.sleep(1000);
            Utilitarios.scrollarTela(js, "-500");

            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Iuri");
            assertEquals("Iuri Da Silva Fernandes", trAluno.findElement(By.tagName("p")).getText());

            WebElement inputDeNota = manageCoursePage.localizarInputDeNota(trAluno);
            String notaDoAluno = manageCoursePage.obterNotaDoAluno(inputDeNota);

            assertEquals("10", notaDoAluno);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 11/11/2025
     *
     */
    @Test
    @DisplayName("Consulta de Avaliações de Alunos com aluno já tendo nota atribuída")
    void CT19_1() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");

            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A1");
            manageCoursePage.clicarBotaoDeAtribuirNota(trAvaliacao);

            Thread.sleep(1000);
            Utilitarios.scrollarTela(js, "-500");

            WebElement trAluno1 = manageCoursePage.localizarLinhaDoAlunoPorNome("Iuri");
            assertEquals("Iuri Da Silva Fernandes", trAluno1.findElement(By.tagName("p")).getText());

            WebElement inputDeNota1 = manageCoursePage.localizarInputDeNota(trAluno1);
            String notaDoAluno1 = manageCoursePage.obterNotaDoAluno(inputDeNota1);

            assertEquals("10", notaDoAluno1);

            WebElement trAluno2 = manageCoursePage.localizarLinhaDoAlunoPorNome("Zildo");
            assertEquals("Zildo Tester Java", trAluno2.findElement(By.tagName("p")).getText());

            WebElement inputDeNota2 = manageCoursePage.localizarInputDeNota(trAluno2);
            String notaDoAluno2 = manageCoursePage.obterNotaDoAluno(inputDeNota2);

            assertEquals("10", notaDoAluno2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 11/11/2025
     *
     */
    @Test
    @DisplayName("Consulta de Avaliação sem nenhuma nota atribuída")
    void CT19_2() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");

            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação sem Notas");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);
            manageCoursePage.clicarBotaoDeAtribuirNota(trAvaliacao);

            WebElement tBodyContendoOsAlunos = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tbody")
            ));

            for (WebElement trAluno : tBodyContendoOsAlunos.findElements(By.tagName("tr"))) {
                assertEquals("", manageCoursePage.obterNotaDoAluno(
                        manageCoursePage.localizarInputDeNota(trAluno)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 12/11/2025
     *
     */
    @Test
    @DisplayName("Informações e opções da Avaliação são exibidas corretamente")
    void CT19_3() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");

            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A1");

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("A1", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("50%", elementosDentroDoTrAvaliacao.get(1).getText());

            // Realiza os assertEquals, garantindo que todas as opções existem
            verificarSeExisteMenuDeOpcoesDaAvaliacao(elementosDentroDoTrAvaliacao);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 12/11/2025
     *
     */
    @Test
    @DisplayName("Soma dos percentuais das avaliações cadastradas é exibido corretamente")
    void CT19_4() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");

            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            int percentualTotal = manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas();

            String textoPercentualTotal = manageCoursePage.obterTextoDoTotalDaNotaFinal();

            assertEquals("Total: " + percentualTotal + "% da nota final", textoPercentualTotal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 12/11/2025
     *
     */
    @Test
    @DisplayName("Verificar comportamento do sistema em um curso sem nenhuma avaliação cadastrada")
    void CT19_5() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Curso sem Avaliações");

            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//h6[contains(text(), 'Avaliações Cadastradas')]")))
                    .findElement(By.xpath(".."));

            assertTrue(
                    div.findElements(By.tagName("div")).stream().anyMatch(
                            element -> element.getText().equalsIgnoreCase("Nenhuma avaliação cadastrada. Adicione sua primeira " +
                                    "avaliação usando o formulário acima.")
                    )
            );

            String textoPercentualTotal = manageCoursePage.obterTextoDoTotalDaNotaFinal();
            assertEquals("Total: 0% da nota final", textoPercentualTotal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Cadastro de avaliação com sucesso")
    void CT22() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            int percentualTotalAntesDaAdicao = manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas();

            manageCoursePage.obterInputDeNomeDaAvaliacao().sendKeys("A2");

            manageCoursePage.obterInputDeNotaDaAvaliacao().sendKeys("20");

            manageCoursePage.obterBotaoAdicionarAvaliacao().click();

            assertTrue(manageCoursePage.verificarSeMensagemAvaliacaoCadastradaComSucessoApareceu());

            String textoPercentualTotal = manageCoursePage.obterTextoDoTotalDaNotaFinal();

            int percentualAposAdicao = manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas();

            assertEquals(percentualTotalAntesDaAdicao + 20, percentualAposAdicao);

            assertEquals("Total: " + (percentualTotalAntesDaAdicao + 20) + "% da nota final",
                    textoPercentualTotal);

            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A2");

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("A2", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("20%", elementosDentroDoTrAvaliacao.get(1).getText());

            // Realiza os assertEquals, garantindo que todas as opções existem
            verificarSeExisteMenuDeOpcoesDaAvaliacao(elementosDentroDoTrAvaliacao);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Tentar cadastrar uma avaliação sem nome")
    void CT22_1() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            inputNome.sendKeys("");

            manageCoursePage.obterInputDeNotaDaAvaliacao().sendKeys("20");

            manageCoursePage.obterBotaoAdicionarAvaliacao().click();

            assertEquals("Preencha este campo.", capturarMensagemPadraoDeCampoVazioDoInput(inputNome));
            assertFalse(verificarValidadeDoInput(inputNome));

            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoTotalDaNotaFinal());

            manageCoursePage.irAteSecaoAvaliacoesCadastradas();
            assertThrows(TimeoutException.class, () -> {
                manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A2");
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Tentar cadastrar uma avaliação sem nota")
    void CT22_2() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            manageCoursePage.obterInputDeNomeDaAvaliacao().sendKeys("A2");

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            inputNota.sendKeys("");

            manageCoursePage.obterBotaoAdicionarAvaliacao().click();

            assertEquals("Preencha este campo.", capturarMensagemPadraoDeCampoVazioDoInput(inputNota));
            assertFalse(verificarValidadeDoInput(inputNota));

            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoTotalDaNotaFinal());

            manageCoursePage.irAteSecaoAvaliacoesCadastradas();
            assertThrows(TimeoutException.class, () -> {
                manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A2");
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Verificar se mensagem é exibida quando percentual total das avaliações passa de 100%")
    void CT22_3() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            int percentualTotalAntesDaAdicao = manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas();

            manageCoursePage.obterInputDeNomeDaAvaliacao().sendKeys("A2");

            manageCoursePage.obterInputDeNotaDaAvaliacao().sendKeys("30");

            manageCoursePage.obterBotaoAdicionarAvaliacao().click();

            String alertaSomaPercentualAcimaDe100 = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(), 'Atenção: O total dos')]")
            )).getText();

            assertEquals("Atenção: O total dos percentuais (" + (percentualTotalAntesDaAdicao + 30) + "%) excede 100%" +
                    ".", alertaSomaPercentualAcimaDe100);

            assertTrue(manageCoursePage.verificarSeMensagemAvaliacaoCadastradaComSucessoApareceu());

            String textoPercentualTotal = manageCoursePage.obterTextoDoTotalDaNotaFinal();

            int percentualAposAdicao = manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas();

            assertEquals(percentualTotalAntesDaAdicao + 30, percentualAposAdicao);

            assertEquals("Total: " + (percentualAposAdicao) + "% da nota final",
                    textoPercentualTotal);

            manageCoursePage.irAteSecaoAvaliacoesCadastradas();

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A2");

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("A2", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("30%", elementosDentroDoTrAvaliacao.get(1).getText());

            // Realiza os assertEquals, garantindo que todas as opções existem
            verificarSeExisteMenuDeOpcoesDaAvaliacao(elementosDentroDoTrAvaliacao);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Verificar se sistema bloqueia notas negativas ou acima de 100")
    void CT22_4() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            manageCoursePage.obterInputDeNomeDaAvaliacao().sendKeys("A2");

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            inputNota.sendKeys("-10");

            manageCoursePage.obterBotaoAdicionarAvaliacao().click();

            assertEquals("O valor deve ser maior ou igual a 1.", capturarMensagemPadraoDeCampoVazioDoInput(inputNota));
            assertFalse(verificarValidadeDoInput(inputNota));

            Thread.sleep(2000);
            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            Thread.sleep(2000);

            inputNota.sendKeys("200");

            manageCoursePage.obterBotaoAdicionarAvaliacao().click();
            assertEquals("O valor deve ser menor ou igual a 100.",
                    capturarMensagemPadraoDeCampoVazioDoInput(inputNota));
            assertFalse(verificarValidadeDoInput(inputNota));

            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoTotalDaNotaFinal());

            manageCoursePage.irAteSecaoAvaliacoesCadastradas();
            assertThrows(TimeoutException.class, () -> {
                manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A2");
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean verificarValidadeDoInput(WebElement input) {
        Boolean valido = (Boolean) js.executeScript("return arguments[0].checkValidity()", input);
        assertNotNull(valido);
        return valido;
    }

    private String capturarMensagemPadraoDeCampoVazioDoInput(WebElement input) {
        String msg = (String) js.executeScript("return arguments[0].validationMessage", input);
        assertNotNull(msg);
        return msg;
    }

    private void irAteAPaginaDeGerenciarCursos() {
        dashboardPage.abrirMenuDeOpcoesPerfil();
        dashboardPage.abrirMenuGerenciamentoDeCursos();
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }

    private void verificarSeExisteMenuDeOpcoesDaAvaliacao(List<WebElement> elementos) {
        List<WebElement> opcoes = elementos.getLast().findElements(By.tagName("button"));
        WebElement editButton = opcoes.getFirst().findElement(By.tagName("svg"));
        assertEquals("EditIcon", editButton.getAttribute("data-testid"));

        WebElement deleteButton = opcoes.get(1).findElement(By.tagName("svg"));
        assertEquals("DeleteIcon", deleteButton.getAttribute("data-testid"));

        assertEquals("ATRIBUIR NOTA", opcoes.getLast().getText());
    }
}
