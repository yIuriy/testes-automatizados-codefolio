import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DashboardPage;
import utils.Authentication;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;


class CursosTest {

    WebDriver driver;
    Authentication authentication;
    DashboardPage dashboardPage;
    WebDriverWait wait;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        dashboardPage = new DashboardPage(driver);
        
        // O login via IndexedDB já nos leva para a /dashboard
        authentication.realizarLoginViaIndexedBD(); 
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Teste para o Caso de Teste CT-33: Acesso a Cursos Recomendados
     * Objetivo: Verificar se o usuário pode acessar um curso sem PIN
     * a partir da lista de recomendados.
     */
    @Test
    void CT33_acessarCursoRecomendadoSemPin() throws InterruptedException {
        // O @BeforeEach já nos autenticou e estamos na Dashboard.
        
        // Com base no seu HTML, "Curso Teste Grupo 21762726476531" não tem cadeado.
        String nomeDoCurso = "Curso Teste Grupo 21762726476531";

        // 1. Chama o método da Page Object para encontrar e clicar no curso
        dashboardPage.acessarCursoRecomendadoPorNome(nomeDoCurso);

        // 2. Verifica o Resultado Esperado
        // (O usuário deve conseguir acessá-lo)
        // Verificamos se a URL mudou da dashboard para a página do curso.
        
        // Espera a URL não ser mais a da dashboard
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/dashboard")
        ));

        // Confirma que fomos para a página correta (assumindo que contenha /course)
        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/classes"), "A URL não mudou para a página /classes.");

        Thread.sleep(1000); // Pausa para ver o resultado
    }
}