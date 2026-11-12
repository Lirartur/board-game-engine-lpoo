package engine.regras;

import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Posicao;
import engine.modelo.Tabuleiro;

public interface RegraDeMovimento {

    /**
     * Executa a jogada no tabuleiro.
     * Retorna true se o jogador mantem a vez, false se a vez passa.
     */
    boolean executar(Tabuleiro tabuleiro, Jogador jogador, Posicao posicao)
            throws PosicaoInvalida, JogadaInvalida;

    /**
     * Indica se o jogador escolhe apenas o numero da coluna (como no Connect 4)
     * em vez do numero da casa.
     */
    default boolean usaNumeroDaColuna() {
        return false;
    }
}