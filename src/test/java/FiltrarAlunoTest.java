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

public class FiltrarAlunoTest {
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
    @DisplayName("Filtrar alunos Iniciantes")
    void CT20() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.filtrarAlunosPorProgresso("Iniciante (0-24%)");

            WebElement trAlunos = manageCoursePage.obterTableBodyDosAlunosExibidos();

            for (WebElement e : trAlunos.findElements(By.tagName("tr"))) {
                WebElement td = e.findElements(By.tagName("td")).get(1);
                assertEquals("0%", td.getText());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    @DisplayName("Filtrar alunos que concluíram o curso")
    void CT20_1() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.filtrarAlunosPorProgresso("Concluído (100%)");

            WebElement trAlunos = manageCoursePage.obterTableBodyDosAlunosExibidos();

            for (WebElement e : trAlunos.findElements(By.tagName("tr"))) {
                WebElement td = e.findElements(By.tagName("td")).get(1);
                assertEquals("100%", td.getText());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    // Passou
    @Test
    @DisplayName("Filtrar alunos por progresso utilizando uma opção que não retornará alunos")
    void CT20_2() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.filtrarAlunosPorProgresso("Avançado");

            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//p[contains(text(), 'Nenhum')]")
            )).findElement(By.xpath(".."));

            assertEquals("Nenhum estudante corresponde aos filtros aplicados.",
                    div.findElement(By.tagName("p")).getText());

            assertEquals("LIMPAR FILTROS",
                    div.findElement(By.tagName("button")).getText());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Falhou
    @Test
    @DisplayName("Filtrar alunos por role de Estudante")
    void CT20_3() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.filtrarAlunosPorRole("Estudante");

            WebElement trAlunos = manageCoursePage.obterTableBodyDosAlunosExibidos();

            for (WebElement e : trAlunos.findElements(By.tagName("tr"))) {
                WebElement td = e.findElements(By.tagName("td")).get(3);
                assertEquals("Estudante", td.getText());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Falhou
    @Test
    @DisplayName("Filtrar alunos por role de Admin")
    void CT20_4() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.filtrarAlunosPorRole("Admin");


            assertDoesNotThrow(() -> {
                WebElement trAlunos = manageCoursePage.obterTableBodyDosAlunosExibidos();

                for (WebElement e : trAlunos.findElements(By.tagName("tr"))) {
                    WebElement td = e.findElements(By.tagName("td")).get(3);
                    assertEquals("Admin", td.getText());
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Filtrar alunos por role utilizando uma opção que não retornará alunos")
    void CT20_5() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.filtrarAlunosPorRole("Professor");

            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//p[contains(text(), 'Nenhum')]")
            )).findElement(By.xpath(".."));

            assertEquals("Nenhum estudante corresponde aos filtros aplicados.",
                    div.findElement(By.tagName("p")).getText());

            assertEquals("LIMPAR FILTROS",
                    div.findElement(By.tagName("button")).getText());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    @DisplayName("Verificar se a ordenação de A-Z funciona")
    void CT20_6() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.clicarEmOrdenarDeAZ();

            WebElement tbody = manageCoursePage.obterTableBodyDosAlunosExibidos();

            String nomePrimeiroAluno = tbody.findElements(By.tagName("tr")).getFirst().findElement(By.tagName("th")).getText();
            String nomeUltimoAluno =
                    tbody.findElements(By.tagName("tr")).getLast().findElement(By.tagName("th")).getText();

            assertEquals("Amanda Dias De Souza", nomePrimeiroAluno);
            assertEquals("Zildo Tester Java", nomeUltimoAluno);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    @DisplayName("Verificar se a ordenação de Z-A funciona")
    void CT20_7() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.clicarEmOrdenarDeZA();

            WebElement tbody = manageCoursePage.obterTableBodyDosAlunosExibidos();

            String nomePrimeiroAluno = tbody.findElements(By.tagName("tr")).getFirst().findElement(By.tagName("th")).getText();
            String nomeUltimoAluno =
                    tbody.findElements(By.tagName("tr")).getLast().findElement(By.tagName("th")).getText();

            assertEquals("Zildo Tester Java", nomePrimeiroAluno);
            assertEquals("Amanda Dias De Souza", nomeUltimoAluno);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    @DisplayName("Filtrando aluno por nome único")
    void CT20_8() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.inserirTextoNoFiltrar("Zildo");

            List<WebElement> trAlunos = manageCoursePage.obterTableBodyDosAlunosExibidos().
                    findElements(By.tagName("tr"));

            assertEquals(1, trAlunos.size());

            assertEquals("Zildo Tester Java",
                    trAlunos.getFirst().findElement(By.tagName("th")).getText());

            assertEquals("zildotesterjava@gmail.com",
                    trAlunos.getFirst().findElement(By.tagName("td")).getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    @DisplayName("Filtrando aluno por nome em comum entre dois alunos diferentes")
    void CT20_9() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.inserirTextoNoFiltrar("Gabriel");

            List<WebElement> trAlunos = manageCoursePage.obterTableBodyDosAlunosExibidos().
                    findElements(By.tagName("tr"));

            assertEquals(2, trAlunos.size());

            assertEquals("Gabriel Camargo Ortiz",
                    trAlunos.getFirst().findElement(By.tagName("th")).getText());

            assertEquals("Gabriel Dutra Martinez",
                    trAlunos.getLast().findElement(By.tagName("th")).getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Passou
    @Test
    @DisplayName("Filtrando aluno com nome inexistente")
    void CT20_10() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoDoPrimeiroCurso();
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");
            Utilitarios.scrollarTela(js, "500");

            manageCoursePage.inserirTextoNoFiltrar("Homem-Aranha");

            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//p[contains(text(), 'Nenhum')]")
            )).findElement(By.xpath(".."));

            assertEquals("Nenhum estudante corresponde aos filtros aplicados.",
                    div.findElement(By.tagName("p")).getText());

            assertEquals("LIMPAR FILTROS",
                    div.findElement(By.tagName("button")).getText());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

