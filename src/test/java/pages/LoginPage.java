package pages;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class LoginPage {
    private final WebDriver driver;
    private WebDriverWait wait;
    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private final JavascriptExecutor js;

    // ajuste se o DB / store tiver nome diferente
    private static final String FIREBASE_DB = "firebaseLocalStorageDb";
    private static final String FIREBASE_STORE = "firebaseLocalStorage";

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void abrir() {
        driver.get("https://react-na-pratica.web.app/login");
    }

    /**
     * Principal entry — tenta injetar vindo do .env; se não existir, lança explicação
     */
    public void realizarLoginViaIndexedBD() {
        String key = dotenv.get("FIREBASE_KEY");
        String value = dotenv.get("FIREBASE_VALUE");

        if (key == null || value == null) {
            throw new IllegalStateException("""
                    FIREBASE_KEY / FIREBASE_VALUE não encontradas nas variáveis de ambiente.
                    Opções:
                      1) Defina FIREBASE_KEY e FIREBASE_VALUE no .env (recomendado).
                      2) Use realizarLoginComKeyValue(key, value) passando as strings copiadas do IndexedDB.
                    """);
        }
        realizarLoginComKeyValue(key, value);
    }

    /**
     * Injeção robusta: espera o indexedDB existir, injeta o registro e recarrega a página.
     * @param firebaseKey  chave exata do IndexedDB (ex: "firebase:authUser:...:[DEFAULT]")
     * @param firebaseValue JSON bruto (o objeto inteiro) como string (não escapado)
     */
    private void realizarLoginComKeyValue(String firebaseKey, String firebaseValue) {
        Objects.requireNonNull(firebaseKey, "firebaseKey == null");
        Objects.requireNonNull(firebaseValue, "firebaseValue == null");

        driver.manage().window().maximize();

        // aumentar um pouco o script timeout porque indexedDB pode demorar
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));
        abrir();

        // 1) espera até que o indexedDB esteja visível/ disponível (evita script timeout)
        boolean dbReady = waitForIndexedDb(FIREBASE_DB, 15);
        if (!dbReady) {
            System.out.println("Aviso: IndexedDB '" + FIREBASE_DB + "' não apareceu em 15s. Ainda assim tentaremos injetar.");
        }

        // 2) script assíncrono para colocar o registro no object store
        String injectScript =
                "const done = arguments[arguments.length - 1];" +
                        "const key = arguments[0];" +
                        "const valueString = arguments[1];" +
                        "let value;" +
                        "try { value = typeof valueString === 'string' ? JSON.parse(valueString) : valueString; } " +
                        "catch(e) { done('PARSE_ERROR:' + e.message); return; }" +

                        // timeout interno: se algo travar no indexedDB, retorna erro rápido
                        "let internalTimed = false;" +
                        "const t = setTimeout(()=>{ internalTimed = true; done('INTERNAL_TIMEOUT'); }, 7000);" +

                        "let openReq = indexedDB.open('" + FIREBASE_DB + "');" +
                        "openReq.onerror = function(ev){ if(!internalTimed){ clearTimeout(t); done('DB_OPEN_ERROR:' + ev.target.error); }};" +
                        "openReq.onsuccess = function(ev) {" +
                        "  if(internalTimed) return;" +
                        "  try{" +
                        "    const db = ev.target.result;" +
                        "    const tx = db.transaction(['" + FIREBASE_STORE + "'], 'readwrite');" +
                        "    tx.onerror = function(e){ if(!internalTimed){ clearTimeout(t); done('TX_ERROR:' + e.target.error); }};" +
                        "    tx.oncomplete = function(){ if(!internalTimed){ clearTimeout(t); done('OK'); }};" +
                        // garante que o campo fbase_key bate com a key (algumas versões salvam isso)
                        "    if (typeof value === 'object') value.fbase_key = key;" +
                        // usar put com chave explícita (para compatibilidade)
                        "    tx.objectStore('" + FIREBASE_STORE + "').put(value);" +
                        "  } catch(err) { if(!internalTimed){ clearTimeout(t); done('INJECT_ERROR:' + err.message); }}" +
                        "};";
        Object result;

        try {
            result = js.executeAsyncScript(injectScript, firebaseKey, firebaseValue);
        } catch (Exception ex) {
            // se o executeAsyncScript itself falhar (ex: script timeout global)
            System.out.println("Falha crítica ao executar script assíncrono: " + ex.getMessage());
            throw new RuntimeException("Falha no setup do IndexedDB", ex);
        }

        System.out.println("Resultado da injeção IndexedDB: " + result);

        if (!"OK".equals(result)) {
            throw new RuntimeException("Erro ao injetar IndexedDB: " + result);
        }

        // 3) recarrega a página para que o app leia o IndexedDB
        driver.navigate().refresh();
        driver.get("https://testes.codefolio.com.br/dashboard");
    }

    /**
     * Espera (poll) até que indexedDB.databases() contenha um DB com o nome dado.
     * Retorna true se apareceu dentro do timeout; false se não apareceu.
     */
    private boolean waitForIndexedDb(String dbName, int timeoutSeconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return shortWait.until(d -> {
                Object res = ((JavascriptExecutor) d).executeAsyncScript(
                        "const cb = arguments[arguments.length-1];" +
                                "if (!indexedDB || !indexedDB.databases) { cb(false); return; }" +
                                "indexedDB.databases().then(dbs => cb(dbs.some(db => db.name === arguments[0]))).catch(()=>cb(false));",
                        dbName);
                return res;
            }).equals(Boolean.TRUE);
        } catch (Exception ignored) {
            return false;
        }
    }
}
