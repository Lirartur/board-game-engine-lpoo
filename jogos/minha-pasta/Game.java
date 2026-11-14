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
    
    public String executeAttack(Position target) throws InvalidPositionException, InvalidMoveException {
        if (isFinished()) throw new InvalidMoveException("Partida encerrada.");
        Player opponent = getOpponentPlayer();
        String result = opponent.getBoard().receiveAttack(target);
        if (opponent.getBoard().allShipsSunk()) {
            winner = getCurrentPlayer();
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        return result;
    }

    public boolean isFinished() { return winner != null; }
    public Player getWinner() { return winner; }
}

