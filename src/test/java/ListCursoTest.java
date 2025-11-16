import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import pages.DashboardPage;
import pages.ListCursoPage;
import pages.ManageCoursePage;
import utils.Authentication;

public class ListCursoTest {

    WebDriver driver;
    Authentication authentication;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        ListCursoPage cursoPage = new ListCursoPage(driver);
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
        ListCursoPage course = new ListCursoPage(driver);

        course.abrirPaginaCursos()
            .abaEmAndamento()
            .abrirCurso("Teste em Andamento")
            .clicarVerVideoPorTitulo("video1");

        assertTrue(course.videoEstaNaTela(),
                "O vídeo não apareceu na tela do curso.");
    }


    // RF49 – Assistir Vídeo
    @Test
    @DisplayName("Verifica se vídeo do Youtube carregou em curso sem senha na aba de disponíveis")
    void CT49() throws InterruptedException {
        ListCursoPage course = new ListCursoPage(driver);

        course.abrirPaginaCursos()
            .abaEmAndamento()
            .abrirCurso("Teste em Andamento")
            .clicarVerVideoPorTitulo("video1");

        assertTrue(course.videoCarregou(), "O vídeo não carregou.");
    Thread.sleep(3000); // simula assistir
}

    // RF50 – Navegar entre Vídeos
    @Test
    @DisplayName("Clicar para ir para o próximo vídeo  e confirmar que carregou")
    void CT50 () throws InterruptedException {
        ListCursoPage course = new ListCursoPage(driver);

        course.abrirPaginaCursos()
            .abaEmAndamento()
            .abrirCurso("Teste em Andamento")
            .clicarVerVideoPorTitulo("video1")
            .videoCarregou();

        assertTrue(course.videoCarregou(), "O vídeo não carregou.");
        
        Thread.sleep(3000);

        course.avancarVideo()
              .videoCarregou();

        assertTrue(course.videoCarregou(), "O vídeo não carregou.");
        
        Thread.sleep(3000);

        course.voltarVideo()
              .videoCarregou();

        assertTrue(course.videoCarregou(), "O vídeo não carregou.");
    }
}