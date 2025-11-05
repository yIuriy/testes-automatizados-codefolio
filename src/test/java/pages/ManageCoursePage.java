package pages;

import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilitarios;

import java.time.Duration;
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

    @AfterEach
    void close() {
        try {
            Thread.sleep(20000);
            driver.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

