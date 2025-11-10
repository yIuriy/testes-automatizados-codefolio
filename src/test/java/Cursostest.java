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

public class Cursostest { 

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
        authentication.realizarLoginViaIndexedBD(); 
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Teste para o Caso de Teste CT-33: Acesso a (um) Curso Recomendado.
     */
    @Test 
    void CT33_acessarCursoRecomendadoSemPin() throws InterruptedException {
        String nomeDoCurso = "OneFrameMan"; 
        dashboardPage.acessarCursoRecomendadoPorNome(nomeDoCurso);
        
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/dashboard")
        ));
        
        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/classes"), "A URL não mudou para a página /classes.");
        Thread.sleep(1000); 
    }

    /**
     * Teste para o Caso de Teste CT-34: Acesso a Cursos com PIN.
     * Tenta acessar o "Grupo-01" com o PIN "grupo1".
     */
    @Test
    void CT34_acessarCursoComPin() throws InterruptedException {
        
        String nomeDoCursoComPin = "Grupo-01";
        

        String pinCorreto = "grupo1";

        // Passo 1 e 2: Clica em "Acessar" no curso protegido
        dashboardPage.acessarCursoRecomendadoPorNome(nomeDoCursoComPin);
        
        // Passo 3 e 4: Insere o PIN e confirma
        dashboardPage.inserirPinParaCurso(pinCorreto);

        // Resultado Esperado: O sistema deve liberar o acesso
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/dashboard")
        ));

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/classes"), "A URL não mudou para /classes após inserir o PIN.");
        Thread.sleep(1000); // Pausa
    }
}