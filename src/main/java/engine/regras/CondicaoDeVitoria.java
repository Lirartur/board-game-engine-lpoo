package engine.regras;

import engine.modelo.Jogador;
import engine.modelo.Resultado;
import engine.modelo.Tabuleiro;

import java.util.List;

public interface CondicaoDeVitoria {

    Resultado verificar(Tabuleiro tabuleiro, List<Jogador> jogadores, Jogador ultimoJogador);
}