public class Player {
    private final String name;
    private final Board board;

    public Player(String name, int rows, int cols) {
        this.name = name;
        this.board = new Board(rows, cols);
    }
