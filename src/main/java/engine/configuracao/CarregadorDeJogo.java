package engine.configuracao;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import engine.excecoes.Excecoes.ConfiguracaoInvalida;
import engine.excecoes.Excecoes.JogadaInvalida;
import engine.excecoes.Excecoes.PosicaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Jogo;
import engine.modelo.Tabuleiro;
import engine.regras.CondicaoDeVitoria;
import engine.regras.PreparacaoDoTabuleiro;
import engine.regras.RegraDeMovimento;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CarregadorDeJogo {

    private record Componentes(Tabuleiro tabuleiro, PreparacaoDoTabuleiro preparacao,
                               RegraDeMovimento movimento, CondicaoDeVitoria vitoria) {
    }

    private final FabricaDeRegras fabrica;
    private final Gson gson = new Gson();

    public CarregadorDeJogo(FabricaDeRegras fabrica) {
        this.fabrica = fabrica;
    }

    public ConfiguracaoDoJogo ler(Path arquivo) throws ConfiguracaoInvalida {
        String nomeDoArquivo = arquivo.getFileName().toString();
        ConfiguracaoDoJogo configuracao;
        try (Reader leitor = Files.newBufferedReader(arquivo)) {
            configuracao = gson.fromJson(leitor, ConfiguracaoDoJogo.class);
        } catch (IOException e) {
            throw new ConfiguracaoInvalida("Nao foi possivel ler o arquivo '" + nomeDoArquivo + "'.");
        } catch (JsonParseException e) {
            throw new ConfiguracaoInvalida("O arquivo '" + nomeDoArquivo + "' nao e um JSON valido.");
        }
        if (configuracao == null) {
            throw new ConfiguracaoInvalida("O arquivo '" + nomeDoArquivo + "' esta vazio.");
        }
        validar(configuracao);
        return configuracao;
    }

    public Jogo montar(ConfiguracaoDoJogo configuracao, List<Jogador> jogadores)
            throws ConfiguracaoInvalida {
        Componentes componentes = criarComponentes(configuracao);
        try {
            return new Jogo(configuracao.getNome(), componentes.tabuleiro(), jogadores,
                    componentes.preparacao(), componentes.movimento(), componentes.vitoria());
        } catch (PosicaoInvalida | JogadaInvalida e) {
            throw new ConfiguracaoInvalida("Falha ao preparar o tabuleiro: " + e.getMessage());
        }
    }

    private void validar(ConfiguracaoDoJogo configuracao) throws ConfiguracaoInvalida {
        exigir(configuracao.getNome(), "nome");
        exigir(configuracao.getTabuleiro(), "tabuleiro");
        exigir(configuracao.getTabuleiro().getLinhas(), "tabuleiro.linhas");
        exigir(configuracao.getTabuleiro().getColunas(), "tabuleiro.colunas");
        exigir(configuracao.getJogadores(), "jogadores");
        exigir(configuracao.getJogadores().getQuantidade(), "jogadores.quantidade");
        exigir(configuracao.getRegras(), "regras");
        exigir(configuracao.getRegras().getMovimento(), "regras.movimento");
        exigir(configuracao.getRegras().getVitoria(), "regras.vitoria");

        if (configuracao.getJogadores().getQuantidade() < 2) {
            throw new ConfiguracaoInvalida("Quantidade de jogadores invalida: minimo 2.");
        }
        // Cria os componentes uma vez so para validar tabuleiro e regras
        // antes de perguntar os simbolos aos jogadores.
        criarComponentes(configuracao);
    }

    private Componentes criarComponentes(ConfiguracaoDoJogo configuracao)
            throws ConfiguracaoInvalida {
        int linhas = configuracao.getTabuleiro().getLinhas();
        int colunas = configuracao.getTabuleiro().getColunas();
        ConfiguracaoDoJogo.ConfigRegras regras = configuracao.getRegras();
        return new Componentes(
                new Tabuleiro(linhas, colunas),
                fabrica.criarPreparacao(regras.getPreparacao()),
                fabrica.criarMovimento(regras.getMovimento()),
                fabrica.criarVitoria(regras.getVitoria(), linhas, colunas));
    }

    private void exigir(Object valor, String campo) throws ConfiguracaoInvalida {
        boolean ausente = valor == null || (valor instanceof String texto && texto.isBlank());
        if (ausente) {
            throw new ConfiguracaoInvalida("Campo obrigatorio ausente: " + campo + ".");
        }
    }
}