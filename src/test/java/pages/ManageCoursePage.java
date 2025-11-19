package pages;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilitarios;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Classe que armazena métodos referentes à página de gerenciar cursos.*
 */
public class ManageCoursePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public ManageCoursePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    /**
     * Abre a página de gerenciar cursos, não recomendado, pois dá conflito com a autenticação.
     *
     */
    @Deprecated
    public void abrir() {
        driver.get("https://testes.codefolio.com.br/manage-courses");
    }

    /**
     * Clica no botão de Gerenciar Curso do primeiro curso exibido.
     * Prefira {@link #clicarBotaoGerenciarCursoPorNomeDoCurso(String)} para mais controle.
     *
     */
    public void clicarBotaoGerenciarCursoDoPrimeiroCurso() {
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Gerenciar Curso']")
        ))).click();
    }

    /**
     * Clicar no botão de Gerenciar Curso com base no nome do curso.
     *
     * @param nome o nome do curso
     *
     */
    public void clicarBotaoGerenciarCursoPorNomeDoCurso(String nome) {
        WebElement div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                "//div[contains(@class, 'MuiGrid-item') and .//h6[contains(normalize-space(), '" + nome + "')]]")));
        div.findElement(By.xpath(".//button[contains(., 'Gerenciar Curso')]")).click();
    }

    /**
     * Clica em um dos menus da página de gerenciar cursos com base no nome do menu.<br>
     * Menus disponíveis:
     * <ul>
     *     <li>Vídeos</li>
     *     <li>Slides</li>
     *     <li>Materiais Extras</li>
     *     <li>Quiz</li>
     *     <li>Alunos</li>
     *     <li>Avaliações</li>
     *     </ul>
     *
     * @param nomeDoMenu o nome do menu
     */
    public void localizarEClicarNoMenuPorNome(String nomeDoMenu) {
        WebElement menuAlunos = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(
                "//button[contains(text(),'" + nomeDoMenu + "')]"))));
        Utilitarios.centralizarElementoNaTela(menuAlunos, driver);

        assert menuAlunos != null;
        menuAlunos.click();
    }

    /**
     * Retorna o WebElement referente ao input de filtragem de alunos.
     *
     * @return o elemento input para filtrar alunos por nome ou email
     *
     */
    public WebElement getInputFiltrar() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@placeholder, 'Buscar')]")));
    }

    /**
     * Insere um texto no input de filtrar alunos.
     *
     * @param texto o texto a ser inserido
     *
     */
    public void inserirTextoNoFiltrar(String texto) {
        WebElement inputFiltrar = getInputFiltrar();
        assertNotNull(inputFiltrar);
        Utilitarios.centralizarElementoNaTela(inputFiltrar, driver);
        inputFiltrar.sendKeys(texto);
    }

    /**
     * Limpa o input de filtrar alunos.
     *
     */
    public void limparTextoNoFiltrar() {
        WebElement inputFiltrar = getInputFiltrar();
        assertNotNull(inputFiltrar);
        Utilitarios.centralizarElementoNaTela(inputFiltrar, driver);
        inputFiltrar.sendKeys(Keys.CONTROL + "a");
        inputFiltrar.sendKeys(Keys.DELETE);
    }

    /**
     * Filtra aluno por progresso.
     * Existem os seguintes níveis de progresso:
     * <ul>
     *     <li>Todos</li>
     *     <li>Concluído (100%)</li>
     *     <li>Avançado (75-99%)</li>
     *     <li>Intermediário (25-74%)</li>
     *     <li>Iniciante (0-24%)</li>
     * </ul>
     *
     * @param opcao o nível de progresso a ser filtrado.
     */
    public void filtrarAlunosPorProgresso(String opcao) {
        clicarInputFiltroPorId("progress-filter");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), '" + opcao + "')]")
        ))).click();
    }

    /**
     * Filtra aluno por role.
     * Existem os seguintes roles:
     * <ul>
     *     <li>Todos</li>
     *     <li>Estudante</li>
     *     <li>Professor</li>
     *     <li>Admin</li>
     * </ul>
     *
     * @param role o role a ser filtrado.
     */
    public void filtrarAlunosPorRole(String role) {
        clicarInputFiltroPorId("role-filter");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), '" + role + "')]")
        ))).click();
    }

    /**
     * Clica na opção de ordenar alunos de A até Z.
     *
     */
    public void clicarEmOrdenarDeAZ() {
        clicarInputFiltroPorId("sort-select");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), 'Nome (A-Z)')]")
        ))).click();
    }

    /**
     * Clica na opção de ordenar alunos de Z até A.
     *
     */
    public void clicarEmOrdenarDeZA() {
        clicarInputFiltroPorId("sort-select");
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), 'Nome (Z-A)')]")
        ))).click();
    }

    /**
     * Clica no input de filtrar alunos com base no id.
     * Existem 3 id's válidos:
     * <ul>
     *     <li>"sort-select" => input de ordenação</li>
     *     <li>"role-filter" => input de role</li>
     *     <li>"progress-filter" => input de progresso</li>
     * </ul>
     *
     * @param id o id do filtro
     *
     */
    private void clicarInputFiltroPorId(String id) {
        WebElement until = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id(id)
                )
        );
        WebElement webElement = until.findElement(By.xpath(".."));
        webElement.click();
    }

    /**
     * Localiza a linha que contém as informações do aluno com base no nome do aluno.
     *
     * @param nome o nome do aluno
     * @return o tr(table row) do aluno com o nome passado
     *
     */
    public WebElement localizarLinhaDoAlunoPorNome(String nome) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[.//p[contains(text(),'" + nome + "')]]")
        ));
    }

    /**
     * Verifica se existe o ícone de deletar no elementos dentro do tr do aluno.
     *
     * @return true se existe; false caso contrário
     *
     */
    public boolean verificarSeExisteIconeDeDeletarAluno(List<WebElement> elementos) {
        for (WebElement elemento : elementos) {
            List<WebElement> buttons = elemento.findElements(By.cssSelector("button, input[type='button']"));
            if (!buttons.isEmpty()) {
                for (WebElement elemento1 : buttons) {
                    WebElement webElement = elemento1.findElement(By.cssSelector("[data-testid]"));
                    if (Objects.requireNonNull(webElement.getAttribute("data-testid")).equalsIgnoreCase("DeleteIcon"))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Clica no ícone de excluir do aluno passado.
     *
     * @param trAluno o tr do aluno a ser excluído.
     *
     */
    public void clicarIconeDeExcluirAluno(WebElement trAluno) {
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(trAluno.findElement(By.cssSelector("button"))))).click();
    }

    /**
     * Localiza a caixa de mensagem exibida após clicar no ícone de excluir de um aluno.
     *
     * @return a caixa de mensagem
     *
     */
    @NotNull
    public WebElement localizarCaixaDeTextoConfirmarCancelarExclusao() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains" +
                "(normalize-space(), " +
                "'Confirmar exclusão')]"))).findElement(By.xpath(".."));
    }

    /**
     * Clica no botão de confirmar exclusão do aluno.
     *
     */
    public void clicarBotaoConfirmarExclusaoDeAluno() {
        WebElement div = localizarCaixaDeTextoConfirmarCancelarExclusao();
        div.findElements(By.cssSelector("button")).get(1).click();

    }

    /**
     * Clica no botão de cancelar exclusão do aluno.
     *
     */
    public void clicarBotaoCancelarExclusaoDeAluno() {
        WebElement div = localizarCaixaDeTextoConfirmarCancelarExclusao();
        div.findElements(By.cssSelector("button")).getFirst().click();
    }

    /**
     * Vai até a seção de avaliações cadastradas.
     *
     */
    public void irAteSecaoAvaliacoesCadastradas() {
        WebElement tituloSecao = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[contains(text(), 'Avaliações Cadastradas')]")
        ));
        Utilitarios.centralizarElementoNaTela(tituloSecao, driver);
    }

    /**
     * Localiza e retorna o tr de uma avaliação com base no nome dela.
     *
     * @param nome o nome da avaliação
     * @return o tr(table row) contendo a avaliação
     */
    public WebElement localizarLinhaDaAvaliacaoPorNome(String nome) {
        WebElement trAvaliacao = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[.//td[contains(text(),'" + nome + "')]]")
        ));
        List<WebElement> elementosDentroDoTrAvaliacao = trAvaliacao.findElements(By.tagName("td"));
        assertEquals(nome, elementosDentroDoTrAvaliacao.getFirst().getText());
        return trAvaliacao;
    }

    /**
     * Clica no botão de atribuir nota de uma avaliação passada.
     *
     * @param trAvaliacao o tr da avaliação em que se deseja atribuir nota
     *
     */
    public void clicarBotaoDeAtribuirNota(WebElement trAvaliacao) {
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                trAvaliacao.findElement(By.xpath(
                        ".//button[contains(text(), 'Atribuir Nota')]"
                ))
        ))).click();
    }

    /**
     * Localiza e retorna o input de nota de um aluno.
     *
     * @param trAluno o tr do aluno
     * @return o input de nota do aluno passado
     *
     */
    public WebElement localizarInputDeNota(WebElement trAluno) {
        return wait.until(ExpectedConditions.elementToBeClickable(trAluno.findElement(By.tagName("input"))
        ));
    }

    /**
     * Obtém a nota presente no input de nota do aluno.
     *
     * @param inputNota o input contendo a nota
     * @return o valor da nota dentro do input
     */
    public String obterNotaDoAluno(WebElement inputNota) {
        return inputNota.getAttribute("value");
    }

    /**
     * Localiza e retorna o tr do primeiro aluno sem nota atribuída.
     *
     * @return o tr do primeiro aluno sem nota atribuída
     *
     */
    public WebElement localizarLinhaDoAlunoSemNotaAtribuida() {
        List<WebElement> inputLista = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tbody[.//tr[.//td]]")
        )).findElements(By.tagName("input"));

        inputLista = inputLista.stream().filter(webElement ->
                Objects.equals(webElement.getAttribute("value"), "")).toList();

        WebElement primeiroTrAlunoSemNota = inputLista.getFirst();

        return primeiroTrAlunoSemNota.findElement(By.xpath("./ancestor::tr"));
    }

    /**
     * Obtém o aria-label do svg que exibe se uma nota digitada é válida ou não.
     *
     * @param trAlunoSemNotaAtribuida a tr do aluno sem nota atribuída
     * @return o valor dentro do aria-label do svg
     *
     */
    public String obterValorDoSvgDeConfirmacaoSeNotaFoiSalvaCorretamente(WebElement trAlunoSemNotaAtribuida) {
        WebElement tr = trAlunoSemNotaAtribuida.findElements(By.tagName("td")).get(2);
        String svg = tr.findElements(By.tagName("svg")).getFirst().getAttribute("aria-label");
        assertNotNull(svg);
        return svg;
    }

    /**
     * Obtém a soma do percentual de todas as avaliações cadastradas em um curso.
     *
     * @return o valor da soma
     *
     */
    public int obterSomaDoPercentualDeTodasAsAvaliacoesCadastradas() {
        WebElement tbody = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.tagName("tbody")
                )
        );
        int somaTotal = 0;
        for (WebElement e : tbody.findElements(By.tagName("tr"))) {
            List<WebElement> elements = e.findElements(By.xpath("td"));
            int percentual = Integer.parseInt(elements.get(1).getText().replace("%", ""));
            somaTotal += percentual;
        }
        return somaTotal;
    }

    /**
     * Obtém o texto que informa qual a soma do percentual de todas as avaliações cadastradas em um curso.
     *
     * @return o texto com o percentual total no formato "Total: XX% da nota final"
     */
    public String obterTextoDoTotalDaNotaFinal() {
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h6[contains(text(),'Total:')]")
                )
        ).getText();
    }

    /**
     * Obtém o table body que guarda todas os tr com informações dos alunos.
     *
     * @return o tbody que armazena os alunos
     */
    public WebElement obterTableBodyDosAlunosExibidos() {
        WebElement trAlunos = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tbody")
        ));
        return trAlunos;
    }

    /**
     * Clica no botão de remover filtros ativos.
     */
    public void clicarRemoverFiltroAtivo() {
        WebElement divContainerFiltroAtivo = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@aria-label='Limpar todos os filtros']")
        ));

        divContainerFiltroAtivo.findElement(By.tagName("svg")).click();
    }

    public WebElement obterRoleAluno(WebElement trAluno) {
        List<WebElement> elementosDentroDoTrAluno = trAluno.findElements(By.cssSelector("th, td"));
        return elementosDentroDoTrAluno.get(4);
    }

    public String obterValorRoleAluno(WebElement trAluno) {
        return obterRoleAluno(trAluno).getText();
    }

    public void alterarRoleDoAluno(WebElement trAluno, String novoRole) {
        obterRoleAluno(trAluno).click();
        Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), '" + novoRole + "')]")
        ))).click();
    }


}

