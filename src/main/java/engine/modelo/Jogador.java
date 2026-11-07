package engine.modelo;

public class Jogador {

    private final String nome;
    private final String simbolo;
    private int pontuacao;

    public Jogador(String nome, String simbolo) {
        this.nome = nome;
        this.simbolo = simbolo;
        this.pontuacao = 0;
    }

    public String getNome() {
        return nome;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void adicionarPonto() {
        pontuacao++;
    }

    @Override
    public String toString() {
        return nome + " (" + simbolo + ")";
    }
}