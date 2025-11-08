package game.core.board;

public class Board {
    private final int width;   // columns (x: 0..width-1)
    private final int height;  // rows    (y: 0..height-1)

    public Board(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("보드 크기는 양수여야 합니다.");
        this.width = width; this.height = height;
    }

    public int width() { return width; }
    public int height() { return height; }

    public boolean isInside(Position p) {
        return p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
    }
}
