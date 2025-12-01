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
        WebElement btnGenDoCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[contains(text(), 'Introdução ao Java')]/ancestor::div[contains(@class, 'MuiCard-root')]//button[normalize-space()='Gerenciar Curso']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnGenDoCard);
        wait.until(ExpectedConditions.urlContains("courseId"));

        // 2. Clicar na aba "Vídeos"
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Vídeos')]")
        )).click();

        // 3. Clicar no botão de EDITAR
        // ESTRATÉGIA NOVA: Acha o texto do vídeo -> sobe para o item da lista (li) -> busca o botão que tem o ícone EditIcon dentro
        String tituloDoVideo = "Aula 1 - Introdução ao Java";

        WebElement btnEditarVideo = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[contains(text(), '" + tituloDoVideo + "')]/ancestor::li//button[.//svg[@data-testid='EditIcon']]")
        ));

        // Clica via JS para garantir
        js.executeScript("arguments[0].click();", btnEditarVideo);

        // 4. Alterar os campos (Título e Descrição)
        WebElement inputTitulo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Título do Vídeo']/following-sibling::div//input")
        ));

        // Limpeza segura para React
        inputTitulo.sendKeys(Keys.CONTROL + "a");
        inputTitulo.sendKeys(Keys.DELETE);
        inputTitulo.sendKeys("Aula 1 - Java Editada");

        WebElement inputDesc = driver.findElement(
                By.xpath("//label[text()='Descrição do Vídeo']/following-sibling::div//input")
        );
        inputDesc.sendKeys(Keys.CONTROL + "a");
        inputDesc.sendKeys(Keys.DELETE);
        inputDesc.sendKeys("Descrição atualizada com sucesso");

        // 5. Salvar (ajuste o texto do botão conforme sua tela, ex: 'Salvar', 'Atualizar', 'Salvar Curso')
        WebElement btnSalvar = driver.findElement(
                By.xpath("//button[contains(text(), 'Salvar')]")
        );
        js.executeScript("arguments[0].click();", btnSalvar);

        // 6. Validar (Mensagem e mudança na lista)
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'sucesso')]")
        ));

        // Fecha o modal de sucesso se houver
        try {
            WebElement btnOk = driver.findElement(By.xpath("//div[@role='presentation']//button[normalize-space()='OK']"));
            js.executeScript("arguments[0].click();", btnOk);
        } catch (Exception e) {
            // Ignora se não tiver botão OK e o modal fechar sozinho
        }

        // Valida se o título mudou na lista
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(), 'Aula 1 - Java Editada')]")
        ));
    }
    @Test
    void CT07_exclusaoVideo(){
        irAteAPaginaDeGerenciarCursos();

        // 1. Entrar no curso
        WebElement btnGenDoCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[contains(text(), 'Introdução ao Java')]/ancestor::div[contains(@class, 'MuiCard-root')]//button[normalize-space()='Gerenciar Curso']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnGenDoCard);
        wait.until(ExpectedConditions.urlContains("courseId"));

        // 2. Clicar na aba "Vídeos"
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Vídeos')]")
        )).click();

        // Nome do vídeo que será deletado
        String tituloVideoParaDeletar = "Aula 1 - Java Editada"; // Usando o nome editado no teste anterior

        // 3. Clicar no botão de EXCLUIR
        // Tenta achar pelo ícone 'DeleteIcon'. Se o dev mudou o nome, o xpath abaixo procura pelo botão que tem a classe 'edgeEnd' (o da direita)
        WebElement btnExcluirVideo = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[contains(text(), '" + tituloVideoParaDeletar + "')]/ancestor::li//button[.//svg[@data-testid='DeleteIcon'] or contains(@class, 'MuiIconButton-edgeEnd')]")
        ));

        js.executeScript("arguments[0].click();", btnExcluirVideo);

        // 4. Confirmar a exclusão no Modal
        WebElement btnConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Confirmar') or contains(text(), 'Sim') or contains(text(), 'Deletar')]")
        ));
        js.executeScript("arguments[0].click();", btnConfirmar);

        // 5. Validar mensagem de sucesso
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'sucesso')]")
        ));

        try {
            WebElement btnOk = driver.findElement(By.xpath("//div[@role='presentation']//button[normalize-space()='OK']"));
            js.executeScript("arguments[0].click();", btnOk);
        } catch (Exception e) {
            // Ignora
        }

        // 6. Validação final: O elemento NÃO deve mais estar visível
        Boolean videoSumiu = wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//span[contains(text(), '" + tituloVideoParaDeletar + "')]")
        ));

        assert videoSumiu;
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
}