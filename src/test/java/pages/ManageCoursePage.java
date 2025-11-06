package pages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilitarios;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ManageCoursePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public ManageCoursePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    public void abrir() {
        driver.get("https://testes.codefolio.com.br/manage-courses");
    }

    public void clicarBotaoGerenciarCurso() {
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Gerenciar Curso']")
        ))).click();
    }

    public void localizarEClicarNoMenuAlunos() {
        WebElement menuAlunos = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
                "//*[contains(text(),'Alunos')]"))));
        Utilitarios.centralizarElementoNaTela(menuAlunos, driver);

        assert menuAlunos != null;
        menuAlunos.click();
    }

    public WebElement getInputFiltrar() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@placeholder, 'Buscar')]")));
    }

    public void inserirTextoNoFiltrar(String texto) {
        WebElement inputFiltrar = getInputFiltrar();
        Utilitarios.centralizarElementoNaTela(inputFiltrar, driver);
        Assertions.assertNotNull(getInputFiltrar());
        inputFiltrar.sendKeys(texto);
    }

    public WebElement localizarLinhaDoAlunoPorNome(String nome) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[.//p[contains(text(),'" + nome + "')]]")
        ));
    }

    public boolean verificarSeExisteIconeDeDeletarAluno(List<WebElement> elementos) {
        for (WebElement elemento : elementos) {
            List<WebElement> buttons = elemento.findElements(By.cssSelector("button, input[type='button']"));
            if (!buttons.isEmpty()) {
                for (WebElement elemento1 : buttons) {
                    WebElement webElement = elemento1.findElement(By.cssSelector("[data-testid]"));
                    if (Objects.requireNonNull(webElement.getAttribute("data-testid")).equalsIgnoreCase("DeleteIcon"))
                        return true;
                }
            }
        }
        return false;
    }
}

