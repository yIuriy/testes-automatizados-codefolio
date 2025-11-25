import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.Authentication;

import java.time.Duration;

public class SlideCourseTest {
    WebDriver driver;
    Authentication authentication;
    ManageCoursePage manageCoursePage;
    SlidePage slidePage;
    WebDriverWait wait;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        manageCoursePage = new ManageCoursePage(driver);
        slidePage = new SlidePage(driver);
        dashboardPage = new DashboardPage(driver);
        authentication.realizarLoginViaIndexedBD();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    @Test
    void adicionarFurretExtra() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Slides");
            slidePage.inserirTituloNoSlide("Furret Extra");
            slidePage.inserirLinkDoSlide("https://docs.google.com/presentation/d/e/2PACX-1vQj0RqOKJlzEPMf57kLonB8rfwuWv6JRMmIpqRkhpxRcFPJyI8oz6KWqpG7FKSS9LQViN2PsuSoWGnp/pub?start=false&loop=false&delayms=3000");
            slidePage.inserirDescricaoNoSlide("Descrição Furret");
            slidePage.clicarBotaoAdicionarSlide();
            slidePage.verificarSeSlideFoiAdicionado();
            slidePage.clicarBotaoOkSlide();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void CT09() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Slides");
            slidePage.clicarBotaoEditarSlideDoPrimeiroSlide();
            slidePage.inserirTituloNoSlide("Furret Extra");
            slidePage.inserirLinkDoSlide("https://docs.google.com/presentation/d/e/2PACX-1vQj0RqOKJlzEPMf57kLonB8rfwuWv6JRMmIpqRkhpxRcFPJyI8oz6KWqpG7FKSS9LQViN2PsuSoWGnp/pub?start=false&loop=false&delayms=3000");
            slidePage.inserirDescricaoNoSlide("Descrição Furret");
            slidePage.clicarBotaoSalvarAlteracoes();
            slidePage.verificarSeSlideFoiAtualizado();
            slidePage.clicarBotaoOkSlide();
            System.out.println("O Slide foi editado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void CT010() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Slides");
            slidePage.clicarBotaoExcluirSlideDoPrimeiroSlide();
            slidePage.clicarBotaoExcluir();
            slidePage.verificarSeSlideFoiExcluido();
            System.out.println("O Slide foi excluído com sucesso!");
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