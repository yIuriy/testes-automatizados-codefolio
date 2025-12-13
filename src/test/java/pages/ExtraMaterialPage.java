package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtraMaterialPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public ExtraMaterialPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    public void inserirTituloMaterialExtra(String titulo) {
        String xpath = "/html/body/div/div[2]/div[1]/div[5]/div/div[1]/div/div/input";

        WebElement inputTitulo = Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))));

        // 1. Ação: Força o foco e clica via JS (o mais seguro)
        js.executeScript("arguments[0].click();", inputTitulo);

        // 2. Ação: Seleciona todo o texto existente (Ctrl+A / Cmd+A) e sobrescreve (sendKeys)
        // Isso elimina a necessidade de usar inputTitulo.clear(), que está falhando.
        inputTitulo.sendKeys(Keys.chord(Keys.CONTROL, "a"), titulo);
    }

    public void inserirLinkMaterialExtra(String link) {
        String xpath = "/html/body/div/div[2]/div[1]/div[5]/div/div[2]/div/div/input";

        WebElement inputTitulo = Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))));

        // 1. Ação: Força o foco e clica via JS (o mais seguro)
        js.executeScript("arguments[0].click();", inputTitulo);

        // 2. Ação: Seleciona todo o texto existente (Ctrl+A / Cmd+A) e sobrescreve (sendKeys)
        // Isso elimina a necessidade de usar inputTitulo.clear(), que está falhando.
        inputTitulo.sendKeys(Keys.chord(Keys.CONTROL, "a"), link);
    }

    public void clicarBotaoAdicionarMaterial() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/div/div[3]/button")));
        botao.click();
    }

    public void verificarSeMaterialFoiAdicionado() {
        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div[3]/h6[contains(text(), 'Material adicionado com sucesso!')]")));
            System.out.println(div.getText());
            assertEquals("Material adicionado com sucesso!", div.getText());
        });
    }

    public void clicarBotaoOkMaterial() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/button")));
        botao.click();
    }

    //Vai dar erro, pois o botão não existe
    public void clicarBotaoEditarDoPrimeiroMaterial() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/ul/li/div[2]/button[2]")));
        botao.click();
    }

    public void clicarBotaoExcluirDoPrimeiroMaterial() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[1]/div[5]/ul/li/div[2]/button")));
        botao.click();
    }

    public void clicarBotaoConfirmarExclusaoMaterial() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/div/button[1]")));
        botao.click();
    }

    public void verificarSeMaterialFoiEditado() {
        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/section[2]/div/div[contains(text(), 'Material editado com sucesso!')]"))); // Botão não existe
            System.out.println(div.getText());
            assertEquals("Material editado com sucesso!", div.getText());
        });
    }

    public void verificarSeMaterialFoiExcluido() {
        assertDoesNotThrow(() -> { // Não deve lançar timeout exception
            WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/section[2]/div/div[contains(text(), 'Material excluído com sucesso!')]")));
            System.out.println(div.getText());
            assertEquals("Material excluído com sucesso!", div.getText());
        });
    }
}
