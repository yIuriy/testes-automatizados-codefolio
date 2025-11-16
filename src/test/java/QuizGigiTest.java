
import io.github.bonigarcia.wdm.WebDriverManager;
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
import pages.ManageCoursePage;
import utils.Authentication;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class QuizGigiTest {
    WebDriver driver;
    Authentication authentication;
    ManageCoursePage manageCoursePage;
    WebDriverWait wait;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        authentication = new Authentication(driver);
        manageCoursePage = new ManageCoursePage(driver);
        dashboardPage = new DashboardPage(driver);

        authentication.realizarLoginViaIndexedBD();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    @Test //Passou
    void CT26() {
    try {
        WebElement btnCursos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Cursos']")));
        js.executeScript("arguments[0].click();", btnCursos);
        System.out.println("Clicou no botão 'Cursos'.");
        wait.until(ExpectedConditions.urlContains("/listcurso"));

        WebElement abaConcluidos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluídos') or " +
                        "contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluidos')]")
        ));
        js.executeScript("arguments[0].click();", abaConcluidos);
        System.out.println("Abriu a aba 'Concluídos'.");
        Thread.sleep(3000);

        WebElement cardOneFrameMan = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='OneFrameMan']/ancestor::div[contains(@class,'MuiCard-root')]")
        ));
        WebElement btnAcessar = cardOneFrameMan.findElement(By.xpath(".//button"));
        js.executeScript("arguments[0].scrollIntoView(true);", btnAcessar);
        wait.until(ExpectedConditions.elementToBeClickable(btnAcessar));
        js.executeScript("arguments[0].click();", btnAcessar);
        System.out.println("Clicou para acessar o curso OneFrameMan.");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title,'Abrir Quiz Gigi')]")
        )).click();
        System.out.println("Clicou no botão 'Abrir Quiz Gigi'.");
        Thread.sleep(5000);

        List<WebElement> elementosQuiz = driver.findElements(
                By.xpath("//*[contains(text(),'Sortear outro aluno') or contains(text(),'Quiz')]")
        );
        System.out.println("Elementos do quiz encontrados: " + elementosQuiz.size());

WebElement btnSortearOutro = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//button[contains(@title,'Sortear outro aluno') or contains(.,'Sortear outro aluno')]")
));
js.executeScript("arguments[0].scrollIntoView({block:'center'});", btnSortearOutro);
js.executeScript("arguments[0].click();", btnSortearOutro);
System.out.println("Clicou em 'Sortear outro aluno'.");

Thread.sleep(2000);

WebElement btnSortearOutroReload = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//button[contains(@title,'Sortear outro aluno') or contains(.,'Sortear outro aluno')]")
));

assertTrue(btnSortearOutroReload.isDisplayed(),
        "O botão não reapareceu após sortear outro aluno.");
System.out.println("CT26 funcionou corretamente");

    } catch (Exception e) {
        System.out.println("Erro no CT26: " + e.getMessage());
        e.printStackTrace();
        fail("Falha no teste CT26.");
    } finally {
        if (driver != null) {
            driver.quit();
        }
    }
}

@Test //Passou
void CT26_1() {
    try {
        WebElement btnCursos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Cursos']")));
        js.executeScript("arguments[0].click();", btnCursos);
        System.out.println("Clicou no botão 'Cursos'.");
        wait.until(ExpectedConditions.urlContains("/listcurso"));

        WebElement abaConcluidos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluídos') or " +
                        "contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluidos')]")
        ));
        js.executeScript("arguments[0].click();", abaConcluidos);
        System.out.println("Abriu a aba 'Concluídos'.");
        Thread.sleep(3000);

        WebElement cardOneFrameMan = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='OneFrameMan']/ancestor::div[contains(@class,'MuiCard-root')]")
        ));
        WebElement btnAcessar = cardOneFrameMan.findElement(By.xpath(".//button"));
        js.executeScript("arguments[0].scrollIntoView(true);", btnAcessar);
        wait.until(ExpectedConditions.elementToBeClickable(btnAcessar));
        js.executeScript("arguments[0].click();", btnAcessar);
        System.out.println("Clicou para acessar o curso OneFrameMan.");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title,'Abrir Quiz Gigi')]")
        )).click();
        System.out.println("Clicou no botão 'Abrir Quiz Gigi'.");
        Thread.sleep(5000);

        List<WebElement> elementosQuiz = driver.findElements(
                By.xpath("//*[contains(text(),'Sortear outro aluno') or contains(text(),'Quiz')]")
        );
        System.out.println("Elementos do quiz encontrados: " + elementosQuiz.size());
        for (int i = 1; i <= 4; i++) {

            WebElement btnSortearOutro = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[contains(@title,'Sortear outro aluno') or contains(.,'Sortear outro aluno')]")
            ));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", btnSortearOutro);
            js.executeScript("arguments[0].click();", btnSortearOutro);
            Thread.sleep(2000);

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[contains(@title,'Sortear outro aluno') or contains(.,'Sortear outro aluno')]")
            ));
        }
        System.out.println("Sorteio multiplo realizado.");
        System.out.println("CT26.1 executado com sucesso");

    } catch (Exception e) {
        System.out.println("Erro no CT26: " + e.getMessage());
        e.printStackTrace();
        fail("Falha no teste CT26.");
    } finally {
        if (driver != null) {
            driver.quit();
        }
    }
}

