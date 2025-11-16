import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import pages.ListCursoPage;
import utils.Authentication;

public class ListCursoTest {

    WebDriver driver;
    Authentication authentication;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        authentication.realizarLoginViaIndexedBD(); // login via IndexedDB (já funcional no seu projeto)
    }

    @AfterEach
    void teardown() {
        if (driver != null) driver.quit();
    }

    // RF48 – Selecionar Vídeo (ver se o vídeo aparece ao entrar no curso)
    @Test
    @DisplayName("Verifica acesso à curso sem senha na aba de disponíveis")
    public void CT48() {
        ListCursoPage course = new ListCursoPage(driver);

        course.abrirPaginaCursos()
              .abrirCurso("Teste")   // abre o curso
              .clicarVerVideoPorTitulo("Selenium com Java");
        assertTrue(course.videoEstaNaTela(),
                "O vídeo não apareceu na tela do curso.");
    }


    // RF49 – Assistir Vídeo (clicar para assistir e confirmar que carregou)
    @Test
    @DisplayName("Verifica se vídeo do Youtube carregou em curso sem senha na aba de disponíveis")
    void CT49() throws InterruptedException {
        ListCursoPage course = new ListCursoPage(driver);

        course.abrirPaginaCursos()
              .abrirCurso("Teste")
              .clicarVerVideoPorTitulo("Selenium com Java");

        assertTrue(course.videoCarregou(), "O vídeo não carregou.");
        Thread.sleep(3000); // simula assistir
    }
}