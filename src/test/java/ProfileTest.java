import pages.PerfilPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.DashboardPage;
import utils.Authentication;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;

public class ProfileTest {
    WebDriver driver;
    Authentication authentication;
    DashboardPage dashboardPage;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        authentication = new Authentication(driver);
        dashboardPage = new DashboardPage(driver);
        authentication.realizarLoginViaIndexedBD();
    }

    @Test
    void testLogin() throws InterruptedException {
        Thread.sleep(5000);

    }


    @Test
    void CT01_edicaoDePerfil() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PerfilPage perfilPage = new PerfilPage(driver);

        // 1. Acessar o menu "Perfil"
        perfilPage.abrir();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'lucasweber.aluno@unipampa.edu.br')]")));

        // 2. Clicar em "Editar Perfil"
        WebElement btnEditar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(@class, 'MuiIconButton-root') and .//*[name()='svg']])[last()]")));
        js.executeScript("arguments[0].click();", btnEditar);

        // 3. Preencher campos
        WebElement inputNome = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Nome']/following-sibling::div//input")));
        js.executeScript("arguments[0].value = arguments[1];", inputNome, "Lucas");

        WebElement inputSobrenome = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Sobrenome']/following-sibling::div//input")));
        js.executeScript("arguments[0].value = arguments[1];", inputSobrenome, "Weber");

        WebElement inputLinkedIn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='LinkedIn']/following-sibling::div//input")));
        js.executeScript("arguments[0].value = arguments[1];", inputLinkedIn, "https://linkedin.com/in/xxxxxxxx");

        // 4. Clicar em "Salvar Alterações"
        WebElement btnSalvar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Salvar Alterações']")));
        js.executeScript("arguments[0].click();", btnSalvar);

        // 5. Esperar o retorno à tela de visualização
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'lucasweber.aluno@unipampa.edu.br')]")));


        Thread.sleep(3000); // Pausa AUMENTADA para 3s para estabilizar a UI

        // 6. Clicar novamente em "Editar Perfil"
        WebElement btnEditarNovamente = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(@class, 'MuiIconButton-root') and .//*[name()='svg']])[last()]")));
        js.executeScript("arguments[0].click();", btnEditarNovamente);

        // 7. Verificar se os valores foram realmente salvos
        WebElement nomeAtualizado = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Nome']/following-sibling::div//input")));
        WebElement sobrenomeAtualizado = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Sobrenome']/following-sibling::div//input")));
        WebElement linkAtualizado = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='LinkedIn']/following-sibling::div//input")));

      
        // Se o teste passar da linha 98, estas mensagens aparecerão no console:
        System.out.println("Nome salvo: " + nomeAtualizado.getAttribute("value"));
        System.out.println("Sobrenome salvo: " + sobrenomeAtualizado.getAttribute("value"));
        System.out.println("LinkedIn salvo: " + linkAtualizado.getAttribute("value"));

        // 8. Asserts
        assertEquals("Lucas", nomeAtualizado.getAttribute("value"), "O Nome não foi atualizado.");
        assertEquals("Weber", sobrenomeAtualizado.getAttribute("value"), "O Sobrenome não foi atualizado.");
        assertEquals("https://linkedin.com/in/xxxxxxxx", linkAtualizado.getAttribute("value"),
                "O LinkedIn não foi atualizado.");
    }
}