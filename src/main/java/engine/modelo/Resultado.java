package engine.modelo;

public record Resultado(EstadoDoJogo estado, Jogador vencedor) {

    public static Resultado emAndamento() {
        return new Resultado(EstadoDoJogo.EM_ANDAMENTO, null);
    }

    public static Resultado vitoria(Jogador vencedor) {
        return new Resultado(EstadoDoJogo.VITORIA, vencedor);
    }

    public static Resultado empate() {
        return new Resultado(EstadoDoJogo.EMPATE, null);
    }
}