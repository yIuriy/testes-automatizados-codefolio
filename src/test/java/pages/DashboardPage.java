package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait; // <-- Adicionado

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        // Inicializa o wait
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // <-- Adicionado
    }

    public void abrir(){
        driver.get("https://react-na-pratica.web.app/dashboard");
    }

    public void abrirMenuDeOpcoesPerfil(){
        driver.findElement(By.xpath("//*[@id=\":r5:\"]/div/img")).click();
    }

    public void abrirMenuGerenciamentoDeCursos(){
        driver.findElement(By.xpath("/html/body/div[2]/div[3]/ul/li[2]")).click();
    }

    //MÉTODO PARA O CT-33
    /**
     * Localiza um curso na seção "Cursos Recomendados" pelo nome e clica em "Acessar".
     * @param nomeDoCurso O nome exato do curso a ser acessado.
     */
    public void acessarCursoRecomendadoPorNome(String nomeDoCurso) {
        // Espera o título "Cursos Recomendados"
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[contains(text(), 'Cursos Recomendados')]")
        ));

        // Encontra o botão "Acessar" dentro do card do curso
        WebElement botaoAcessar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h6[contains(text(), '" + nomeDoCurso + "')]" +
                         "/ancestor::div[contains(@class, 'MuiCard-root')]" +
                         "//button[contains(text(), 'Acessar')]")
        ));
        
        botaoAcessar.click();
    }
}