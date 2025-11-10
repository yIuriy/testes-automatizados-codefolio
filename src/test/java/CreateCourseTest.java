import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DashboardPage;
import pages.ManageCoursePage;
import utils.Authentication;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateCourseTest {

    WebDriver driver;
    Authentication authentication;
    DashboardPage dashboardPage;
    ManageCoursePage manageCoursePage;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver; // Essencial para a correção
        authentication = new Authentication(driver);
        dashboardPage = new DashboardPage(driver);
        manageCoursePage = new ManageCoursePage(driver);
        authentication.realizarLoginViaIndexedBD();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Teste automatizado para CT-02: Cadastro de Curso
     */
    @Test
    void CT02_cadastroDeCurso() throws InterruptedException {

        // 1. Clicar em "Gerenciar Cursos"
        irAteAPaginaDeGerenciarCursos();

        // 2. Clicar em "Criar Novo Curso"
        WebElement btnAdicionar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Criar Novo Curso']")
        ));
        btnAdicionar.click();

        // 3. Preencher os campos

        WebElement inputTitulo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Título do Curso']/following-sibling::div//input")
        ));
        inputTitulo.sendKeys("Introdução ao Java");

        WebElement inputDescricao = driver.findElement(
                By.xpath("//label[text()='Descrição do Curso']/following-sibling::div//textarea[1]")
        );
        inputDescricao.sendKeys("Curso introdutório sobre fundamentos da linguagem Java");

        WebElement pinToggleLabel = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Criar PIN para acesso ao curso']")
        ));
        js.executeScript("arguments[0].click();", pinToggleLabel);

        WebElement inputPin = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='PIN de Acesso']/following-sibling::div//input")
        ));
        inputPin.sendKeys("1234");

        // 4. Clicar em "Salvar Curso"
        WebElement btnSalvarCurso = driver.findElement(
                By.xpath("//button[normalize-space()='Salvar Curso']")
        );
        btnSalvarCurso.click();

        // 5. Verificar Resultado Esperado

        // Lidar com o Modal de sucesso
        WebElement btnOkModal = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='OK!']")
        ));
        js.executeScript("arguments[0].click();", btnOkModal);

        // Sair da página de edição para voltar à listagem
        WebElement btnCancelar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Cancelar']")
        ));
        btnCancelar.click();

        // Esperar voltar para a página de gerenciamento
        wait.until(ExpectedConditions.urlContains("/manage-courses"));

        // Resultado 2: Curso aparece na listagem
        WebElement cursoNaLista = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[normalize-space()='Introdução ao Java']")
        ));

        // Mensagens no console
        System.out.println("--- DADOS LIDOS DA PÁGINA (CT-02) ---");
        System.out.println("Verificador do Curso: " + cursoNaLista.getText());
        System.out.println("Status da Verificação (visível): " + cursoNaLista.isDisplayed());
        System.out.println("-------------------------------------");
        //caso não ache
        assertTrue(cursoNaLista.isDisplayed(), "O curso 'Introdução ao Java' não foi encontrado na lista.");
    }

    // Método de ir até o gerenciamento de curso
    private void irAteAPaginaDeGerenciarCursos() {
        // 1. Clica no ícone do avatar (usando JS)
        WebElement btnAvatar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Configurações da Conta']")
        ));
        js.executeScript("arguments[0].click();", btnAvatar);

        // 2. Clica no item "Gerenciamento de Cursos" (o wait aqui é o suficiente)
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Gerenciamento de Cursos']")
        )).click();

        // 3. Espera a URL de destino carregar
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }
}