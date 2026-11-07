package engine.regras;

import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Peca;
import engine.modelo.Posicao;
import engine.modelo.Tabuleiro;

public class PosicionamentoLivre implements RegraDeMovimento {

    @Override
    public boolean executar(Tabuleiro tabuleiro, Jogador jogador, Posicao posicao)
            throws PosicaoInvalida, JogadaInvalida {
        tabuleiro.colocarPeca(posicao, new Peca(jogador.getSimbolo()));
        return false;
    }
}