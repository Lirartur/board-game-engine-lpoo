package engine.modelo;

import engine.excecoes.Excecoes.ConfiguracaoInvalida;
import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;

public class Tabuleiro {

    private final int linhas;
    private final int colunas;
    private final Peca[][] grade;

    public Tabuleiro(int linhas, int colunas) throws ConfiguracaoInvalida {
        if (linhas <= 0) {
            throw new ConfiguracaoInvalida(
                    "Tabuleiro invalido: linhas deve ser maior que zero.");
        }
        if (colunas <= 0) {
            throw new ConfiguracaoInvalida(
                    "Tabuleiro invalido: colunas deve ser maior que zero.");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        this.grade = new Peca[linhas][colunas];
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public boolean estaDentro(Posicao posicao) {
        return posicao.linha() >= 0 && posicao.linha() < linhas
                && posicao.coluna() >= 0 && posicao.coluna() < colunas;
    }

    private void verificarDentro(Posicao posicao) throws PosicaoInvalida {
        if (!estaDentro(posicao)) {
            throw new PosicaoInvalida("Posicao fora do tabuleiro.");
        }
    }

    public boolean estaVazia(Posicao posicao) throws PosicaoInvalida {
        verificarDentro(posicao);
        return grade[posicao.linha()][posicao.coluna()] == null;
    }

    public Peca obterPeca(Posicao posicao) throws PosicaoInvalida {
        verificarDentro(posicao);
        return grade[posicao.linha()][posicao.coluna()];
    }

    public void colocarPeca(Posicao posicao, Peca peca)
            throws PosicaoInvalida, JogadaInvalida {
        verificarDentro(posicao);
        if (grade[posicao.linha()][posicao.coluna()] != null) {
            throw new JogadaInvalida("Posicao ja ocupada.");
        }
        grade[posicao.linha()][posicao.coluna()] = peca;
    }

    public void removerPeca(Posicao posicao) throws PosicaoInvalida {
        verificarDentro(posicao);
        grade[posicao.linha()][posicao.coluna()] = null;
    }

    public boolean estaCheio() {
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                if (grade[l][c] == null) {
                    return false;
                }
            }
        }
        return true;
    }
}