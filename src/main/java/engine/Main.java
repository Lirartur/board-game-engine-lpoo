package engine;

import engine.excecoes.Excecoes.ConfiguracaoInvalida;
import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Jogo;
import engine.modelo.Tabuleiro;
import engine.regras.PosicionamentoLivre;
import engine.regras.TabuleiroVazio;
import engine.regras.VitoriaPorAlinhamento;
import engine.terminal.InterfaceDeTerminal;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InterfaceDeTerminal terminal = new InterfaceDeTerminal(new Scanner(System.in));
        try {
            Tabuleiro tabuleiro = new Tabuleiro(3, 3);
            List<Jogador> jogadores = terminal.criarJogadores(2, List.of("X", "O"));
            Jogo jogo = new Jogo("jogo da velha", tabuleiro, jogadores,
                    new TabuleiroVazio(), new PosicionamentoLivre(), new VitoriaPorAlinhamento(3));
            terminal.executar(jogo);
        } catch (ConfiguracaoInvalida | PosicaoInvalida | JogadaInvalida e) {
            terminal.exibirErro("erro ao carregar o jogo: " + e.getMessage());
        }
    }
}