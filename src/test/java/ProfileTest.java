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
    void CT01_edicaoDePerfil_ComSeletoresCorretos() throws InterruptedException {
        // Pré-condições: Driver e login (do @BeforeEach)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PerfilPage perfilPage = new PerfilPage(driver);

        // 1. Acessar o menu "Perfil" (Passo 1 do PDF)
        perfilPage.abrir();

        // Espera a página de visualização carregar
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'lucasweber.aluno@unipampa.edu.br')]")));

        // 2. Clicar em "Editar Perfil" (o ícone de lápis) (Passo 2 do PDF)
        WebElement btnEditar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(@class, 'MuiIconButton-root') and .//*[name()='svg']])[last()]")));
        btnEditar.click(); // <--- Clica no botão de edição

        // 3. Alterar os campos (Passo 3 do PDF)

        // --- CORREÇÃO: Usando "Selecionar Tudo" (Ctrl+A) e "Delete" ---
        WebElement inputNome = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Nome']/following-sibling::div//input")));
        inputNome.sendKeys(Keys.CONTROL + "a");
        inputNome.sendKeys(Keys.DELETE);
        inputNome.sendKeys("Lucas"); // Dado do PDF adaptado

        // --- CORREÇÃO: Usando "Selecionar Tudo" (Ctrl+A) e "Delete" ---
        WebElement inputSobrenome = driver.findElement(
                By.xpath("//label[text()='Sobrenome']/following-sibling::div//input"));
        inputSobrenome.sendKeys(Keys.CONTROL + "a");
        inputSobrenome.sendKeys(Keys.DELETE);
        inputSobrenome.sendKeys("Weber"); // Dado do PDF adaptado

        // --- CORREÇÃO: Usando "Selecionar Tudo" (Ctrl+A) e "Delete" ---
        WebElement inputLinkedIn = driver.findElement(
                By.xpath("//label[text()='LinkedIn']/following-sibling::div//input"));
        inputLinkedIn.sendKeys(Keys.CONTROL + "a");
        inputLinkedIn.sendKeys(Keys.DELETE);
        inputLinkedIn.sendKeys("https://linkedin.com/in/xxxxxxxx"); // Dado exato do PDF

        // 4. Clicar em "Salvar Alterações" (Passo 4 do PDF)
        WebElement btnSalvar = driver.findElement(By.xpath("//button[normalize-space()='Salvar Alterações']"));
        btnSalvar.click();

        // 5. REMOVIDO: Verificação da mensagem de sucesso (conforme solicitado)
        // (Vamos adicionar uma pequena pausa para o salvamento acontecer
        // antes de recarregar)
        Thread.sleep(1000); // Pausa de 1 segundo

        // 6. Atualizar a página e verificar (Passo 5 do PDF)
        driver.navigate().refresh();

        // Clica em editar novamente para os campos ficarem visíveis
        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//button[contains(@class, 'MuiIconButton-root') and .//*[name()='svg']])[last()]")))
                .click();

        // Verifica se os valores do PDF foram salvos
        WebElement nomeAtualizado = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Nome']/following-sibling::div//input")));
        assertEquals("Lucas", nomeAtualizado.getAttribute("value"), "O Nome não foi atualizado.");

        WebElement sobrenomeAtualizado = driver.findElement(
                By.xpath("//label[text()='Sobrenome']/following-sibling::div//input"));
        assertEquals("Weber", sobrenomeAtualizado.getAttribute("value"), "O Sobrenome não foi atualizado.");

        WebElement linkAtualizado = driver.findElement(
                By.xpath("//label[text()='LinkedIn']/following-sibling::div//input"));
        assertEquals("https://linkedin.com/in/xxxxxxxx", linkAtualizado.getAttribute("value"),
                "O LinkedIn não foi atualizado.");
    }
}