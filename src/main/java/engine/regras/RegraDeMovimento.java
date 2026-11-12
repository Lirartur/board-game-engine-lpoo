package engine.regras;

import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Posicao;
import engine.modelo.Tabuleiro;

public interface RegraDeMovimento {

    /**
     * faz a jogada no tabuleiro.
     * ficatrue se o jogador mantem a vez, false se a vez passa.
     */
    boolean executar(Tabuleiro tabuleiro, Jogador jogador, Posicao posicao)
            throws PosicaoInvalida, JogadaInvalida;

    default boolean usarNumeroDaColuna() {
        return false;
    }
}