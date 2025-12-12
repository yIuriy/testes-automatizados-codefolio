import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
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
import pages.MenuHomePage;
import utils.Authentication;

import java.time.Duration;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

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
    @AfterEach
    void teardown() {
        if (driver != null) driver.quit();
    }

    // Reprovou
    @Test
    void CT40_1() {

        // Objetivo: Verificar se o sistema computa corretamente o like, cancelamento do
        // like, dislike e cancelamento do dislike do estudante diretamente pela página
        // “Home”.

        try {
            Thread.sleep(4000);
            irAteAHome();
            String titulo = "Selenium (Testes Automatizados)";

            int numLikesInicial = menuHomePage.pegarNumeroDeLikes(titulo);

            menuHomePage.clicarEmLike(titulo);
            Thread.sleep(1000);
            assertEquals(numLikesInicial + 1, menuHomePage.pegarNumeroDeLikes(titulo));

            menuHomePage.clicarEmLike(titulo);
            Thread.sleep(1000);
            assertEquals(numLikesInicial, menuHomePage.pegarNumeroDeLikes(titulo));

            menuHomePage.clicarEmDislike(titulo);
            Thread.sleep(1000);
            assertEquals(numLikesInicial - 1, menuHomePage.pegarNumeroDeLikes(titulo));

            menuHomePage.clicarEmDislike(titulo);
            Thread.sleep(1000);
            assertEquals(numLikesInicial, menuHomePage.pegarNumeroDeLikes(titulo));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Reprovou
    @Test
    void CT40_2() {

        // Objetivo: Verificar se o sistema computa corretamente a mudança direta de
        // like para dislike e vice-versa, do estudante diretamente pela página “Home”.

        try {
            Thread.sleep(4000);
            irAteAHome();

            String titulo = "Selenium (Testes Automatizados)";

            int numLikesInicial = menuHomePage.pegarNumeroDeLikes(titulo);

            menuHomePage.clicarEmLike(titulo);
            Thread.sleep(1000);

            menuHomePage.clicarEmDislike(titulo);
            Thread.sleep(1000);
            assertEquals(numLikesInicial - 1, menuHomePage.pegarNumeroDeLikes(titulo));

            menuHomePage.clicarEmLike(titulo);
            Thread.sleep(1000);
            assertEquals(numLikesInicial + 1, menuHomePage.pegarNumeroDeLikes(titulo));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Reprovou
    @Test
    void CT41() {

        // Objetivo: Verificar se o sistema permite a adição de um comentário ao vídeo.

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

            if (!retorno.isEmpty()) {
                assertEquals(texto, retorno);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT42() {

        // Objetivo: Verificar se o botão de compartilhar cola o Link do vídeo na área
        // de transferência.

        try {
            Thread.sleep(4000);
            irAteAHome();
            menuHomePage.clicarEmCompartilhar("Selenium (Testes Automatizados)");
            String areaDeTransferencia = menuHomePage.pegarTextoDaAreaDeTransferencia();

            assertEquals("https://www.youtube.com/watch?v=Fw9YW5_MZRs", areaDeTransferencia);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT43() {

        // Objetivo: Verificar se o sistema permite o acesso a cursos que não exigem
        // PIN.

        try {
            Thread.sleep(4000);
            irAteAHome();

            menuHomePage.acessarCursoSemPIN();
            Thread.sleep(4000);
            assertTrue(menuHomePage.verificarSeEntrouNoCurso());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT44_1() {
        // Objetivo: Verificar se o sistema bloqueia o acesso ao curso quando o PIN incorreto é inserido.

        try {
            Thread.sleep(4000);
            irAteAHome();
            
            menuHomePage.tentarAcessarCursoComPIN("Grupo1", "54321");
            assertFalse(menuHomePage.verificarSeEntrouNoCurso());
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Passou
    @Test
    void CT44_2() {
        // Objetivo: Verificar se o sistema permite o acesso ao curso quando o PIN correto é inserido.

        try {
            Thread.sleep(4000);
            irAteAHome();

            menuHomePage.tentarAcessarCursoComPIN("Grupo1", "12345");
            assertTrue(menuHomePage.verificarSeEntrouNoCurso());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private void irAteAHome() {
        wait.until(ExpectedConditions.urlContains("/"));
    }
    @Test
    void CT38_Home_VisualizarListagemDeVideos() {
        try {
            Thread.sleep(4000);
            irAteAHome();
            String video1 = "Selenium (Testes Automatizados)";
            
            assertTrue(menuHomePage.videoEstaListado(video1), 
                "O vídeo '" + video1 + "' deveria estar listado na Home.");
        } catch (Exception e) {
            System.err.println("Falha no CT38_Home: " + e.getMessage());
            fail("Erro ao listar vídeos na Home");
        }
    }
    @Test
    void CT38_1_Home_ValidarIntegridadeDoCardDeVideo() {
        try {
            Thread.sleep(4000);
            irAteAHome();

            String tituloVideo = "Selenium (Testes Automatizados)";
            assertTrue(menuHomePage.videoEstaListado(tituloVideo), 
                "Falha Crítica: O vídeo não foi encontrado na listagem da Home.");
            assertTrue(menuHomePage.botoesDeInteracaoEstaoVisiveis(tituloVideo), 
                "O card do vídeo está incompleto: botões de Like/Dislike não foram listados.");

        } catch (Exception e) {
            System.err.println("Falha no CT38_1: " + e.getMessage());
            fail("Erro ao validar integridade da listagem de vídeos");
        }
    }
    @Test
    void CT39_Home_AssistirVideo() {
        try {
            Thread.sleep(4000);
            irAteAHome();

            String tituloVideo = "Selenium (Testes Automatizados)";
            assertTrue(menuHomePage.videoEstaListado(tituloVideo), 
                "O vídeo precisa estar listado para ser assistido.");
            assertTrue(menuHomePage.playerDeVideoEstaVisivel(tituloVideo), 
                "O player de vídeo não foi carregado corretamente na Home.");
        } catch (Exception e) {
            System.err.println("Falha no CT39_Home: " + e.getMessage());
            fail("Erro ao verificar player de vídeo na Home");
        }
    } 
    @Test
    void CT39_1_Home_ValidarFonteDoPlayerYoutube() {
        try {
            Thread.sleep(4000);
            irAteAHome();

            String tituloVideo = "Selenium (Testes Automatizados)";
            assertTrue(menuHomePage.playerDeVideoEstaVisivel(tituloVideo), 
                "O player de vídeo não está visível.");
            String srcVideo = menuHomePage.pegarFonteDoVideo(tituloVideo);
            
            System.out.println("Fonte do vídeo encontrada: " + srcVideo);
            assertNotNull(srcVideo, "O atributo 'src' do vídeo está nulo.");
            assertTrue(srcVideo.contains("youtube.com") || srcVideo.contains("youtu.be"), 
                "O player não carregou um vídeo válido do YouTube. Fonte atual: " + srcVideo);

        } catch (Exception e) {
            System.err.println("Falha no CT39_1: " + e.getMessage());
            fail("Erro ao validar a fonte do player de vídeo");
        }
    }

}

