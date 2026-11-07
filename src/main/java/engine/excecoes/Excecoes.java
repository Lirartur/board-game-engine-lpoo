package engine.excecoes;

public class Excecoes {

    private Excecoes() {
    }

    public static class JogadaInvalida extends Exception {
        public JogadaInvalida(String mensagem) {
            super(mensagem);
        }
    }

    public static class PosicaoInvalida extends Exception {
        public PosicaoInvalida(String mensagem) {
            super(mensagem);
        }
    }

    public static class ConfiguracaoInvalida extends Exception {
        public ConfiguracaoInvalida(String mensagem) {
            super(mensagem);
        }
    }
}