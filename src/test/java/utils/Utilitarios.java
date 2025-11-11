package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class Utilitarios {

    /**
     * Centraliza um elemento na tela.
     *
     * @param elemento o elemento a ser centralizado
     * @param driver o web driver
     *
     */
    public static void centralizarElementoNaTela(WebElement elemento, WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(
                "var element = arguments[0];" +
                        "var y = element.getBoundingClientRect().top + window.pageYOffset - (window.innerHeight / 2) + (element.offsetHeight / 2);" +
                        "window.scrollTo({top: y, behavior: 'smooth'});",
                elemento
        );
    }

    /**
     * Move a tela para cima ou para baixo.
     *
     * @param js o executor javascript
     * @param valor o valor em pixeis a ser scrollado, use valores positivos para scrollar para baixo e valores negativas para
     *              scrollar para cima
     * */
    public static void scrollarTela(JavascriptExecutor js, String valor){
        js.executeScript("window.scrollBy({top: -500})");
    }
}