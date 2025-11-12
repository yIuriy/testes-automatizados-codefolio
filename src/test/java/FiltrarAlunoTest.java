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

            WebElement trAlunos = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tbody")
            ));

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

            WebElement trAlunos = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tbody")
            ));

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

            WebElement trAlunos = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tbody")
            ));

            for (WebElement e : trAlunos.findElements(By.tagName("tr"))) {
                WebElement td = e.findElements(By.tagName("td")).get(3);
                assertEquals("Estudante", td.getText());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

