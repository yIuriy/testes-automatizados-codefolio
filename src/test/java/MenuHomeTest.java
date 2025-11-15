import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import model.Aluno;
import pages.DashboardPage;
import pages.MenuHomePage;
import utils.Authentication;

public class MenuHomeTest {

    WebDriver driver;
    Authentication authentication;
    MenuHomePage menuHomePage;
    WebDriverWait wait;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        menuHomePage = new MenuHomePage(driver);
        dashboardPage = new DashboardPage(driver);
        authentication.realizarLoginViaIndexedBD();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    // Reprovou
    @Test
    void CT40_1() {
        
        //   Objetivo: Verificar se o sistema computa corretamente o like, cancelamento do
        //  like, dislike e cancelamento do dislike do estudante diretamente pela página “Home”.

        try {
            Thread.sleep(4000);
            irAteAHome();

            int numLikesInicial = menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)");

            menuHomePage.clicarEmLike("Selenium (Testes Automatizados)");
            Thread.sleep(3000);
            assertEquals(numLikesInicial + 1, menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)"));

            menuHomePage.clicarEmLike("Selenium (Testes Automatizados)");
            Thread.sleep(3000);
            assertEquals(numLikesInicial, menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)"));       

            menuHomePage.clicarEmDislike("Selenium (Testes Automatizados)");
            Thread.sleep(3000);
            assertEquals(numLikesInicial -1, menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)"));       
            
            menuHomePage.clicarEmDislike("Selenium (Testes Automatizados)");
            Thread.sleep(3000);
            assertEquals(numLikesInicial, menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)"));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Reprovou
    @Test
    void CT40_2() {
        
        //   Objetivo: Verificar se o sistema computa corretamente a mudança direta de 
        //  like para dislike e vice-versa, do estudante diretamente pela página “Home”.

        try {
            Thread.sleep(4000);
            irAteAHome();

            int numLikesInicial = menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)");

            menuHomePage.clicarEmLike("Selenium (Testes Automatizados)");
            Thread.sleep(3000);

            menuHomePage.clicarEmDislike("Selenium (Testes Automatizados)");
            Thread.sleep(3000);
            assertEquals(numLikesInicial -1, menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)"));       
            
            menuHomePage.clicarEmLike("Selenium (Testes Automatizados)");
            Thread.sleep(3000);
            assertEquals(numLikesInicial +1, menuHomePage.pegarNumeroDeLikes("Selenium (Testes Automatizados)"));       

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Reprovou
    @Test
    void CT41() {
        
        //   Objetivo: Verificar se o sistema permite a adição de um comentário ao vídeo.

        try {
            Thread.sleep(4000);
            irAteAHome();

            String texto = "Comentário de Teste grupo1";
            String titulo = "Selenium (Testes Automatizados)";

            menuHomePage.clicarEmComentarios(titulo);
            Thread.sleep(2000);

            menuHomePage.comentar(titulo, texto);
            Thread.sleep(1500);

            String retorno = menuHomePage.visualizarComentario(titulo, texto);

            assertNotNull(retorno);

            if (!retorno.isEmpty()){
            assertEquals(texto, retorno);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT42(){

        //   Objetivo: Verificar se o botão de compartilhar cola o Link do vídeo na área de transferência.

        try{
        Thread.sleep(4000);
        irAteAHome();
            menuHomePage.clicarEmCompartilhar("Selenium (Testes Automatizados)");
            String areaDeTransferencia = menuHomePage.pegarTextoDaAreaDeTransferencia();

            assertEquals("https://youtu.be/Fw9YW5_MZRs", areaDeTransferencia);

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT43(){

        // Objetivo: Verificar se o sistema permite o acesso a cursos que não exigem PIN.

        try{
            Thread.sleep(4000);
            irAteAHome();

            menuHomePage.acessarCursoSemPIN();
            Thread.sleep(4000);
            assertTrue(menuHomePage.verificarSeEntrouNoCurso());


        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    private void irAteAHome() {
        wait.until(ExpectedConditions.urlContains("/"));
    }
}
