package game.core.board;

import java.util.Objects;

public class Position {
    private final int x; // column
    private final int y; // row

    private Position(int x, int y) {
        this.x = x; this.y = y;
    }

    public static Position of(int x, int y) { return new Position(x, y); }

    public int x() { return x; }
    public int y() { return y; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return x == p.x && y == p.y;
    }
    @Override public int hashCode() { return Objects.hash(x, y); }
    @Override public String toString() { return "(" + x + "," + y + ")"; }
}
