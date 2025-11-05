import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
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
import utils.Utilitarios;

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
            Thread.sleep(10000);
            dashboardPage.abrirMenuDeOpcoesPerfil();
            dashboardPage.abrirMenuGerenciamentoDeCursos();
            wait.until(ExpectedConditions.urlContains("/manage-courses"));
            manageCoursePage.clicarBotaoGerenciarCurso();
            manageCoursePage.localizarEClicarNoMenuAlunos();

            boolean encontrado = false;
            WebElement trAluno = null;
            WebElement nomeAluno = null;
            WebElement emailAluno = null;
            while (!encontrado) {
                try {
//                    nomeAluno = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                            By.xpath("//*[contains(text(),'Iuri Da Silva Fernandes')]")
//                    ));
                    trAluno = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//tr[.//p[contains(text(),'Iuri da Silva Fernandes')]]")
                    ));
                    encontrado = true;
                } catch (Exception e) {
                    js.executeScript("window.scrollBy(0, 200)");
                }
            }

            List<WebElement> elementosDentroDoTrAluno = trAluno.findElements(By.tagName("td"));
            for (WebElement element : elementosDentroDoTrAluno) {
                System.out.println(element.getText());
            }

//            emailAluno = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//*[contains(text(), 'iurifernandes@aluno.unipampa.edu.br')]")
//            ));

            assertNotNull(nomeAluno);
            Utilitarios.centralizarElementoNaTela(nomeAluno, driver);
            assertEquals("Iuri Da Silva Fernandes", nomeAluno.getText());
//            assertNotNull(emailAluno);
//            assertEquals("iurifernandes@aluno.unipampa.edu.br", emailAluno.getText());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
