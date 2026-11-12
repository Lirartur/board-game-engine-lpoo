package engine.terminal;

import engine.excecoes.Excecoes.ConfiguracaoInvalida;
import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.EstadoDoJogo;
import engine.modelo.Jogador;
import engine.modelo.Jogo;
import engine.modelo.Peca;
import engine.modelo.Posicao;
import engine.modelo.Resultado;
import engine.modelo.Tabuleiro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Stream;

public class InterfaceDeTerminal {

    private final Scanner scanner;

    public InterfaceDeTerminal(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirBanner() {
        System.out.println("================================");
        System.out.println("          BOARD ENGINE");
        System.out.println("================================");
    }

    public Path escolherArquivo(Path pasta) throws ConfiguracaoInvalida {
        List<Path> arquivos;
        try (Stream<Path> fluxo = Files.list(pasta)) {
            arquivos = fluxo
                    .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".json"))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new ConfiguracaoInvalida("Nao foi possivel abrir a pasta '" + pasta + "'.");
        }
        if (arquivos.isEmpty()) {
            throw new ConfiguracaoInvalida("Nenhum jogo encontrado na pasta '" + pasta + "'.");
        }

        System.out.println();
        System.out.println("Selecione o jogo:");
        System.out.println();
        for (int i = 0; i < arquivos.size(); i++) {
            String nome = arquivos.get(i).getFileName().toString().replaceFirst("\\.json$", "");
            System.out.println((i + 1) + " - " + nome);
        }
        while (true) {
            System.out.print("Opcao: ");
            try {
                int opcao = Integer.parseInt(lerLinha().trim());
                if (opcao >= 1 && opcao <= arquivos.size()) {
                    return arquivos.get(opcao - 1);
                }
                exibirErro("Escolha um numero entre 1 e " + arquivos.size() + ".");
            } catch (NumberFormatException e) {
                exibirErro("Digite apenas o numero do jogo.");
            }
        }
    }

    public List<Jogador> criarJogadores(int quantidade, List<String> simbolosSugeridos) {
        List<Jogador> jogadores = new ArrayList<>();
        List<String> usados = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            String sugestao = i < simbolosSugeridos.size() ? simbolosSugeridos.get(i) : null;
            String simbolo = lerSimbolo(i + 1, sugestao, usados);
            usados.add(simbolo);
            jogadores.add(new Jogador("Jogador " + (i + 1), simbolo));
        }
        return jogadores;
    }

    public void executar(Jogo jogo) {
        boolean porColuna = jogo.usaNumeroDaColuna();
        System.out.println();
        System.out.println("Jogo: " + jogo.getNome());
        while (!jogo.terminou()) {
            desenharTabuleiro(jogo.getTabuleiro(), porColuna);
            Jogador jogador = jogo.getJogadorDaVez();
            Posicao posicao = lerPosicao(jogador, jogo.getTabuleiro(), porColuna);
            try {
                jogo.jogar(posicao);
            } catch (PosicaoInvalida | JogadaInvalida e) {
                exibirErro(e.getMessage());
            }
        }
        desenharTabuleiro(jogo.getTabuleiro(), porColuna);
        exibirResultado(jogo.getResultado());
    }

    public void exibirErro(String mensagem) {
        System.out.println("Erro: " + mensagem);
    }

    public void exibirErroDeCarregamento(String mensagem) {
        System.out.println();
        System.out.println("Erro ao carregar o jogo:");
        System.out.println();
        System.out.println(mensagem);
    }

    private void exibirResultado(Resultado resultado) {
        System.out.println();
        if (resultado.estado() == EstadoDoJogo.VITORIA) {
            System.out.println("Fim de jogo! Vencedor: " + resultado.vencedor());
        } else {
            System.out.println("Fim de jogo! Empate.");
        }
    }

    private String lerSimbolo(int numeroDoJogador, String sugestao, List<String> usados) {
        while (true) {
            String dica = sugestao != null ? ", Enter para '" + sugestao + "'" : "";
            System.out.print("Jogador " + numeroDoJogador
                    + " - escolha seu simbolo (1 caractere" + dica + "): ");
            String texto = lerLinha();
            if (texto.isEmpty() && sugestao != null) {
                texto = sugestao;
            }
            if (texto.codePointCount(0, texto.length()) != 1) {
                exibirErro("O simbolo deve ter exatamente 1 caractere.");
            } else if (Character.isWhitespace(texto.codePointAt(0))) {
                exibirErro("O simbolo nao pode ser um espaco.");
            } else if (usados.contains(texto)) {
                exibirErro("Esse simbolo ja foi escolhido por outro jogador.");
            } else {
                return texto;
            }
        }
    }

    private Posicao lerPosicao(Jogador jogador, Tabuleiro tabuleiro, boolean porColuna) {
        int colunas = tabuleiro.getColunas();
        String alvo = porColuna ? "da coluna" : "da posicao";
        while (true) {
            System.out.print(jogador + ", informe o numero " + alvo + ": ");
            String texto = lerLinha().trim();
            try {
                int numero = Integer.parseInt(texto);
                if (numero < 1) {
                    exibirErro("Digite um numero maior que zero.");
                    continue;
                }
                if (porColuna) {
                    if (numero > colunas) {
                        exibirErro("Escolha uma coluna de 1 a " + colunas + ".");
                        continue;
                    }
                    return new Posicao(0, numero - 1);
                }
                return new Posicao((numero - 1) / colunas, (numero - 1) % colunas);
            } catch (NumberFormatException e) {
                exibirErro("Digite apenas o numero " + alvo + ".");
            }
        }
    }

    private void desenharTabuleiro(Tabuleiro tabuleiro, boolean porColuna) {
        int linhas = tabuleiro.getLinhas();
        int colunas = tabuleiro.getColunas();
        int maiorNumero = porColuna ? colunas : linhas * colunas;
        int largura = String.valueOf(maiorNumero).length();
        String separador = "+" + ("-".repeat(largura + 2) + "+").repeat(colunas);

        System.out.println();
        if (porColuna) {
            System.out.println(cabecalhoDasColunas(colunas, largura));
        }
        System.out.println(separador);
        for (int l = 0; l < linhas; l++) {
            StringBuilder linha = new StringBuilder("|");
            for (int c = 0; c < colunas; c++) {
                int numero = l * colunas + c + 1;
                linha.append(textoDaCasa(tabuleiro, new Posicao(l, c), numero, largura, porColuna))
                        .append("|");
            }
            System.out.println(linha);
            System.out.println(separador);
        }
    }

    private String cabecalhoDasColunas(int colunas, int largura) {
        StringBuilder cabecalho = new StringBuilder(" ");
        for (int c = 1; c <= colunas; c++) {
            cabecalho.append(" ").append(String.format("%" + largura + "s", c)).append("  ");
        }
        return cabecalho.toString();
    }

    private String textoDaCasa(Tabuleiro tabuleiro, Posicao posicao, int numero,
                               int largura, boolean porColuna) {
        Peca peca = pecaEm(tabuleiro, posicao);
        String formato = "%" + largura + "s";
        if (peca != null && peca.estaVirada()) {
            return "[" + String.format(formato, peca.getSimbolo()) + "]";
        }
        if (porColuna) {
            return " ".repeat(largura + 2);
        }
        return " " + String.format(formato, numero) + " ";
    }

    private Peca pecaEm(Tabuleiro tabuleiro, Posicao posicao) {
        try {
            return tabuleiro.obterPeca(posicao);
        } catch (PosicaoInvalida e) {
            return null; // nunca acontece: so desenhamos posicoes do proprio tabuleiro
        }
    }

    private String lerLinha() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Entrada encerrada. Ate a proxima!");
            System.exit(0);
            return "";
        }
    }
}