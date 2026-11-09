package engine.configuracao;

import engine.configuracao.ConfiguracaoDoJogo.ConfigVitoria;
import engine.excecoes.Excecoes.ConfiguracaoInvalida;
import engine.regras.CondicaoDeVitoria;
import engine.regras.PosicionamentoLivre;
import engine.regras.PreparacaoDoTabuleiro;
import engine.regras.QuedaPorColuna;
import engine.regras.RegraDeMovimento;
import engine.regras.TabuleiroVazio;
import engine.regras.VitoriaPorAlinhamento;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class FabricaDeRegras {

    @FunctionalInterface
    private interface CriadorDeVitoria {
        CondicaoDeVitoria criar(ConfigVitoria config, int linhas, int colunas)
                throws ConfiguracaoInvalida;
    }

    private final Map<String, Supplier<PreparacaoDoTabuleiro>> preparacoes = new TreeMap<>();
    private final Map<String, Supplier<RegraDeMovimento>> movimentos = new TreeMap<>();
    private final Map<String, CriadorDeVitoria> vitorias = new TreeMap<>();

    public FabricaDeRegras() {
        preparacoes.put("TABULEIRO_VAZIO", TabuleiroVazio::new);

        movimentos.put("POSICIONAMENTO_LIVRE", PosicionamentoLivre::new);
        movimentos.put("QUEDA_POR_COLUNA", QuedaPorColuna::new);

        vitorias.put("ALINHAMENTO", FabricaDeRegras::criarAlinhamento);
    }

    public PreparacaoDoTabuleiro criarPreparacao(String nome) throws ConfiguracaoInvalida {
        return buscar(preparacoes, nome, "Preparacao do tabuleiro").get();
    }

    public RegraDeMovimento criarMovimento(String nome) throws ConfiguracaoInvalida {
        return buscar(movimentos, nome, "Regra de movimento").get();
    }

    public CondicaoDeVitoria criarVitoria(ConfigVitoria config, int linhas, int colunas)
            throws ConfiguracaoInvalida {
        if (config.getTipo() == null || config.getTipo().isBlank()) {
            throw new ConfiguracaoInvalida("Campo obrigatorio ausente: regras.vitoria.tipo.");
        }
        return buscar(vitorias, config.getTipo(), "Regra de vitoria")
                .criar(config, linhas, colunas);
    }

    private <T> T buscar(Map<String, T> catalogo, String nome, String tipoDeRegra)
            throws ConfiguracaoInvalida {
        T item = catalogo.get(nome.trim().toUpperCase(Locale.ROOT));
        if (item == null) {
            throw new ConfiguracaoInvalida(tipoDeRegra + " '" + nome + "' nao reconhecida. "
                    + "Disponiveis: " + String.join(", ", catalogo.keySet()) + ".");
        }
        return item;
    }

    private static CondicaoDeVitoria criarAlinhamento(ConfigVitoria config, int linhas, int colunas)
            throws ConfiguracaoInvalida {
        Integer quantidade = config.getQuantidade();
        if (quantidade == null || quantidade < 2) {
            throw new ConfiguracaoInvalida(
                    "Vitoria por alinhamento exige 'quantidade' de pelo menos 2.");
        }
        if (quantidade > Math.max(linhas, colunas)) {
            throw new ConfiguracaoInvalida("Nao e possivel alinhar " + quantidade
                    + " pecas em um tabuleiro " + linhas + "x" + colunas + ".");
        }
        return new VitoriaPorAlinhamento(quantidade);
    }
}