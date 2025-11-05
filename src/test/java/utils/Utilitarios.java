package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class Utilitarios {
    public static void centralizarElementoNaTela(WebElement menuAlunos, WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(
                "var element = arguments[0];" +
                        "var y = element.getBoundingClientRect().top + window.pageYOffset - (window.innerHeight / 2) + (element.offsetHeight / 2);" +
                        "window.scrollTo({top: y, behavior: 'smooth'});",
                menuAlunos
        );
    }
}