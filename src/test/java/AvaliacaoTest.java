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
import pages.MinhasAvaliacoesPage;
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
    MinhasAvaliacoesPage minhasAvaliacoesPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        manageCoursePage = new ManageCoursePage(driver);
        dashboardPage = new DashboardPage(driver);
        minhasAvaliacoesPage = new MinhasAvaliacoesPage(driver);
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
    @DisplayName("Consulta de Avaliações de Alunos com múltiplos alunos")
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

            String textoPercentualTotal = manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal();

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

            String textoPercentualTotal = manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal();
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

            String textoPercentualTotal = manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal();

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

            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

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

            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

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

            String textoPercentualTotal = manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal();

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

            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

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
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Editar nome e nota da avaliação")
    void CT23() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            inputNome.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);

            inputNome.sendKeys("Avaliação Editada");
            inputNota.sendKeys("20");

            WebElement btnAtualizarAvaliacao = manageCoursePage.obterBotaoAtualizarAvaliacao();
            assertEquals("ATUALIZAR AVALIAÇÃO", btnAtualizarAvaliacao.getText());
            btnAtualizarAvaliacao.click();

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação Editada");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação Editada", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("20%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(90, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
            assertEquals("Total: 90% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT23: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Editar somente o nome da avaliação")
    void CT23_1() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            inputNome.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);

            inputNome.sendKeys("Avaliação Renomeada");

            WebElement btnAtualizarAvaliacao = manageCoursePage.obterBotaoAtualizarAvaliacao();
            assertEquals("ATUALIZAR AVALIAÇÃO", btnAtualizarAvaliacao.getText());
            btnAtualizarAvaliacao.click();

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação Renomeada");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação Renomeada", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("10%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT23.1: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Editar somente a nota da avaliação")
    void CT23_2() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            inputNota.sendKeys("20");

            WebElement btnAtualizarAvaliacao = manageCoursePage.obterBotaoAtualizarAvaliacao();
            assertEquals("ATUALIZAR AVALIAÇÃO", btnAtualizarAvaliacao.getText());
            btnAtualizarAvaliacao.click();

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação para Editar", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("20%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(90, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
            assertEquals("Total: 90% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT23.2: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Clicar em Editar e Cancelar")
    void CT23_3() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            inputNota.sendKeys("99");

            WebElement btnCancelar = manageCoursePage.obterBotaoCancelarAtualizacaoDaAvaliacao();
            assertEquals("CANCELAR", btnCancelar.getText());
            btnCancelar.click();

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação para Editar", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("10%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT23.3: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Verificar se sistema bloqueia notas negativas ou acima de 100 ao editar avaliação")
    void CT23_4() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            WebElement btnAtualizarAvaliacao = manageCoursePage.obterBotaoAtualizarAvaliacao();
            assertEquals("ATUALIZAR AVALIAÇÃO", btnAtualizarAvaliacao.getText());

            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);

            inputNota.sendKeys("-20");
            btnAtualizarAvaliacao.click();

            assertEquals("O valor deve ser maior ou igual a 1.", capturarMensagemPadraoDeCampoVazioDoInput(inputNota));
            assertFalse(verificarValidadeDoInput(inputNota));

            Thread.sleep(2000);
            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            Thread.sleep(2000);

            inputNota.sendKeys("300");
            btnAtualizarAvaliacao.click();

            assertEquals("O valor deve ser menor ou igual a 100.",
                    capturarMensagemPadraoDeCampoVazioDoInput(inputNota));
            assertFalse(verificarValidadeDoInput(inputNota));

            Thread.sleep(3000);
            driver.navigate().refresh();
            Thread.sleep(1000);
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação para Editar", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("10%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
        } catch (Exception e) {
            System.err.println("Erro no CT23.4: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Verificar se mensagem é exibida quando percentual total das avaliações passa de 100% ao editar avaliação")
    void CT23_5() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            int percentualTotalAntesDaAdicao = manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas();

            assertEquals(80, percentualTotalAntesDaAdicao);

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            inputNota.sendKeys("40");

            WebElement btnAtualizarAvaliacao = manageCoursePage.obterBotaoAtualizarAvaliacao();
            assertEquals("ATUALIZAR AVALIAÇÃO", btnAtualizarAvaliacao.getText());
            btnAtualizarAvaliacao.click();

            String alertaSomaPercentualAcimaDe100 = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(), 'Atenção: O total dos')]")
            )).getText();

            assertEquals("Atenção: O total dos percentuais (" + (percentualTotalAntesDaAdicao + 30) + "%) excede 100%" +
                    ".", alertaSomaPercentualAcimaDe100);

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação para Editar", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("40%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(110, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
            assertEquals("Total: 110% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT23.5: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Sistema bloqueia tentativa de salvar a edição de uma avaliação com nome vazio")
    void CT23_6() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            inputNome.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);

            inputNome.sendKeys("");

            WebElement btnAtualizarAvaliacao = manageCoursePage.obterBotaoAtualizarAvaliacao();
            assertEquals("ATUALIZAR AVALIAÇÃO", btnAtualizarAvaliacao.getText());
            btnAtualizarAvaliacao.click();

            assertEquals("Preencha este campo.", capturarMensagemPadraoDeCampoVazioDoInput(inputNome));
            assertFalse(verificarValidadeDoInput(inputNome));

            Thread.sleep(5000);
            driver.navigate().refresh();
            Thread.sleep(4000);
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação para Editar", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("10%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT23.6: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Sistema bloqueia tentativa de salvar a edição de uma avaliação com nota vazia")
    void CT23_7() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "200");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");

            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            assertEquals("ADICIONAR AVALIAÇÃO", manageCoursePage.obterBotaoAdicionarAvaliacao().getText());

            manageCoursePage.clicarBotaoDeEditarAvaliacao(trAvaliacao);
            Thread.sleep(2000);
            Utilitarios.scrollarTela(js, "-300");

            WebElement inputNome = manageCoursePage.obterInputDeNomeDaAvaliacao();
            assertEquals("Avaliação para Editar", inputNome.getAttribute("value"));

            WebElement inputNota = manageCoursePage.obterInputDeNotaDaAvaliacao();
            assertEquals("10", inputNota.getAttribute("value"));

            inputNota.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            inputNota.sendKeys("");

            WebElement btnAtualizarAvaliacao = manageCoursePage.obterBotaoAtualizarAvaliacao();
            assertEquals("ATUALIZAR AVALIAÇÃO", btnAtualizarAvaliacao.getText());
            btnAtualizarAvaliacao.click();

            assertEquals("Preencha este campo.", capturarMensagemPadraoDeCampoVazioDoInput(inputNota));
            assertFalse(verificarValidadeDoInput(inputNota));

            Thread.sleep(5000);
            driver.navigate().refresh();
            Thread.sleep(4000);
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");

            trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("Avaliação para Editar");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("Avaliação para Editar", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("10%", elementosDentroDoTrAvaliacao.get(1).getText());

            assertEquals(80, manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());
            assertEquals("Total: 80% da nota final", manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT23.7: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 25/11/2025
     *
     */
    @Test
    @DisplayName("Excluir avaliação com sucesso em curso com mais de uma avaliação")
    void CT24() {
        try {
            Thread.sleep(5000);
            irAtePaginaMinhasAvaliacoes();

            WebElement divPrincipalDoCurso = minhasAvaliacoesPage.obterDivDasAvaliacoesDoCursoPorNomeDoCurso("Curso para " +
                    "Excluir Avaliação");

            assertEquals("Iuri da Silva Fernandes", minhasAvaliacoesPage.obterNomeDoAluno(divPrincipalDoCurso));

            WebElement h3ComBotaoQueAbreAvaliacoes = minhasAvaliacoesPage.obterElementoQueAbreAsAvaliacoesDoCurso(divPrincipalDoCurso);
            assertEquals("2 avaliações",
                    minhasAvaliacoesPage.obterQuantidadeTotalDeAvaliacoesPresentesNoCurso(h3ComBotaoQueAbreAvaliacoes));

            h3ComBotaoQueAbreAvaliacoes.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'MuiAccordionDetails')]")
            ));

            Thread.sleep(2000);
            divPrincipalDoCurso = minhasAvaliacoesPage.obterDivDasAvaliacoesDoCursoPorNomeDoCurso("Curso para Excluir" +
                    " Avaliação");

            WebElement divAvaliacaoA1 = minhasAvaliacoesPage.obterDivDaAvaliacao(divPrincipalDoCurso, "A1");

            assertEquals("A1", minhasAvaliacoesPage.obterTituloDaDivAvaliacao(divAvaliacaoA1));
            assertEquals("Sem nota", minhasAvaliacoesPage.obterNotaDaDivAvaliacao(divAvaliacaoA1));

            WebElement divAvaliacaoParaExcluir = minhasAvaliacoesPage.obterDivDaAvaliacao(divPrincipalDoCurso, "Avaliação para Excluir");

            assertEquals("Avaliação para Excluir", minhasAvaliacoesPage.obterTituloDaDivAvaliacao(divAvaliacaoParaExcluir));
            assertEquals("5", minhasAvaliacoesPage.obterNotaDaDivAvaliacao(divAvaliacaoParaExcluir));

            Utilitarios.scrollarTela(js, "500");
            Thread.sleep(2000);

            assertEquals("2,50",
                    minhasAvaliacoesPage.obterH5DaNotaTotal(divPrincipalDoCurso).getText());

            Thread.sleep(2000);

            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Curso para Excluir Avaliação");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");

            WebElement trAvaliacaoParaExcluir = manageCoursePage.
                    localizarLinhaDaAvaliacaoPorNome("Avaliação para Excluir");

            Utilitarios.centralizarElementoNaTela(trAvaliacaoParaExcluir, driver);

            manageCoursePage.clicarBotaoDeExcluirAvaliacao(trAvaliacaoParaExcluir);

            Thread.sleep(1000);
            irAtePaginaMinhasAvaliacoes();

            divPrincipalDoCurso = minhasAvaliacoesPage.obterDivDasAvaliacoesDoCursoPorNomeDoCurso("Curso para " +
                    "Excluir Avaliação");
            Utilitarios.centralizarElementoNaTela(divPrincipalDoCurso, driver);

            h3ComBotaoQueAbreAvaliacoes = minhasAvaliacoesPage.obterElementoQueAbreAsAvaliacoesDoCurso(divPrincipalDoCurso);
            assertEquals("1 avaliações",
                    minhasAvaliacoesPage.obterQuantidadeTotalDeAvaliacoesPresentesNoCurso(h3ComBotaoQueAbreAvaliacoes));

            h3ComBotaoQueAbreAvaliacoes.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'MuiAccordionDetails')]")
            ));

            Thread.sleep(2000);
            divPrincipalDoCurso = minhasAvaliacoesPage.obterDivDasAvaliacoesDoCursoPorNomeDoCurso("Curso para Excluir" +
                    " Avaliação");

            divAvaliacaoA1 = minhasAvaliacoesPage.obterDivDaAvaliacao(divPrincipalDoCurso, "A1");

            assertEquals("A1", minhasAvaliacoesPage.obterTituloDaDivAvaliacao(divAvaliacaoA1));
            assertEquals("Sem nota", minhasAvaliacoesPage.obterNotaDaDivAvaliacao(divAvaliacaoA1));

            Utilitarios.scrollarTela(js, "500");
            Thread.sleep(2000);

            assertEquals("0,00",
                    minhasAvaliacoesPage.obterH5DaNotaTotal(divPrincipalDoCurso).getText());

        } catch (Exception e) {
            System.err.println("Erro no CT24: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Excluir todas as avaliações de um curso faz mostrar mensagem ")
    void CT24_1() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Curso para Excluir Avaliação");
            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");

            WebElement trAvaliacao = manageCoursePage.localizarLinhaDaAvaliacaoPorNome("A1");
            Utilitarios.centralizarElementoNaTela(trAvaliacao, driver);

            List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));

            assertEquals("A1", elementosDentroDoTrAvaliacao.getFirst().getText());

            assertEquals("50%", elementosDentroDoTrAvaliacao.get(1).getText());


            assertEquals(50,
                    manageCoursePage.obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas());

            manageCoursePage.clicarBotaoDeExcluirAvaliacao(trAvaliacao);

            manageCoursePage.localizarEClicarNoMenuPorNome("Avaliações");
            Utilitarios.scrollarTela(js, "500");

            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//h6[contains(text(), 'Avaliações Cadastradas')]")))
                    .findElement(By.xpath(".."));

            assertTrue(
                    div.findElements(By.tagName("div")).stream().anyMatch(
                            element -> element.getText().equalsIgnoreCase("Nenhuma avaliação cadastrada. Adicione sua primeira " +
                                    "avaliação usando o formulário acima.")
                    )
            );

            assertEquals("Total: 0% da nota final",
                    manageCoursePage.obterTextoDoPercentualTotalDaNotaFinal());

        } catch (Exception e) {
            System.err.println("Erro no CT24.1: " + e.getMessage());
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

    private void irAtePaginaMinhasAvaliacoes() {
        dashboardPage.abrirMenuMinhasAvaliacoes();
        wait.until(ExpectedConditions.urlContains("/minhas-avaliacoes"));
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
