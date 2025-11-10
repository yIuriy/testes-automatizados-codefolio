import io.github.bonigarcia.wdm.WebDriverManager;
import model.Aluno;
import org.jetbrains.annotations.NotNull;
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

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageCourseTest {
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

    // Passou
    @Test
    void CT17() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            manageCoursePage.inserirTextoNoFiltrar("Iuri");

            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Iuri");
            assertNotNull(trAluno);

            List<WebElement> elementosDentroDoTrAluno = trAluno.findElements(By.cssSelector("th, td"));

            Aluno aluno = construirAlunoAPartirDoTableRow(elementosDentroDoTrAluno);

            assertEquals("Iuri Da Silva Fernandes", aluno.nome());
            assertEquals("iurifernandes.aluno@unipampa.edu.br", aluno.email());
            assertEquals("100%", aluno.progresso());
            assertEquals("Concluído", aluno.status());
            assertEquals("Admin", aluno.role());
            assertTrue(manageCoursePage.verificarSeExisteIconeDeDeletarAluno(elementosDentroDoTrAluno), "Verifica se " +
                    "existe o ícone de deletar na linha do Aluno em questão");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT17_1() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            manageCoursePage.inserirTextoNoFiltrar("Iuri");
            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Iuri");
            assertNotNull(trAluno);

            List<WebElement> elementosDentroDoTrAluno = trAluno.findElements(By.cssSelector("th, td"));

            Aluno aluno1 = construirAlunoAPartirDoTableRow(elementosDentroDoTrAluno);

            assertEquals("Iuri Da Silva Fernandes", aluno1.nome());
            assertEquals("iurifernandes.aluno@unipampa.edu.br", aluno1.email());
            assertEquals("100%", aluno1.progresso());
            assertEquals("Concluído", aluno1.status());
            assertEquals("Admin", aluno1.role());
            assertTrue(manageCoursePage.verificarSeExisteIconeDeDeletarAluno(elementosDentroDoTrAluno), "Verifica se " +
                    "existe o ícone de deletar na linha do Aluno em questão");

            manageCoursePage.limparTextoNoFiltrar();
            manageCoursePage.inserirTextoNoFiltrar("Dyonathan");
            trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Dyonathan");
            assertNotNull(trAluno);

            elementosDentroDoTrAluno = trAluno.findElements(By.cssSelector("th, td"));

            Aluno aluno2 = construirAlunoAPartirDoTableRow(elementosDentroDoTrAluno);

            assertEquals("Dyonathan Bento Laner", aluno2.nome());
            assertEquals("dyonathanlaner.aluno@unipampa.edu.br", aluno2.email());
            assertEquals("0%", aluno2.progresso());
            assertEquals("Em Progresso", aluno2.status());
            assertEquals("Estudante", aluno2.role());
            assertTrue(manageCoursePage.verificarSeExisteIconeDeDeletarAluno(elementosDentroDoTrAluno), "Verifica se " +
                    "existe o ícone de deletar na linha do Aluno em questão");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void VT17_2() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Curso sem Alunos");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            js.executeScript("window.scrollBy({top: 500})");

            WebElement p = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains" +
                    "(normalize-space(), 'Exibindo')]")));
            List<WebElement> div = p.findElement(By.xpath("..")).findElements(By.tagName("p"));

            String numeroDeAlunosExibidos = div.get(0).getText();
            String msgNenhumAlunoMatriculado = div.get(1).getText();

            assertEquals("Exibindo 0 de 0 estudantes", numeroDeAlunosExibidos);
            assertEquals("Nenhum estudante matriculado neste curso.", msgNenhumAlunoMatriculado);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT17_3() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");

            WebElement p = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains" +
                    "(normalize-space(), 'Exibindo')]")));

            String[] split = p.getText().split(" ");
            int alunosExibidosNoMomento = Integer.parseInt(split[1]);
            int totalDeAlunosExistentes = Integer.parseInt(split[3]);

            List<WebElement> linhasDosAlunos = driver.findElements(By.xpath("//tr[contains(@class, " +
                    "'MuiTableRow-hover')]"));

            assertEquals(alunosExibidosNoMomento, linhasDosAlunos.size());
            assertEquals(totalDeAlunosExistentes, linhasDosAlunos.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @NotNull
    private Aluno construirAlunoAPartirDoTableRow(List<WebElement> elementosDentroDoTrAluno) {
        return new Aluno(
                elementosDentroDoTrAluno.getFirst().getText(),
                elementosDentroDoTrAluno.get(1).getText(),
                elementosDentroDoTrAluno.get(2).getText(),
                elementosDentroDoTrAluno.get(3).getText(),
                elementosDentroDoTrAluno.get(4).getText()
        );
    }

    private void irAteAPaginaDeGerenciarCursos() {
        dashboardPage.abrirMenuDeOpcoesPerfil();
        dashboardPage.abrirMenuGerenciamentoDeCursos();
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }

    @Test
    @DisplayName("Clicar para Excluir aluno e clicar em Confirmar")
    void CT18() {
        try {
            Thread.sleep(5000);
            String nomeDoAluno = "Zildo Tester Java";
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Curso para Deletar Alunos");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            js.executeScript("window.scrollBy({top: 500})");
            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome(nomeDoAluno);
            manageCoursePage.clicarIconeDeExcluirAluno(trAluno);
            manageCoursePage.clicarBotaoConfirmarExclusaoDeAluno();

            Thread.sleep(2000);
            driver.navigate().refresh();
            Thread.sleep(2000);
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Thread.sleep(1000);
            js.executeScript("window.scrollBy({top: 500})");

            // Não deve localizar o aluno, lançando uma exceção
            assertThrows(TimeoutException.class, () -> {
                manageCoursePage.localizarLinhaDoAlunoPorNome(nomeDoAluno);
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Clicar para Excluir aluno, mas não clicar em confirmar")
    void CT18_1() {
        try {
            Thread.sleep(5000);
            String nomeDoAluno = "Zildo Tester Java";
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Curso para Deletar Alunos");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            js.executeScript("window.scrollBy({top: 500})");
            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome(nomeDoAluno);
            manageCoursePage.clicarIconeDeExcluirAluno(trAluno);
            manageCoursePage.clicarBotaoCancelarExclusaoDeAluno();

            Thread.sleep(2000);
            driver.navigate().refresh();
            Thread.sleep(2000);
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Thread.sleep(1000);
            js.executeScript("window.scrollBy({top: 500})");

            WebElement trAlunoAposCancelamentoExclusao = manageCoursePage.localizarLinhaDoAlunoPorNome(nomeDoAluno);

            assertNotNull(trAlunoAposCancelamentoExclusao);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Clicar para Excluir aluno, e verificar se o prompt exibe o nome do aluno a ser excluído corretamente")
    void CT18_2() {
        try {
            Thread.sleep(5000);
            String nomeDoAluno = "Zildo Tester Java";
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Curso para Deletar Alunos");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            js.executeScript("window.scrollBy({top: 500})");
            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome(nomeDoAluno);
            manageCoursePage.clicarIconeDeExcluirAluno(trAluno);

            String textoExibido = manageCoursePage.localizarCaixaDeTextoConfirmarCancelarExclusao().findElement(By.tagName("p")).getText();

            assertEquals("Tem certeza que deseja remover Zildo Tester Java do curso?", textoExibido);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void CT25() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();

            WebElement abaAvaliacoes = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Avaliações')]"))
            );
            abaAvaliacoes.click();
            WebElement botaoAtribuirNota = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Atribuir Nota')]"))
            );
            botaoAtribuirNota.click();
            WebElement linhaIuri = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tr[.//text()[contains(.,'Iuri Da Silva Fernandes')]]")
            ));
            WebElement campoNota = linhaIuri.findElement(By.xpath(".//input"));
            campoNota.clear();
            campoNota.sendKeys("6.5");
            js.executeScript("arguments[0].blur();", campoNota);
            WebElement iconeCheck = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath(".//*[name()='svg' and @data-testid='CheckCircleIcon']")
            ));
            assertTrue(iconeCheck.isDisplayed(), "O ícone de confirmação deve aparecer após salvar a nota.");

            System.out.println("CT25 passou");

        } catch (Exception e) {
            System.out.println("Falha no CT25 ");
        }
    }

    @Test
    void CT25_1() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();

            WebElement abaAvaliacoes = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Avaliações')]"))
            );
            abaAvaliacoes.click();
            WebElement botaoAtribuirNota = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Atribuir Nota')]"))
            );
            botaoAtribuirNota.click();
            WebElement linhaAluno = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tr[.//text()[contains(.,'Gustavo Borges Arrussul Veiga')]]")
            ));
            WebElement campoNota = linhaAluno.findElement(By.xpath(".//input"));
            String notaAnterior = campoNota.getAttribute("value");
            campoNota.click();
            campoNota.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            campoNota.sendKeys("8.0");
            js.executeScript("arguments[0].blur();", campoNota);
            WebElement iconeCheck = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath(".//*[name()='svg' and @data-testid='CheckCircleIcon']"))
            );
            assertTrue(iconeCheck.isDisplayed(), "Ícone de sucesso deve aparecer após atualizar a nota.");
            String notaAtualizada = campoNota.getAttribute("value");
            assertEquals("8.0", notaAtualizada, "A nota do aluno deve ter sido atualizada para 8.0");

            System.out.println("CT25.1 passou, nota alterada com sucesso.");

        } catch (Exception e) {
            System.out.println("Falha no CT25.1");
        }
    }

    @Test
    void CT25_2_naoPermitirCampoNotaVazio() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();

            WebElement abaAvaliacoes = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Avaliações')]"))
            );
            abaAvaliacoes.click();
            WebElement botaoAtribuirNota = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Atribuir Nota')]"))
            );
            botaoAtribuirNota.click();
            WebElement linhaAluno = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tr[.//text()[contains(.,'Gustavo Borges Arrussul Veiga')]]")
            ));
            WebElement campoNota = linhaAluno.findElement(By.xpath(".//input"));
            String notaAnterior = campoNota.getAttribute("value");

            campoNota.click();
            campoNota.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            campoNota.sendKeys(Keys.DELETE);

            js.executeScript("arguments[0].blur();", campoNota);

            Thread.sleep(1500);
            String valorDepois = campoNota.getAttribute("value");

            driver.navigate().back();
            WebElement abaAvaliacoes2 = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Avaliações')]"))
            );
            abaAvaliacoes2.click();

            WebElement botaoAtribuirNota2 = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Atribuir Nota')]"))
            );
            botaoAtribuirNota2.click();

            WebElement linhaAluno2 = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tr[.//text()[contains(.,'Gustavo Borges Arrussul Veiga')]]")
            ));

            WebElement campoNota2 = linhaAluno2.findElement(By.xpath(".//input"));
            String notaAtualAposVoltar = campoNota2.getAttribute("value");

            assertEquals(notaAnterior, notaAtualAposVoltar,
                    "A nota anterior deve permanecer após tentar apagar e voltar à página.");

            System.out.println("CT25.2 passou: campo vazio não foi salvo.");

        } catch (Exception e) {
            System.out.println("Falha no CT25.2");
        }
    }
}
