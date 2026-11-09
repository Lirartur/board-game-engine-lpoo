package engine.configuracao;

import java.util.List;

public class ConfiguracaoDoJogo {

    private String nome;
    private ConfigTabuleiro tabuleiro;
    private ConfigJogadores jogadores;
    private ConfigRegras regras;

    public String getNome() {
        return nome;
    }

    public ConfigTabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public ConfigJogadores getJogadores() {
        return jogadores;
    }

    public ConfigRegras getRegras() {
        return regras;
    }

    public static class ConfigTabuleiro {
        private Integer linhas;
        private Integer colunas;

        public Integer getLinhas() {
            return linhas;
        }

        public Integer getColunas() {
            return colunas;
        }
    }

    public static class ConfigJogadores {
        private Integer quantidade;
        private List<String> simbolosSugeridos;

        public Integer getQuantidade() {
            return quantidade;
        }

        public List<String> getSimbolosSugeridos() {
            return simbolosSugeridos == null ? List.of() : simbolosSugeridos;
        }
    }

    public static class ConfigRegras {
        private String preparacao;
        private String movimento;
        private ConfigVitoria vitoria;

        public String getPreparacao() {
            return (preparacao == null || preparacao.isBlank()) ? "TABULEIRO_VAZIO" : preparacao;
        }

        public String getMovimento() {
            return movimento;
        }

        public ConfigVitoria getVitoria() {
            return vitoria;
        }
    }

    public static class ConfigVitoria {
        private String tipo;
        private Integer quantidade;

        public String getTipo() {
            return tipo;
        }

        public Integer getQuantidade() {
            return quantidade;
        }
    }
}