package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;


    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void abrir() {
        driver.get("https://react-na-pratica.web.app/dashboard");
    }

    public void abrirMenuDeOpcoesPerfil() {
        driver.findElement(By.xpath("//*[@id=\":r5:\"]/div/img")).click();
    }

    public void abrirMenuGerenciamentoDeCursos() {
        driver.findElement(By.xpath("/html/body/div[2]/div[3]/ul/li[2]")).click();
    }

    /**
     * Localiza um curso na seção "Cursos Recomendados" pelo nome e clica em "Acessar".
     *
     * @param nomeDoCurso O nome exato do curso a ser acessado.
     */
    public void acessarCursoRecomendadoPorNome(String nomeDoCurso) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[contains(text(), 'Cursos Recomendados')]")
        ));
        WebElement botaoAcessar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[contains(text(), '" + nomeDoCurso + "')]" +
                        "/ancestor::div[contains(@class, 'MuiCard-root')]" +
                        "//button[contains(text(), 'Acessar')]")
        ));

        botaoAcessar.click();
    }

    public void irAteAPaginaDeGerenciarCursos() {
        abrirMenuDeOpcoesPerfil();
        abrirMenuGerenciamentoDeCursos();
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }

    // --- MÉTODO CORRIGIDO PARA O CT-34 ---

    /**
     * Espera o modal do PIN aparecer, digita o PIN e clica em Enviar.
     *
     * @param pin O PIN do curso.
     */
    public void inserirPinParaCurso(String pin) {
        // CORREÇÃO: Usando o placeholder "PIN de Acesso"
        WebElement inputPin = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='PIN de Acesso']/following-sibling::div/input")
        ));

        inputPin.sendKeys(pin);

        WebElement botaoEnviar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'css-1mu702d')]//button[contains(text(), 'Enviar')]")
        ));

        botaoEnviar.click();
    }
}