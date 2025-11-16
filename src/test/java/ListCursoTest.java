import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import pages.ListCursoPage;
import utils.Authentication;

public class ListCursoTest {

    WebDriver driver;
    Authentication authentication;
    ListCursoPage listCursoPage;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        listCursoPage = new ListCursoPage(driver);
        authentication.realizarLoginViaIndexedBD();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void teardown() {
        if (driver != null) driver.quit();
    }

    // RF48 – Selecionar Vídeo
    @Test
    @DisplayName("Verifica acesso à curso sem senha na aba de disponíveis")
    void CT48() {
        try {
            Thread.sleep(5000);
            listCursoPage.abrirPaginaCursos()
                    .abaEmAndamento()
                    .abrirCurso("Teste em Andamento")
                    .clicarVerVideoPorTitulo("video1");

            assertTrue(listCursoPage.videoEstaNaTela(),
                    "O vídeo não apareceu na tela do curso.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    // RF49 – Assistir Vídeo
    @Test
    @DisplayName("Verifica se vídeo do Youtube carregou em curso sem senha na aba de disponíveis")
    void CT49() {
        try {
            Thread.sleep(5000);
            listCursoPage.abrirPaginaCursos()
                    .abaEmAndamento()
                    .abrirCurso("Teste em Andamento")
                    .clicarVerVideoPorTitulo("video1");

            assertTrue(listCursoPage.videoCarregou(), "O vídeo não carregou.");
            Thread.sleep(3000); // simula assistir

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // RF50 – Navegar entre Vídeos
    @Test
    @DisplayName("Clicar para ir para o próximo vídeo  e confirmar que carregou")
    void CT50() throws InterruptedException {
        Thread.sleep(5000);
        listCursoPage.abrirPaginaCursos()
                .abaEmAndamento()
                .abrirCurso("Teste em Andamento")
                .clicarVerVideoPorTitulo("video1")
                .videoCarregou();
        String srcVideoInicial = listCursoPage.pegarSrcVideo();
        Thread.sleep(3000);
        listCursoPage.avancarVideo()
                     .videoCarregou();
        String srcProximoVideo = listCursoPage.pegarSrcVideo();
        assertNotEquals(srcVideoInicial, srcProximoVideo, "O vídeo não mudou após avançar/voltar.");
    }

    @Test
    @DisplayName("Clicar para ir para o próximo vídeo e depois voltar para o inicial")
    void CT50_1() throws InterruptedException {
        Thread.sleep(5000);
        listCursoPage.abrirPaginaCursos()
                .abaEmAndamento()
                .abrirCurso("Teste em Andamento")
                .clicarVerVideoPorTitulo("video1")
                .videoCarregou();
        String srcVideoInicial = listCursoPage.pegarSrcVideo();
        Thread.sleep(3000);
        listCursoPage.avancarVideo()
                     .videoCarregou();
        listCursoPage.voltarVideo()
                     .videoCarregou();
        String srcVoltando = listCursoPage.pegarSrcVideo();
        assertEquals(srcVideoInicial, srcVoltando, "Algum dos botões não funcionou");
    }

    @Test  
    @DisplayName("Acessar materiais extras de um curso que não tem materiais extras")
    void CT51() throws InterruptedException {
        Thread.sleep(5000);
        listCursoPage.abrirPaginaCursos()
                .abaEmAndamento()
                .abrirCurso("Teste em Andamento");

        Thread.sleep(3000);

        listCursoPage.materiaisExtra();

        assertTrue(listCursoPage.mensagemSemMateriaisExtraEstaNaTela(), "A mensagem de ausência de materiais extras não apareceu.");

    }

    @Test  
    @DisplayName("Acessar materiais extras de um curso que TEM materiais extras")
    void CT51_1() throws InterruptedException {
        Thread.sleep(5000);
        listCursoPage.abrirPaginaCursos()
                .abaEmAndamento()
                .abrirCurso("Tenho materias");

        Thread.sleep(3000);

        listCursoPage.materiaisExtra();

        assertFalse(listCursoPage.mensagemSemMateriaisExtraEstaNaTela(), "A mensagem de ausência de materiais extras apareceu.");

    }
}
