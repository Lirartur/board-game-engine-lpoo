package engine.terminal;

import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.EstadoDoJogo;
import engine.modelo.Jogador;
import engine.modelo.Jogo;
import engine.modelo.Peca;
import engine.modelo.Posicao;
import engine.modelo.Resultado;
import engine.modelo.Tabuleiro;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InterfaceDeTerminal {

    private final Scanner scanner;

    public InterfaceDeTerminal(Scanner scanner) {
        this.scanner = scanner;
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
        exibirCabecalho(jogo);
        while (!jogo.terminou()) {
            desenharTabuleiro(jogo.getTabuleiro());
            Jogador jogador = jogo.getJogadorDaVez();
            Posicao posicao = lerPosicao(jogador, jogo.getTabuleiro());
            try {
                jogo.jogar(posicao);
            } catch (PosicaoInvalida | JogadaInvalida e) {
                exibirErro(e.getMessage());
            }
        }
        desenharTabuleiro(jogo.getTabuleiro());
        exibirResultado(jogo.getResultado());
    }

    public void exibirErro(String mensagem) {
        System.out.println("Erro: " + mensagem);
    }

    private void exibirCabecalho(Jogo jogo) {
        System.out.println("Board game engine");
        System.out.println("Jogo: " + jogo.getNome());
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
                exibirErro("o simbolo tem que ter 1 caractere.");
            } else if (Character.isWhitespace(texto.codePointAt(0))) {
                exibirErro("O simbolo nao pode ser espaco.");
            } else if (usados.contains(texto)) {
                exibirErro("Esse simbolo ja ta sendo usado.");
            } else {
                return texto;
            }
        }
    }

    private Posicao lerPosicao(Jogador jogador, Tabuleiro tabuleiro) {
        while (true) {
            System.out.print(jogador + ", informe o numero da posicao: ");
            String texto = lerLinha().trim();
            try {
                int numero = Integer.parseInt(texto);
                if (numero < 1) {
                    exibirErro("digite um numero maior que zero.");
                    continue;
                }
                int colunas = tabuleiro.getColunas();
                return new Posicao((numero - 1) / colunas, (numero - 1) % colunas);
            } catch (NumberFormatException e) {
                exibirErro("digite apenas o numero da posicao.");
            }
        }
    }

    private void desenharTabuleiro(Tabuleiro tabuleiro) {
        int linhas = tabuleiro.getLinhas();
        int colunas = tabuleiro.getColunas();
        int largura = String.valueOf(linhas * colunas).length();
        String separador = "+" + ("-".repeat(largura + 2) + "+").repeat(colunas);

        System.out.println();
        System.out.println(separador);
        for (int l = 0; l < linhas; l++) {
            StringBuilder linha = new StringBuilder("|");
            for (int c = 0; c < colunas; c++) {
                int numero = l * colunas + c + 1;
                linha.append(textoDaCasa(tabuleiro, new Posicao(l, c), numero, largura)).append("|");
            }
            System.out.println(linha);
            System.out.println(separador);
        }
    }

    private String textoDaCasa(Tabuleiro tabuleiro, Posicao posicao, int numero, int largura) {
        Peca peca = pecaEm(tabuleiro, posicao);
        String formato = "%" + largura + "s";
        if (peca != null && peca.estaVirada()) {
            return "[" + String.format(formato, peca.getSimbolo()) + "]";
        }
        return " " + String.format(formato, numero) + " ";
    }

    private Peca pecaEm(Tabuleiro tabuleiro, Posicao posicao) {
        try {
            return tabuleiro.obterPeca(posicao);
        } catch (PosicaoInvalida e) {
            return null; // nn acontece so coisamos posicoes do proprio tabuleiro
        }
    }

    private String lerLinha() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("entrada encerrada. ate a proxima!");
            System.exit(0);
            return "";
        }
    }
}
