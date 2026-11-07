package engine.modelo;

public class Peca {

    private final String simbolo;
    private boolean virada;

    public Peca(String simbolo) {
        this(simbolo, true);
    }

    public Peca(String simbolo, boolean virada) {
        this.simbolo = simbolo;
        this.virada = virada;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public boolean estaVirada() {
        return virada;
    }

    public void setVirada(boolean virada) {
        this.virada = virada;
    }

    @Override
    public String toString() {
        return simbolo;
    }
}