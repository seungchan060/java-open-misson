package game.core.board;

public class Position {
    private final int x; // column
    private final int y; // row

    private Position(int x, int y) {
        this.x = x; this.y = y;
    }

    public static Position of(int x, int y) { return new Position(x, y); }

    public int x() { return x; }
    public int y() { return y; }
} 
