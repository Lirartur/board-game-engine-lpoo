package engine;

import engine.configuracao.CarregadorDeJogo;
import engine.configuracao.ConfiguracaoDoJogo;
import engine.configuracao.FabricaDeRegras;
import engine.excecoes.Excecoes.ConfiguracaoInvalida;
import engine.modelo.Jogador;
import engine.modelo.Jogo;
import engine.terminal.InterfaceDeTerminal;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Path PASTA_DOS_JOGOS = Path.of("jogos");

    public static void main(String[] args) {
        InterfaceDeTerminal terminal = new InterfaceDeTerminal(new Scanner(System.in));
        CarregadorDeJogo carregador = new CarregadorDeJogo(new FabricaDeRegras());

        terminal.exibirBanner();
        try {
            Path arquivo = args.length > 0
                    ? Path.of(args[0])
                    : terminal.escolherArquivo(PASTA_DOS_JOGOS);
            ConfiguracaoDoJogo configuracao = carregador.ler(arquivo);
            List<Jogador> jogadores = terminal.criarJogadores(
                    configuracao.getJogadores().getQuantidade(),
                    configuracao.getJogadores().getSimbolosSugeridos());
            Jogo jogo = carregador.montar(configuracao, jogadores);
            terminal.executar(jogo);
        } catch (ConfiguracaoInvalida e) {
            terminal.exibirErroDeCarregamento(e.getMessage());
        }
    }
}