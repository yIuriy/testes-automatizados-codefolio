import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ListCursoPage;
import utils.Authentication;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RankingTest {

    WebDriver driver;
    Authentication authentication;
    ListCursoPage listCursoPage;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        authentication = new Authentication(driver);
        listCursoPage = new ListCursoPage(driver);
        
        authentication.realizarLoginViaIndexedBD();
    }

    @AfterEach
    void teardown() {
        if (driver != null) driver.quit();
    }

    @Test
    void CT35_AcessarRankingCursoSemPin() {
        try {
            listCursoPage.abrirPaginaCursos();
            Thread.sleep(2000); 
            listCursoPage.clicarAbaConcluidos();
            Thread.sleep(2000);
            listCursoPage.abrirCurso("Teste quiz grupo1");
            listCursoPage.clicarBotaoFechar();
            Thread.sleep(1000);
            listCursoPage.clicarAbrirQuizGigi();
            Thread.sleep(2000);
            listCursoPage.clicarBotaoRanking();
            Thread.sleep(1000);
            assertTrue(listCursoPage.rankingEstaVisivel(), "O Ranking não foi exibido na tela.");
            System.out.println("CT-35: Sucesso - Ranking visualizado.");

        } catch (Exception e) {
            System.err.println("Falha no CT-35: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}