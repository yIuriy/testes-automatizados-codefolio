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

public class QuizCourseTest {
    WebDriver driver;
    Authentication authentication;
    ManageCoursePage manageCoursePage;
    QuizPage quizPage;
    WebDriverWait wait;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        authentication = new Authentication(driver);
        manageCoursePage = new ManageCoursePage(driver);
        quizPage = new QuizPage(driver);
        dashboardPage = new DashboardPage(driver);
        authentication.realizarLoginViaIndexedBD();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    @Test
    void CT014(){
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Quiz");
            quizPage.clicarBotaoQuizVideo();
            quizPage.clicarSelecaoVideoAssociado();
            quizPage.clicarPrimeiroVideoAssociado();
            quizPage.definirNotaMinimaQuiz("100");
            quizPage.clicarBotaoAdicionarQuiz();
            quizPage.verificarSeQuizFoiAdicionado();
            quizPage.clicarBotaoOk();
            System.out.println("Quiz adicionado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void CT015() {
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Quiz");
            quizPage.clicarBotaoEditarQuiz();
            quizPage.inserirTituloPergunta("Qual o Pokémon mais fofo:");
            quizPage.inserirOpcao("Pikachu", 1);
            quizPage.inserirOpcao("Furret", 2);
            quizPage.declararOpcaoCorreta(2);
            quizPage.clicarBotaoSalvarQuestao();
            quizPage.verificarSeQuestaoFoiSalva();
            quizPage.clicarBotaoCancelar();
            quizPage.clicarBotaoSetaBaixo();
            quizPage.clicarBotaoExcluirQuestao();
            quizPage.clicarBotaoConfirmarExcluirQuestao();
            quizPage.verificarSeQuestaoFoiExcluida();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void CT016(){
        try {
            Thread.sleep(5000);
            dashboardPage.irAteAPaginaDeGerenciarCursos();
            manageCoursePage.clicarBotaoGerenciarCursoPorNomeDoCurso("Grupo 01");
            manageCoursePage.localizarEClicarNoMenuPorNome("Quiz");
            quizPage.clicarBotaoExcluirQuiz();
            quizPage.clicarBotaoConfirmaExcluirQuiz();
            quizPage.verificarSeQuizFoiExcluido();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
