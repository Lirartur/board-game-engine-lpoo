package engine.modelo;

import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.regras.CondicaoDeVitoria;
import engine.regras.PreparacaoDoTabuleiro;
import engine.regras.RegraDeMovimento;

import java.util.List;

public class Jogo {

    private final String nome;
    private final Tabuleiro tabuleiro;
    private final List<Jogador> jogadores;
    private final RegraDeMovimento regraDeMovimento;
    private final CondicaoDeVitoria condicaoDeVitoria;

    private int indiceDaVez;
    private Resultado resultado;

    public Jogo(String nome, Tabuleiro tabuleiro, List<Jogador> jogadores,
                PreparacaoDoTabuleiro preparacao, RegraDeMovimento regraDeMovimento,
                CondicaoDeVitoria condicaoDeVitoria)
            throws PosicaoInvalida, JogadaInvalida {
        this.nome = nome;
        this.tabuleiro = tabuleiro;
        this.jogadores = List.copyOf(jogadores);
        this.regraDeMovimento = regraDeMovimento;
        this.condicaoDeVitoria = condicaoDeVitoria;
        this.indiceDaVez = 0;
        this.resultado = Resultado.emAndamento();
        preparacao.preparar(tabuleiro);
    }

    public String getNome() {
        return nome;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public Jogador getJogadorDaVez() {
        return jogadores.get(indiceDaVez);
    }

    public Resultado getResultado() {
        return resultado;
    }

    public boolean terminou() {
        return resultado.estado() != EstadoDoJogo.EM_ANDAMENTO;
    }

    public void jogar(Posicao posicao) throws PosicaoInvalida, JogadaInvalida {
        if (terminou()) {
            throw new JogadaInvalida("A partida ja terminou.");
        }
        Jogador jogador = getJogadorDaVez();
        boolean mantemAVez = regraDeMovimento.executar(tabuleiro, jogador, posicao);
        resultado = condicaoDeVitoria.verificar(tabuleiro, jogadores, jogador);
        if (!terminou() && !mantemAVez) {
            indiceDaVez = (indiceDaVez + 1) % jogadores.size();
        }
    }
}