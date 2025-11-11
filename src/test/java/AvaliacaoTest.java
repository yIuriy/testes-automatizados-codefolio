import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    // Passou
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

    private void irAteAPaginaDeGerenciarCursos() {
        dashboardPage.abrirMenuDeOpcoesPerfil();
        dashboardPage.abrirMenuGerenciamentoDeCursos();
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }

}