@Test //Passou
    void CT27() {
    try {
        WebElement btnCursos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Cursos']")));
        js.executeScript("arguments[0].click();", btnCursos);
        System.out.println("Clicou no botão 'Cursos'.");
        wait.until(ExpectedConditions.urlContains("/listcurso"));

        WebElement abaConcluidos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluídos') or " +
                        "contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluidos')]")
        ));
        js.executeScript("arguments[0].click();", abaConcluidos);
        System.out.println("Abriu a aba 'Concluídos'.");

        Thread.sleep(3000);

        WebElement cardOneFrameMan = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='OneFrameMan']/ancestor::div[contains(@class,'MuiCard-root')]")
        ));
        WebElement btnAcessar = cardOneFrameMan.findElement(By.xpath(".//button"));
        js.executeScript("arguments[0].scrollIntoView(true);", btnAcessar);
        wait.until(ExpectedConditions.elementToBeClickable(btnAcessar));
        js.executeScript("arguments[0].click();", btnAcessar);
        System.out.println("Clicou para acessar o curso OneFrameMan.");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title,'Abrir Quiz Gigi')]")
        )).click();
        System.out.println("Clicou no botão 'Abrir Quiz Gigi'.");
        Thread.sleep(5000);

        List<WebElement> elementosQuiz = driver.findElements(
                By.xpath("//*[contains(text(),'Escolher outro aluno') or contains(text(),'Quiz')]")
        );
        System.out.println("Elementos do quiz encontrados: " + elementosQuiz.size());

WebElement btnEscolherOutro = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//button[contains(@title,'Escolher outro aluno') or contains(.,'Escolher outro aluno')]")
));

js.executeScript("arguments[0].scrollIntoView({block:'center'});", btnEscolherOutro);
js.executeScript("arguments[0].click();", btnEscolherOutro);
System.out.println("Clicou em 'Escolher outro aluno'.");
Thread.sleep(2000);

WebElement btnSortearOutroReload = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//button[contains(@title,'Escolher outro aluno') or contains(.,'Escolher outro aluno')]")
));

assertTrue(btnSortearOutroReload.isDisplayed(),
        "O botão não reapareceu após escolher outro aluno.");

        WebElement alunoZildo = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(translate(normalize-space(.), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ZILDO')]/ancestor::button[1]")
        ));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", alunoZildo);
        js.executeScript("arguments[0].click();", alunoZildo);
        Thread.sleep(2000);
        System.out.println("CT27 executado com sucesso");

    } catch (Exception e) {
        System.out.println("Erro no CT27: " + e.getMessage());
        e.printStackTrace();
        fail("Falha no teste CT27.");
    } finally {
        if (driver != null) {
            driver.quit();
        }
    }
}

@Test //Passou
void CT27_1() {
    try {
        WebElement btnCursos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Cursos']")));
        js.executeScript("arguments[0].click();", btnCursos);
        System.out.println("Clicou no botão 'Cursos'.");
        wait.until(ExpectedConditions.urlContains("/listcurso"));

        WebElement abaConcluidos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluídos') or " +
                        "contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluidos')]")
        ));
        js.executeScript("arguments[0].click();", abaConcluidos);
        System.out.println("Abriu a aba 'Concluídos'.");
        Thread.sleep(3000);

        WebElement cardOneFrameMan = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='OneFrameMan']/ancestor::div[contains(@class,'MuiCard-root')]")
        ));
        WebElement btnAcessar = cardOneFrameMan.findElement(By.xpath(".//button"));
        js.executeScript("arguments[0].scrollIntoView(true);", btnAcessar);
        wait.until(ExpectedConditions.elementToBeClickable(btnAcessar));
        js.executeScript("arguments[0].click();", btnAcessar);
        System.out.println("Clicou para acessar o curso OneFrameMan.");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title,'Abrir Quiz Gigi')]")
        )).click();

        System.out.println("Clicou no botão 'Abrir Quiz Gigi'.");
        Thread.sleep(3000);

        WebElement btnEscolherOutro = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title,'Escolher outro aluno') or contains(.,'Escolher outro aluno')]")
        ));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", btnEscolherOutro);
        js.executeScript("arguments[0].click();", btnEscolherOutro);
        System.out.println("Clicou em 'Escolher outro aluno'.");
        Thread.sleep(2000);

        WebElement campoBusca = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[contains(@placeholder,'Buscar aluno')]")
        ));
        campoBusca.sendKeys("Zil");
        System.out.println("Digitou no campo de busca.");
        Thread.sleep(1500);
        System.out.println("CT27.1 executado com sucesso!");

    } catch (Exception e) {
        System.out.println("Erro no CT27.1: " + e.getMessage());
        e.printStackTrace();
        fail("Falha no teste CT27.1");
    } finally {
        if (driver != null) {
            driver.quit();
        }
    }
}

