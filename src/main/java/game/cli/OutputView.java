package game.cli;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;
import game.core.entity.TeamSide;

import java.util.List;

public final class OutputView {

    public void printEmptyBoard(Board board) {
        System.out.println();
        for (int y = 0; y < board.height(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < board.width(); x++) row.append(". ");
            System.out.println(row);
        }
        System.out.println();
    }

    public void printBoard(Board board, List<Unit> units) {
        System.out.println();
        for (int y = 0; y < board.height(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < board.width(); x++) {
                Unit c = findAt(units, Position.of(x, y));
                if (c == null) {
                    row.append(". ");
                } else {
                    char symbol = c.role().name().charAt(0); // K/R/A/M/M/T...
                    if (c.side() == TeamSide.ENEMY) symbol = Character.toLowerCase(symbol);
                    row.append(symbol).append(' ');
                }
            }
            System.out.println(row);
        }
        System.out.println();
    }

    private Unit findAt(List<Unit> chars, Position p) {
        for (Unit c : chars) {
            if (c.position().equals(p) && !c.isDead()) return c;
        }
        return null;
    }
}