package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilitarios;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManageSlidePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public ManageSlidePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    public void clicarBotaoEditarSlideDoPrimeiroSlide() {
        // Localiza o primeiro ícone 'EditIcon', navega para o botão pai e clica.
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//*[@data-testid='EditIcon'])[1]/parent::button")
        ))).click();
    }

    // --- Título (input obrigatório sem placeholder)
    public void inserirTituloNoSlide(String novoTitulo) {
        String xpath = "/html/body/div/div[2]/div[1]/div[5]/div[1]/div/div[1]/div/div/input";

        WebElement inputTitulo = Objects.requireNonNull(wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))
        ));

        // 1. Ação: Força o foco e clica via JS (o mais seguro)
        js.executeScript("arguments[0].click();", inputTitulo);

        // 2. Ação: Seleciona todo o texto existente (Ctrl+A / Cmd+A) e sobrescreve (sendKeys)
        // Isso elimina a necessidade de usar inputTitulo.clear(), que está falhando.
        inputTitulo.sendKeys(Keys.chord(Keys.CONTROL, "a"), novoTitulo);
    }

    // --- Link do Slide (input obrigatório com placeholder)
    public void inserirLinkDoSlide(String link) {
        String xpath = "//input[starts-with(@placeholder, 'https://docs.google.com/presentation/')]";

        WebElement inputLink = Objects.requireNonNull(wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))
        ));

        // 1. Ação: Força o foco e clica via JS
        js.executeScript("arguments[0].click();", inputLink);

        // 2. Ação: Seleciona e sobrescreve
        inputLink.sendKeys(Keys.chord(Keys.CONTROL, "a"), link);
    }

    // --- Descrição (textarea opcional)
    public void inserirDescricaoNoSlide(String descricao) {
        WebElement inputDescricao =
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        (By.xpath("//*[contains(text(), 'Slide para teste')]"))));
        inputDescricao.click();

        // 2. Ação: Seleciona e sobrescreve
        inputDescricao.sendKeys(Keys.CONTROL + "a");
        inputDescricao.sendKeys(Keys.DELETE);
        inputDescricao.sendKeys(descricao);
    }

    public void clicarBotaoSalvarAlteracoes() {
        WebElement botao = wait.until(ExpectedConditions.
                elementToBeClickable(By.xpath("//button[contains(text(), 'Salvar Alterações')]")));
        botao.click();
    }

    public void verificarSeSlideFoiAtualizado() {

        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(), 'Slide Atualizado')]")
            ));
            System.out.println(div.getText());
            assertEquals("Slide Atualizado", div.getText());
        });

    }
}