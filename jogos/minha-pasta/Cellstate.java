public enum CellState {
    WATER('~'),
    SHIP('N'),
    HIT('X'),
    MISS('O');

    private final char symbol;

    CellState(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
