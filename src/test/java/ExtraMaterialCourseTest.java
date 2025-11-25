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

public class ExtraMaterialCourseTest {
    WebDriver driver;
    Authentication authentication;
    ManageCoursePage manageCoursePage;
    ExtraMaterialPage extraMaterialPage;
    WebDriverWait wait;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        manageCoursePage = new ManageCoursePage(driver);
        extraMaterialPage = new ExtraMaterialPage(driver);
        dashboardPage = new DashboardPage(driver);
        authentication.realizarLoginViaIndexedBD();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    @Test
    void CT011() {
        try {
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Materiais Extras");
            extraMaterialPage.inserirTituloMaterialExtra("Wiki Furret");
            extraMaterialPage.inserirLinkMaterialExtra("https://bulbapedia.bulbagarden.net/wiki/Furret_(Pok%C3%A9mon)");
            extraMaterialPage.clicarBotaoAdicionarMaterial();
            extraMaterialPage.verificarSeMaterialFoiAdicionado();
            extraMaterialPage.clicarBotaoOkMaterial();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // O sistema não permite realizar alterações em um material extra após ele ser adicionado
    @Test
    void CT012() {
        System.out.println("CT012");
    }

    @Test
    void CT013() {
        try{
            Thread.sleep(5000);
            irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Materiais Extras");
            extraMaterialPage.clicarBotaoExcluirDoPrimeiroMaterial();
            extraMaterialPage.clicarBotaoConfirmarExclusaoMaterial();
            extraMaterialPage.verificarSeMaterialFoiExcluido();
            System.out.println("O Material foi excluído com sucesso!");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void irAteAPaginaDeGerenciarCursos() {
        dashboardPage.abrirMenuDeOpcoesPerfil();
        dashboardPage.abrirMenuGerenciamentoDeCursos();
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }
}
