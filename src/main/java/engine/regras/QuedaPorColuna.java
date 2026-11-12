package engine.regras;

import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Peca;
import engine.modelo.Posicao;
import engine.modelo.Tabuleiro;

public class QuedaPorColuna implements RegraDeMovimento {

    @Override
    public boolean executar(Tabuleiro tabuleiro, Jogador jogador, Posicao posicao)
            throws PosicaoInvalida, JogadaInvalida {
        if (posicao.linha() != 0) {
            throw new JogadaInvalida("Escolha uma coluna de 1 a " + tabuleiro.getColunas() + ".");
        }
        int coluna = posicao.coluna();
        for (int linha = tabuleiro.getLinhas() - 1; linha >= 0; linha--) {
            Posicao candidata = new Posicao(linha, coluna);
            if (tabuleiro.estaVazia(candidata)) {
                tabuleiro.colocarPeca(candidata, new Peca(jogador.getSimbolo()));
                return false;
            }
        }
        throw new JogadaInvalida("Coluna cheia.");
    }

    @Override
    public boolean usarNumeroDaColuna() {
        return true;
    }
}