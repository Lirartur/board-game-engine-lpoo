package engine.regras;

import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Peca;
import engine.modelo.Posicao;
import engine.modelo.Resultado;
import engine.modelo.Tabuleiro;

import java.util.List;

public class VitoriaPorAlinhamento implements CondicaoDeVitoria {

    private static final int[][] DIRECOES = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    private final int quantidade;

    public VitoriaPorAlinhamento(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public Resultado verificar(Tabuleiro tabuleiro, List<Jogador> jogadores, Jogador ultimoJogador) {
        if (temAlinhamento(tabuleiro, ultimoJogador.getSimbolo())) {
            return Resultado.vitoria(ultimoJogador);
        }
        if (tabuleiro.estaCheio()) {
            return Resultado.empate();
        }
        return Resultado.emAndamento();
    }

    private boolean temAlinhamento(Tabuleiro tabuleiro, String simbolo) {
        for (int l = 0; l < tabuleiro.getLinhas(); l++) {
            for (int c = 0; c < tabuleiro.getColunas(); c++) {
                for (int[] direcao : DIRECOES) {
                    if (contarSeguidas(tabuleiro, l, c, direcao[0], direcao[1], simbolo) >= quantidade) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int contarSeguidas(Tabuleiro tabuleiro, int linha, int coluna,
                               int passoLinha, int passoColuna, String simbolo) {
        int total = 0;
        while (simbolo.equals(simboloEm(tabuleiro, linha, coluna))) {
            total++;
            linha += passoLinha;
            coluna += passoColuna;
        }
        return total;
    }

    private String simboloEm(Tabuleiro tabuleiro, int linha, int coluna) {
        Posicao posicao = new Posicao(linha, coluna);
        if (!tabuleiro.estaDentro(posicao)) {
            return null;
        }
        try {
            Peca peca = tabuleiro.obterPeca(posicao);
            return peca == null ? null : peca.getSimbolo();
        } catch (PosicaoInvalida e) {
            return null; // nunca acontece: a posicao ja foi verificada acima
        }
    }
}