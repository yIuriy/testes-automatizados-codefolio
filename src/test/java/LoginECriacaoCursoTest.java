import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginECriacaoCursoTest {
    private final Dotenv dotenv = Dotenv.load();
    private WebDriver driver;
    private WebDriverWait wait;
    private final Duration TIMEOUT = Duration.ofSeconds(15);
    private final String URL_BASE = "https://testes.codefolio.com.br/";
    private JavascriptExecutor js; // Tornando o JS Executor global

    // --- 1. DADOS DO FIREBASE (PEGUE UM TOKEN NOVO!) ---
    // (Chave do IndexedDB)
    private final String FIREBASE_KEY = dotenv.get("FIREBASE_KEY");

    // (Valor JSON - Objeto 'value' formatado para Java)
    private final String FIREBASE_VALUE = dotenv.get("FIREBASE_VALUE");


    // Adicionamos 'throws InterruptedException' para o Thread.sleep
    @BeforeEach
    public void setup() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, TIMEOUT);
        js = (JavascriptExecutor) driver; // Inicializa o JS Executor

        // --- ESTRATÉGIA LOCAL STORAGE (Formato Correto) ---

        // 1. Carrega o domínio
        driver.get(URL_BASE);

        // 2. Injeta os dados no Local Storage
        System.out.println("Injetando dados de autenticação no Local Storage...");
        try {
            js.executeScript("window.localStorage.setItem(arguments[0], arguments[1]);",
                    FIREBASE_KEY,
                    FIREBASE_VALUE);
            System.out.println("Injeção no Local Storage bem-sucedida.");

        } catch (Exception e) {
            System.out.println("Falha crítica ao injetar no Local Storage: " + e.getMessage());
            driver.quit();
            throw new RuntimeException("Falha no setup do Local Storage", e);
        }

        // 3. Recarrega a página (agora com o token injetado)
        System.out.println("Recarregando a página...");
        driver.navigate().refresh();
    }

    @Test
    public void testFazerLoginECriarCurso() throws InterruptedException {
        // --- 1. Verificação de Login ---
        verificarLogin();

        // --- 2. Processo de Criação de Curso ---
        criarCurso();
    }

    /**
     * Verifica se o login foi bem-sucedido
     */
    private void verificarLogin() throws InterruptedException {
        System.out.println("Verificando se o login foi processado...");
        try {
            // 1. Espera forçada de 5s para o Firebase (JS assíncrono) processar o login
            System.out.println("Aguardando 5s para o Firebase SDK processar o login...");
            Thread.sleep(5000);

            // 2. Clicar no botão de perfil (logado)
            WebElement profileButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("button[aria-label='Configurações da Conta']")
            ));

            js.executeScript("arguments[0].click();", profileButton);
            System.out.println("Clicou no botão de perfil para abrir o menu.");

            // 3. Esperar o menu abrir e o botão "Sair" ficar VISÍVEL
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[normalize-space()='Sair']")
            ));

            System.out.println("Login validado com sucesso!");

            // Clica fora para fechar o menu (opcional)
            js.executeScript("document.body.click();");

        } catch (Exception e) {
            System.out.println("--- ERRO NA VALIDAÇÃO DO LOGIN ---");
            System.out.println("Causa provável: O token no 'FIREBASE_VALUE' expirou ou está incorreto.");
            System.out.println("Exceção: " + e.getMessage());

            assertTrue(false, "Falha na injeção de sessão do Firebase. O token pode estar expirado.");
        }
    }


    /**
     * Módulo que executa o roteiro de criação de curso
     */
    private void criarCurso() {
        System.out.println("Iniciando o roteiro de criação de curso...");

        try {
            // --- Passo 1: Clicar no Ícone de Perfil (LOGADO) ---
            // (Já estamos logados, apenas clicamos no ícone de perfil)
            System.out.println("Clicando no ícone de perfil (logado)...");
            WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[aria-label='Configurações da Conta']")
            ));
            js.executeScript("arguments[0].click();", profileButton);

            // --- Passo 2: Clicar em "Gerenciamento de Cursos" ---
            System.out.println("Clicando em 'Gerenciamento de Cursos'...");
            WebElement gerenciamentoButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[normalize-space()='Gerenciamento de Cursos']")
            ));
            gerenciamentoButton.click();

            // --- Passo 3: Clicar em "Criar Novo Curso" ---
            // Espera a URL de gerenciamento carregar
            wait.until(ExpectedConditions.urlContains("/manage-courses"));
            System.out.println("Clicando em 'Criar Novo Curso'...");
            WebElement criarCursoButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Criar Novo Curso']")
            ));
            criarCursoButton.click();

            // --- Passo 4: Preencher o Formulário ---
            // Espera a URL de admin carregar
            wait.until(ExpectedConditions.urlContains("/adm-cursos"));
            System.out.println("Preenchendo formulário...");

            // Título (Método robusto para IDs dinâmicos)
            WebElement titleLabel = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(),'Título do Curso')]")
            ));
            String titleId = titleLabel.getAttribute("for");
            WebElement titleInput = driver.findElement(By.id(titleId));
            String tituloCurso = "Curso Selenium " + System.currentTimeMillis();
            titleInput.sendKeys(tituloCurso);

            // Descrição (Método robusto para IDs dinâmicos)
            WebElement descLabel = driver.findElement(By.xpath("//label[contains(text(),'Descrição do Curso')]"));
            String descId = descLabel.getAttribute("for");
            WebElement descInput = driver.findElement(By.id(descId));
            descInput.sendKeys("Descrição gerada por teste automatizado.");

            // --- Passo 5: Salvar o Curso ---
            // O botão começa desabilitado. Esperamos ele ficar clicável.
            System.out.println("Aguardando botão 'Salvar Curso' ser habilitado...");
            WebElement salvarButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Salvar Curso']")
            ));
            salvarButton.click();
            System.out.println("Formulário submetido.");

            // --- Verificação Final ---
            System.out.println("Aguardando redirecionamento para a lista de cursos...");
            wait.until(ExpectedConditions.urlContains("/manage-courses"));

            // Verifica se o novo curso aparece na lista (assumindo que o título é um <h6>)
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h6[text()='" + tituloCurso + "']")
            ));

            System.out.println("Teste de Criação de Curso concluído com sucesso!");

        } catch (Exception e) {
            System.out.println("--- ERRO DURANTE A CRIAÇÃO DO CURSO ---");
            System.out.println("Mensagem: " + e.getMessage());
            e.printStackTrace(); // Mostra o erro detalhado
            assertTrue(false, "Falha durante a criação do curso.");
        }
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            // driver.quit(); 
        }
    }
}