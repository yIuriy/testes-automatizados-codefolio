package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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

    public void clicarBotaoExcluirQuiz(){
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/ul/div/div/div[2]/button[4]")));
        botao.click();
    }

    public void clicarBotaoConfirmaExcluirQuiz(){
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/div/button[1]")));
        botao.click();
    }

    public void verificarSeQuizFoiExcluido(){
        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/section[2]/div/div[contains(text(), 'Quiz deletado com sucesso!')]")));
            System.out.println(div.getText());
            assertEquals("Quiz deletado com sucesso!", div.getText());
        });
    }

    public void clicarBotaoEditarQuiz() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/ul/div/div/div[2]/button[2]")));
        botao.click();
    }

    public void clicarBotaoCancelar() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/div[4]/div/div[5]/button[2]")));
        botao.click();
    }

    public void clicarBotaoSetaBaixo() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/ul/div/div[1]/div[2]/button[1]")));
        botao.click();
    }

    public void inserirTituloPergunta(String titulo) {
        WebElement inputTitulo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/div[4]/div/div[1]/div/div/input")));
        inputTitulo.click();
        inputTitulo.sendKeys(Keys.CONTROL + "a");
        inputTitulo.sendKeys(Keys.DELETE);
        inputTitulo.sendKeys(titulo);
    }

    public void inserirOpcao(String opcao, int numero) {
        String xpath = "(//div[@id='question-form']//input[@type='text'])[" + (numero + 1) + "]";

        WebElement inputTitulo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        inputTitulo.clear();
        inputTitulo.sendKeys(opcao);
    }

    public void declararOpcaoCorreta(int opcao){
        String xpath = "/html/body/div/div[2]/div[1]/div[5]/div[4]/div/div["+(opcao+1)+"]/div/button";

        WebElement opcaoCorreta = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        opcaoCorreta.click();
    }

    public void clicarBotaoSalvarQuestao(){
        String xpath = "/html/body/div/div[2]/div[1]/div[5]/div[4]/div/div[5]/button[1]";

        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        botao.click();
    }

    public void clicarBotaoExcluirQuestao(){
        String xpath = "/html/body/div/div[2]/div[1]/div[5]/ul/div/div[2]/div/div/div/ul/li/div[2]/button[2]";
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        botao.click();
    }

    public void clicarBotaoConfirmarExcluirQuestao(){
        String xpath = "/html/body/div[2]/div[3]/div/button[1]";
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        botao.click();
    }

    public void verificarSeQuestaoFoiSalva() {
        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/section[2]/div/div[contains(text(), 'Questão adicionada com sucesso!')]")));
            System.out.println(div.getText());
            assertEquals("Questão adicionada com sucesso!", div.getText());
        });
    }

    public void verificarSeQuestaoFoiExcluida() {
        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/section[2]/div/div[contains(text(), 'Questão deletada com sucesso!')]")));
            System.out.println(div.getText());
            assertEquals("Questão deletada com sucesso!", div.getText());
        });
    }
}
