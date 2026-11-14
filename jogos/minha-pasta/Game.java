import java.util.List;

public class Game {
    private final List<Player> players;
    private int currentPlayerIndex = 0;
    private Player winner;

    public Game(List<Player> players) {
        this.players = players;
    }

    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public Player getOpponentPlayer() { return players.get((currentPlayerIndex + 1) % players.size()); }