@Test //Passou
void CT28() {
    try {
        WebElement btnCursos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Cursos']")));
        js.executeScript("arguments[0].click();", btnCursos);
        System.out.println("Clicou no botão 'Cursos'.");
        wait.until(ExpectedConditions.urlContains("/listcurso"));

        WebElement abaConcluidos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluídos') or " +
                        "contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluidos')]")
        ));
        js.executeScript("arguments[0].click();", abaConcluidos);
        System.out.println("Abriu a aba 'Concluídos'.");

        Thread.sleep(3000);

        WebElement cardOneFrameMan = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='OneFrameMan']/ancestor::div[contains(@class,'MuiCard-root')]")
        ));

        WebElement btnAcessar = cardOneFrameMan.findElement(By.xpath(".//button"));
        js.executeScript("arguments[0].scrollIntoView(true);", btnAcessar);
        wait.until(ExpectedConditions.elementToBeClickable(btnAcessar));
        js.executeScript("arguments[0].click();", btnAcessar);
        System.out.println("Clicou para acessar o curso OneFrameMan.");

        WebElement btnQuizGigi = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title,'Abrir Quiz Gigi')]")
        ));
        js.executeScript("arguments[0].click();", btnQuizGigi);
        System.out.println("Clicou no botão 'Abrir Quiz Gigi'.");

        Thread.sleep(3000);
        By alternativaCorreta = By.xpath("//button[.//span[contains(text(),'A')]]");

        WebElement btnAlternativa = wait.until(ExpectedConditions.presenceOfElementLocated(alternativaCorreta));

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", btnAlternativa);
        js.executeScript("arguments[0].click();", btnAlternativa);

        System.out.println("Alternativa correta selecionada!");
        Thread.sleep(2000);

    } catch (Exception e) {
        System.out.println("Erro no CT26: " + e.getMessage());
        e.printStackTrace();
        fail("Falha no teste CT26.");
    } finally {
        if (driver != null) {
            driver.quit();
        }
    }
}

@Test
void CT30() {
    try {
        WebElement btnCursos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Cursos']")));
        js.executeScript("arguments[0].click();", btnCursos);
        System.out.println("Clicou no botão 'Cursos'.");
        wait.until(ExpectedConditions.urlContains("/listcurso"));

        WebElement abaConcluidos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluídos') or " +
                        "contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'concluidos')]")
        ));
        js.executeScript("arguments[0].click();", abaConcluidos);
        System.out.println("Abriu a aba 'Concluídos'.");
        Thread.sleep(3000);

        WebElement cardOneFrameMan = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='OneFrameMan']/ancestor::div[contains(@class,'MuiCard-root')]")
        ));
        WebElement btnAcessar = cardOneFrameMan.findElement(By.xpath(".//button"));
        js.executeScript("arguments[0].scrollIntoView(true);", btnAcessar);
        wait.until(ExpectedConditions.elementToBeClickable(btnAcessar));
        js.executeScript("arguments[0].click();", btnAcessar);
        System.out.println("Clicou para acessar o curso OneFrameMan.");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title,'Abrir Quiz Gigi')]")
        )).click();

        System.out.println("Clicou no botão 'Abrir Quiz Gigi'.");
        Thread.sleep(5000);
        List<WebElement> elementosQuiz = driver.findElements(
                By.xpath("//*[contains(text(),'Mostrar respostas') or contains(text(),'Quiz')]")
        );
        System.out.println("Elementos do quiz encontrados: " + elementosQuiz.size());

        WebElement btnMostrar = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(@title,'Mostrar respostas') or contains(.,'Mostrar respostas')]")
        ));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", btnMostrar);
        js.executeScript("arguments[0].click();", btnMostrar);
        System.out.println("Clicou em 'Mostrar respostas'.");
        Thread.sleep(2000);

        WebElement btnMostrarReload = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(@title,'Mostrar respostas') or contains(.,'Mostrar respostas')]")
        ));
        assertTrue(btnMostrarReload.isDisplayed(),
                "O botão não reapareceu após Mostrar respostas.");
        System.out.println("Mostrar respostas funcionou corretamente!");

         WebElement btnOcultar = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[contains(@title,'Ocultar respostas') or contains(.,'Ocultar respostas')]")
            ));
            btnOcultar.click();
            System.out.println("Clicou em 'Ocultar respostas'.");
            Thread.sleep(5000);
            
            // Mesmo sendo ocultar, ele aparece como mostrar 
            WebElement btnOcultarReload = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[contains(@title,'Mostrar respostas') or contains(.,'Mostrar respostas')]")
            ));

            assertTrue(btnOcultarReload.isDisplayed(),
                    "O botão 'Ocultar respostas' não apareceu novamente.");
            System.out.println("Ocultar respostas funcionou corretamente!");

        } catch (Exception e) {
            System.out.println("Erro no CT28.1: " + e.getMessage());
            e.printStackTrace();
            fail("Falha no teste CT28.1");
        } finally {
            if (driver != null) {
              driver.quit();
            }
        }
    }
}