import com.sun.source.tree.AssertTree;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
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
    void adicionarFurretExtra() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Slides");
            manageSlidePage.inserirTituloNoSlide("Furret Extra");
            manageSlidePage.inserirLinkDoSlide("https://docs.google.com/presentation/d/e/2PACX-1vQj0RqOKJlzEPMf57kLonB8rfwuWv6JRMmIpqRkhpxRcFPJyI8oz6KWqpG7FKSS9LQViN2PsuSoWGnp/pub?start=false&loop=false&delayms=3000");
            manageSlidePage.inserirDescricaoNoSlide("Descrição Furret");
            manageSlidePage.clicarBotaoAdicionarSlide();
            manageSlidePage.verificarSeSlideFoiAdicionado();
            manageSlidePage.clicarBotaoOkSlide();

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
            manageSlidePage.clicarBotaoEditarSlideDoPrimeiroSlide();
            manageSlidePage.inserirTituloNoSlide("Furret Extra");
            manageSlidePage.inserirLinkDoSlide("https://docs.google.com/presentation/d/e/2PACX-1vQj0RqOKJlzEPMf57kLonB8rfwuWv6JRMmIpqRkhpxRcFPJyI8oz6KWqpG7FKSS9LQViN2PsuSoWGnp/pub?start=false&loop=false&delayms=3000");
            manageSlidePage.inserirDescricaoNoSlide("Descrição Furret");
            manageSlidePage.clicarBotaoSalvarAlteracoes();
            manageSlidePage.verificarSeSlideFoiAtualizado();
            manageSlidePage.clicarBotaoOkSlide();
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
            manageSlidePage.clicarBotaoExcluirSlideDoPrimeiroSlide();
            manageSlidePage.clicarBotaoExcluir();
            manageSlidePage.verificarSeSlideFoiExcluido();
            System.out.println("O Slide foi excluído com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void CT011() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Materiais Extras");
            manageSlidePage.inserirTituloMaterialExtra("Wiki Furret");
            manageSlidePage.inserirLinkMaterialExtra("https://bulbapedia.bulbagarden.net/wiki/Furret_(Pok%C3%A9mon)");
            manageSlidePage.clicarBotaoAdicionarMaterial();
            manageSlidePage.verificarSeMaterialFoiAdicionado();
            manageSlidePage.clicarBotaoOkMaterial();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // O sistema não permite realizar alterações em um material extra após ele ser adicionado
    @Test
    void CT012() {
        System.out.println("CT012");
    }

    private void irAteAPaginaDeGerenciarCursos() {
        dashboardPage.abrirMenuDeOpcoesPerfil();
        dashboardPage.abrirMenuGerenciamentoDeCursos();
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }
}