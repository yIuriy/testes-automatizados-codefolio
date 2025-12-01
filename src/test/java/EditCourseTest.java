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
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void CT_05_cadastroDeVideo() {
        irAteAPaginaDeGerenciarCursos();
        WebElement btnGenDoCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[normalize-space()='Introdução ao Java']/ancestor::div[contains(@class, 'MuiCardContent-root')]/following-sibling::div//button[normalize-space()='Gerenciar Curso']")
        ));
        js.executeScript("arguments[0].click();", btnGenDoCard);

        WebElement inputTitulo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Título do Vídeo']/following-sibling::div//input")
        ));
        inputTitulo.sendKeys("One Frame");

        WebElement inputDescricao = driver.findElement(
                By.xpath("//label[text()='Descrição do Vídeo']/following-sibling::div//textarea[1]")
        );

        inputDescricao.sendKeys("Apresentação inicial sobre variáveis e tipos");

        WebElement inputLink = driver.findElement(
                By.xpath("//label[text()='URL do Vídeo']/following-sibling::div//input")
        );

        inputLink.sendKeys("https://www.youtube.com/watch?v=abc123");


        WebElement btnSalvarVideo = driver.findElement(
                By.xpath("//button[normalize-space()='Adicionar Vídeo']")
        );

        js.executeScript("arguments[0].click();", btnSalvarVideo);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[text()='Vídeo adicionado com sucesso!']")
        ));

        WebElement btnOkDoModal = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='presentation']//button[normalize-space()='OK']")
        ));

        js.executeScript("arguments[0].click();", btnOkDoModal);

        WebElement videoNaLista = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='One Frame']")
        ));


        assertTrue(videoNaLista.isDisplayed(), "O vídeo 'One Frame' não foi encontrado na lista.");
        System.out.println("\n--- RESULTADO DO TESTE CT-05 ---");
        System.out.println("TESTE APROVADO: Vídeo cadastrado com sucesso.");
    }

    @Test
    void CT_06_edicaoVideo() {
        irAteAPaginaDeGerenciarCursos();

        // 1. Entrar no curso
        // Botão padrão do card -> Clique Normal
        WebElement btnGenDoCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[contains(text(), 'Introdução ao Java')]/ancestor::div[contains(@class, 'MuiCard-root')]//button[normalize-space()='Gerenciar Curso']")
        ));
        btnGenDoCard.click();

        wait.until(ExpectedConditions.urlContains("courseId"));

        // 2. Ir para a aba Vídeos -> Clique Normal
        WebElement abaVideos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Vídeos')]")
        ));
        abaVideos.click();

        // 3. Clicar no botão de EDITAR (Ícone SVG)
        // AQUI USAMOS O FORCAR CLIQUE (Estratégia do SVG)
        WebElement iconeEditar = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[name()='svg' and @data-testid='EditIcon']")
        ));
        WebElement btnEditar = iconeEditar.findElement(By.xpath("./..")); // Pega o botão pai

        forcarClique(btnEditar);

        // 4. Editar os campos
        WebElement inputTitulo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Título do Vídeo']/following-sibling::div//input")
        ));
        // Limpeza segura para React
        inputTitulo.sendKeys(Keys.CONTROL + "a");
        inputTitulo.sendKeys(Keys.DELETE);
        inputTitulo.sendKeys("Vídeo Editado - Teste Automatizado");


        WebElement inputDesc = driver.findElement(
                By.xpath("//label[text()='Descrição do Vídeo']/following-sibling::div//textarea[1]")
        );
        inputDesc.sendKeys(Keys.CONTROL + "a");
        inputDesc.sendKeys(Keys.DELETE);
        inputDesc.sendKeys("Descrição atualizada via Selenium");

        // 5. Salvar (Botão Azul/Roxo com texto)
        // AQUI USAMOS CLIQUE NORMAL (Baseado na imagem enviada com texto "Editar Vídeo")
        WebElement btnSalvar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Editar Vídeo')]")
        ));
        btnSalvar.click();

        // 6. Validar Sucesso
        WebElement toastSucesso = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'sucesso')]")
        ));
        assertTrue(toastSucesso.isDisplayed(), "Mensagem de sucesso não apareceu!");

        // Fecha o modal clicando em OK (se existir)
        try {
            WebElement btnOk = driver.findElement(By.xpath("//div[@role='presentation']//button[normalize-space()='OK']"));
            btnOk.click();
        } catch (Exception e) { /* Ignora se o modal fechar sozinho */ }

        // 7. Validação Final: Verifica se o título mudou na lista
        WebElement tituloNaLista = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Vídeo Editado - Teste Automatizado')]")
        ));
        assertTrue(tituloNaLista.isDisplayed());
        System.out.println("Passou");
    }
    @Test
    void CT07_exclusaoVideo(){
        irAteAPaginaDeGerenciarCursos();

        WebElement btnGenDoCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[contains(text(), 'Introdução ao Java')]/ancestor::div[contains(@class, 'MuiCard-root')]//button[normalize-space()='Gerenciar Curso']")
        ));
        forcarClique(btnGenDoCard);
        wait.until(ExpectedConditions.urlContains("courseId"));

        WebElement abaVideos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Vídeos')]")
        ));
        forcarClique(abaVideos);


        // 3. Clicar no botão de EXCLUIR
        // Mesma estratégia: acha o ícone da lixeira e clica no pai dele
        WebElement iconeExcluir = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[name()='svg' and @data-testid='DeleteIcon']")
        ));

        WebElement btnExcluir = iconeExcluir.findElement(By.xpath("./.."));

        System.out.println("Botão de excluir encontrado. Tentando clicar...");
        forcarClique(btnExcluir);

        // 4. Confirmar exclusão
        WebElement btnConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Confirmar') or contains(text(), 'Sim') or contains(text(), 'Deletar')]")
        ));
        forcarClique(btnConfirmar);

        // 5. Validar mensagem
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'sucesso')]")
        ));

        try {
            WebElement btnOk = driver.findElement(By.xpath("//div[@role='presentation']//button[normalize-space()='OK']"));
            forcarClique(btnOk);
        } catch (Exception e) { }

        // 6. Validação: Verifica se o ícone da lixeira sumiu (lista vazia) ou se o vídeo específico sumiu
        boolean lixeiraSumiu = wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[name()='svg' and @data-testid='DeleteIcon']")
        ));

        // Se lixeiraSumiu for true, significa que o botão não está mais na tela (deletado com sucesso)
        assert lixeiraSumiu;
        System.out.println("Foi excluido!");
    }

    @Test
    void CT08_CadastrodeSlides(){
        irAteAPaginaDeGerenciarCursos();
        WebElement btnGenDoCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[contains(text(), 'Introdução ao Java')]/ancestor::div[contains(@class, 'MuiCard-root')]//button[normalize-space()='Gerenciar Curso']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnGenDoCard);
        wait.until(ExpectedConditions.urlContains("courseId"));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Slides')]")
        )).click();

        WebElement inputTitulo = driver.findElement(
                By.xpath("//label[text()='Título do Slide']/following-sibling::div//input")
        );
        inputTitulo.sendKeys("Tutorial de como se acalmar depois de mexer com o Selenium");
        WebElement inputLink = driver.findElement(
                By.xpath("//label[text()='URL do Slide (Google Apresentações)']/following-sibling::div//input")
        );
        inputLink.sendKeys("TesteLink");

        WebElement btnSalvarSlide = driver.findElement(
                By.xpath("//button[normalize-space()='Adicionar Slide']")
        );

        js.executeScript("arguments[0].click();", btnSalvarSlide);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[text()='O slide foi adicionado com sucesso!']")
        ));

        WebElement btnOkDoModal = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='presentation']//button[normalize-space()='OK']")
        ));

        js.executeScript("arguments[0].click();", btnOkDoModal);

        WebElement slideNaLista = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Tutorial de como se acalmar depois de mexer com o Selenium')]")
        ));

        // Opcional: Garantia extra de que está visível (embora o wait acima já garanta isso)
        assert slideNaLista.isDisplayed();
        System.out.println("tá na lista!");
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

    private void forcarClique(WebElement elemento) {
        try {
            // Tentativa 1: Clique Nativo com Wait
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            elemento.click();
        } catch (Exception e) {
            try {
                // Tentativa 2: Javascript Executor (Padrão)
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            } catch (Exception e2) {
                // Tentativa 3: Javascript Event Dispatcher (Para React/MUI teimosos)
                ((JavascriptExecutor) driver).executeScript(
                        "var ev = document.createEvent('MouseEvent');" +
                                "ev.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                                "arguments[0].dispatchEvent(ev);", elemento);
            }
        }
    }
}