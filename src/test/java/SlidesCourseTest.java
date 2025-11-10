import io.github.bonigarcia.wdm.WebDriverManager;
import model.Aluno;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.Authentication;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SlidesCourseTest {
    WebDriver driver;
    Authentication authentication;
    ManageCoursePage manageCoursePage;
    ManageSlidePage manageSlidePage;
    WebDriverWait wait;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        manageCoursePage = new ManageCoursePage(driver);
        manageSlidePage = new ManageSlidePage(driver);
        dashboardPage = new DashboardPage(driver);
        authentication.realizarLoginViaIndexedBD();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    @Test
    void CT09() {
        try {
            Thread.sleep(10000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Slides");
            manageSlidePage.clicarBotaoEditarSlideDoPrimeiroSlide();
            manageSlidePage.inserirTituloNoSlide("Furret");

            manageSlidePage.inserirLinkDoSlide("https://docs.google.com/presentation/d/e/2PACX-1vQj0RqOKJlzEPMf57kLonB8rfwuWv6JRMmIpqRkhpxRcFPJyI8oz6KWqpG7FKSS9LQViN2PsuSoWGnp/pub?start=false&loop=false&delayms=3000");

            manageSlidePage.inserirDescricaoNoSlide("Descrição Furret");

            manageSlidePage.clicarBotaoSalvarAlteracoes();

            manageSlidePage.verificarSeSlideFoiAtualizado();
            assertTrue(true);
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