import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final String name;
    private final int size;
    private final List<Position> positions = new ArrayList<>();
    private int hits = 0;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public void setPositions(List<Position> positions) {
        this.positions.clear();
        this.positions.addAll(positions);
    }

    public boolean occupies(Position pos) { return positions.contains(pos); }
    public void registerHit() { if (hits < size) hits++; }
    public boolean isSunk() { return hits >= size; }
    public String getName() { return name; }
    public int getSize() { return size; }
}
