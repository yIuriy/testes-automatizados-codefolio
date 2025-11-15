package pages;

import java.time.Duration;
import java.awt.*;
import java.awt.datatransfer.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MenuHomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public MenuHomePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    public void abrir() {
        driver.get("https://testes.codefolio.com.br/");
    }

    private WebElement getVideoPorTitulo(String titulo) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[.//div[contains(normalize-space(), '" + titulo + "')]]")));
    }

    public boolean verificarSeEntrouNoCurso(){
        try{
        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(normalize-space(), 'Materiais Extras')]")
        ));
        return true;
        }catch(Exception e){
        return false;
        }
    }

    public void acessarCursoSemPIN() {

    WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//div[./h6[normalize-space() = 'Cursos Recomendados']]")
    ));

    WebElement curso = div.findElement(
        By.xpath(".//div[contains(@class, 'MuiCard-root') and not(.//svg[@data-testid='LockIcon'])]")
    );

    WebElement btnAcessar = curso.findElement(
        By.xpath(".//button[contains(normalize-space(), 'Acessar')]")
    );

    btnAcessar.click();
}

    public void clicarEmLike(String tituloVideo) {

        WebElement div = getVideoPorTitulo(tituloVideo);

        div.findElement(By.xpath(".//button[.//*[@data-testid='ThumbUpIcon']]")).click();
    }

    public void clicarEmDislike(String tituloVideo) {

        WebElement div = getVideoPorTitulo(tituloVideo);

        div.findElement(By.xpath(".//button[.//*[@data-testid='ThumbDownIcon']]")).click();
    }

    public int pegarNumeroDeLikes(String tituloVideo) {
        WebElement div = getVideoPorTitulo(tituloVideo);

        String texto = div.findElement(By.xpath(".//div[contains(@class, 'info-likes')]")).getText();
        String[] partes = texto.split(" ");

        return Integer.parseInt(partes[0]);

    }

    public void clicarEmComentarios(String tituloVideo) {
        WebElement div = getVideoPorTitulo(tituloVideo);

        div.findElement(By.xpath(".//span[normalize-space(text())='Comentários']/ancestor::div[1]")).click();
    }

    public void comentar(String tituloVideo, String texto) {
        WebElement div = getVideoPorTitulo(tituloVideo);

        WebElement input = div.findElement(By.xpath(".//input[contains(normalize-space(@placeholder), 'comentário')]"));

        input.click();
        input.clear();
        input.sendKeys(texto);

        div.findElement(By.xpath(
                ".//button[.//*[@data-testid='SendIcon']]")).click();

    }

    public String visualizarComentario(String tituloVideo, String texto) {
        WebElement div = getVideoPorTitulo(tituloVideo);

        div.findElement(By.xpath(
                ".//button[.//*[contains(., 'Ver')]]")).click();

        WebElement comentario;
        try {
            comentario = div.findElement(By.xpath(
                    ".//span[contains(., '" + texto + "')]"));
        } catch (Exception e) {
            return null;
        }

        return comentario.getText();
    }

    public void clicarEmCompartilhar(String tituloVideo) {
        WebElement div = getVideoPorTitulo(tituloVideo);

        div.findElement(By.xpath(".//span[normalize-space(text())='Compartilhar']/ancestor::div[1]")).click();
    }

    public String pegarTextoDaAreaDeTransferencia() {
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}