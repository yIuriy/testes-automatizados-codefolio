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

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class AlterarRoleTest {

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
     * Resultado: <strong>Falhou</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Alterar função de Estudante para Professor")
    void CT21() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");

            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Zildo Tester Java"
            );
            Utilitarios.centralizarElementoNaTela(trAluno, driver);

            String roleDoAluno = manageCoursePage.obterValorRole(trAluno);
            assertEquals("Estudante", roleDoAluno);

            manageCoursePage.alterarRole(trAluno, "Professor");

            driver.navigate().refresh();
            Thread.sleep(2000);
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");

            trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Zildo Tester Java"
            );

            Utilitarios.centralizarElementoNaTela(trAluno, driver);

            String novoRoleDoAluno = manageCoursePage.obterValorRole(trAluno);
            assertEquals("Professor", novoRoleDoAluno);

        } catch (Exception e) {
            System.err.println("Erro no CT21: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Inconclusivo</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Alterar função de Professor para Estudante")
    void CT21_1() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");

            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Zildo Tester Java"
            );
            Utilitarios.centralizarElementoNaTela(trAluno, driver);

            String roleDoAluno = manageCoursePage.obterValorRole(trAluno);
            assertEquals("Professor", roleDoAluno);

            manageCoursePage.alterarRole(trAluno, "Estudante");

            driver.navigate().refresh();
            Thread.sleep(2000);
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");

            trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Zildo Tester Java"
            );

            Utilitarios.centralizarElementoNaTela(trAluno, driver);

            String novoRoleDoAluno = manageCoursePage.obterValorRole(trAluno);
            assertEquals("Estudante", novoRoleDoAluno);

        } catch (Exception e) {
            System.err.println("Erro no CT21.1: " + e.getMessage());
        }
    }

    /**
     * Autor: Iuri da Silva Fernandes<br>
     * Resultado: <strong>Passou</strong><br>
     * Data de execução: 19/11/2025
     *
     */
    @Test
    @DisplayName("Não permitir alterar o role de um admin")
    void CT21_2() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Teste");
            manageCoursePage.localizarEClicarNoMenuPorNome("Alunos");

            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Iuri Da Silva Fernandes"
            );
            Utilitarios.centralizarElementoNaTela(trAluno, driver);

            String roleDoAluno = manageCoursePage.obterValorRole(trAluno);
            assertEquals("Admin", roleDoAluno);

            // Tenta alterar o role, lançando uma exceção, pois o menu de opções da role não aparece para ser clicado
            assertThrows(TimeoutException.class, () -> {
                manageCoursePage.alterarRole(trAluno, "Estudante");
            });

            WebElement inputRole = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@value, 'admin')]")
            ));

            // Verifica se o input de role está disabled
            assertFalse(inputRole.isEnabled());

        } catch (Exception e) {
            System.err.println("Erro no CT21.2: " + e.getMessage());
        }
    }
}