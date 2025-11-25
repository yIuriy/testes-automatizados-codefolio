package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public QuizPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    public void clicarBotaoQuizVideo() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/div[1]/div/div/button[1]")));
        botao.click();
    }

    public void clicarSelecaoVideoAssociado() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/div[2]/div[1]/div/div/div")));
        botao.click();
    }

    public void clicarPrimeiroVideoAssociado() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/ul/li")));
        botao.click();
    }

    public void definirNotaMinimaQuiz(String notaMinima) {
        WebElement inputNota = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/div[2]/div[2]/div/div/input")));
        inputNota.click();
        inputNota.sendKeys(Keys.chord(Keys.CONTROL, "a"), notaMinima);
    }

    public void clicarBotaoAdicionarQuiz() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/div[2]/div[3]/button")));
        botao.click();
    }

    public void verificarSeQuizFoiAdicionado() {
        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div[3]/h6[contains(text(), 'Quiz do vídeo adicionado com sucesso!')]")));
            System.out.println(div.getText());
            assertEquals("Quiz do vídeo adicionado com sucesso!", div.getText());
        });
    }

    public void clicarBotaoOk() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/button")));
        botao.click();
    }
}
