package engine.regras;

import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Tabuleiro;

public interface PreparacaoDoTabuleiro {

    void preparar(Tabuleiro tabuleiro) throws PosicaoInvalida, JogadaInvalida;
}