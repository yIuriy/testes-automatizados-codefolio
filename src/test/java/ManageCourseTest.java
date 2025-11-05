import io.github.bonigarcia.wdm.WebDriverManager;
import model.Aluno;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void CT17() {
        try {
            Thread.sleep(5000);
            dashboardPage.abrirMenuDeOpcoesPerfil();
            dashboardPage.abrirMenuGerenciamentoDeCursos();
            wait.until(ExpectedConditions.urlContains("/manage-courses"));
            manageCoursePage.clicarBotaoGerenciarCurso();
            manageCoursePage.localizarEClicarNoMenuAlunos();
            manageCoursePage.inserirTextoNoFiltrar("Iuri");

            WebElement trAluno = manageCoursePage.localizarLinhaDoAlunoPorNome("Iuri");
            assertNotNull(trAluno);

            List<WebElement> elementosDentroDoTrAluno = trAluno.findElements(By.cssSelector("th, td"));
            Aluno aluno = new Aluno(
                    elementosDentroDoTrAluno.getFirst().getText(),
                    elementosDentroDoTrAluno.get(1).getText(),
                    elementosDentroDoTrAluno.get(2).getText(),
                    elementosDentroDoTrAluno.get(3).getText(),
                    elementosDentroDoTrAluno.get(4).getText()
            );

            assertEquals("Iuri Da Silva Fernandes", aluno.nome());
            assertEquals("iurifernandes.aluno@unipampa.edu.br", aluno.email());
            assertEquals("100%", aluno.progresso());
            assertEquals("Concluído", aluno.status());
            assertEquals("Admin", aluno.role());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
