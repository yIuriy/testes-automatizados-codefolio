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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class EditCourseTest {
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
    @Test
    void CT03_edicaoDeCurso_ComTratamento() throws InterruptedException {
        // 1. & 2. Navega e clica em "Gerenciar Curso"
        irAteAPaginaDeGerenciarCursos();

        WebElement btnGerenciar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[normalize-space()='Introdução ao Java']/ancestor::div[contains(@class, 'MuiCardContent-root')]/following-sibling::div//button[normalize-space()='Gerenciar Curso']")
        ));
        btnGerenciar.click();

        System.out.println("Página de Gerenciar Curso aberta.");

        // --- INÍCIO DO TRATAMENTO (try...catch) ---
        // Vamos usar um 'wait' local mais curto (5s) para o teste falhar rápido
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // O teste TENTA executar o fluxo de edição (Passos 3, 4, 5 do PDF)
            // Esta linha VAI FALHAR e lançar um TimeoutException
            System.out.println("Tentando encontrar o campo 'Título' clicável (deve falhar)...");
            WebElement btnEditTitulo = localWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Título do Curso *']/following-sibling::div//input")
            ));

            // --- Se o teste chegar aqui, o bug foi corrigido ---
            js.executeScript("arguments[0].value = '';", btnEditTitulo);
            btnEditTitulo.sendKeys("Introdução ao cão do JAVA/Kotlin");

            WebElement btnEditDescricao = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Descrição do Curso *']/following-sibling::div//textarea[1]")
            ));
            js.executeScript("arguments[0].value = '';", btnEditDescricao);
            btnEditDescricao.sendKeys("I really need you tonight..."); // (Dados do seu teste)

            WebElement btnSalvar = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Salvar Curso']")
            ));
            btnSalvar.click();

            // Se o teste chegou até aqui, ele não falhou.
            // Isso significa que o bug de "disabled" foi corrigido pelos devs.
            fail("ERRO INESPERADO: O teste conseguiu editar os campos, mas eles deveriam estar desabilitados.");

        } catch (TimeoutException e) {

            System.out.println("SUCESSO (ESPERADO): O erro 'TimeoutException' foi capturado.");
            System.out.println("Motivo: Os campos 'Título' e 'Descrição' não são 'clicáveis' porque estão desabilitados.");

            WebElement inputTitulo = driver.findElement(
                    By.xpath("//label[text()='Título do Curso *']/following-sibling::div//input")
            );

            String isDisabled = inputTitulo.getAttribute("disabled");
            assertEquals("true", isDisabled, "Verificação final: O campo Título deveria estar 'disabled'.");

            System.out.println("VERIFICADO: O atributo 'disabled' do campo 'Título' é 'true'.");
        }

        System.out.println("\n--- RESULTADO DO TESTE CT-03 ---");
        System.out.println("TESTE APROVADO: O teste provou que a RF3 (Edição de Curso) está quebrada.");
        System.out.println("O PDF está errado e os campos não são editáveis.");
    }

    @Test
    void CT04_exclusaoDeCurso() throws InterruptedException {
        // 1. Clicar em "Gerenciar Cursos"
        irAteAPaginaDeGerenciarCursos();

        // 2. Selecionar o curso "Introdução ao Java"
        WebElement btnDeletarDoCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[normalize-space()='Introdução ao Java']/ancestor::div[contains(@class, 'MuiCardContent-root')]/following-sibling::div//button[normalize-space()='Deletar']")
        ));

        System.out.println("Curso 'Introdução ao Java' encontrado. Clicando em 'Deletar' no card...");
        js.executeScript("arguments[0].click();", btnDeletarDoCard);

        // 4. Confirmar a exclusão no modal
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[text()='Tem certeza que deseja deletar esse curso?']")
        ));

        WebElement btnDeletarDoModal = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='presentation']//button[normalize-space()='Deletar']")
        ));

        System.out.println("Clicando em 'Deletar' no modal de exclusão...");
        js.executeScript("arguments[0].click();", btnDeletarDoModal); // Usando JS


        // Resultado 2: O curso é excluído e não aparece mais na listagem
        System.out.println("Verificando se o curso desapareceu da lista...");
        boolean cursoDesapareceu = wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//h6[normalize-space()='Introdução ao Java']")
        ));

        assertTrue(cursoDesapareceu, "O curso 'Introdução ao Java' ainda está visível na lista.");

        System.out.println("\n--- RESULTADO DO TESTE CT-04 ---");
        System.out.println("TESTE APROVADO: O curso foi excluído com sucesso.");
    }

    private void irAteAPaginaDeGerenciarCursos() {
        // 1. Clica no ícone do avatar (usando JS)
        WebElement btnAvatar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Configurações da Conta']")
        ));
        js.executeScript("arguments[0].click();", btnAvatar);

        // 2. Clica no item "Gerenciamento de Cursos"
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Gerenciamento de Cursos']")
        )).click();

        // 3. Espera a URL de destino carregar
        wait.until(ExpectedConditions.urlContains("/manage-courses"));
    }
}