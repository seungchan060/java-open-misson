package game.app;

import game.cli.OutputView;
import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;

import java.util.List;

public class GameApplication {
    public static void main(String[] args) {
        Board board = new Board(5, 4);

        Unit p1 = new Unit("A", Role.KNIGHT, TeamSide.PLAYER, Stats.of(90, 12, 4, 10, 0), Position.of(2, 3));
        Unit p2 = new Unit("B", Role.ARCHER, TeamSide.PLAYER, Stats.of(65, 13, 2, 15, 0), Position.of(3, 3));
        Unit e1 = new Unit("C", Role.MAGE, TeamSide.ENEMY, Stats.of(55, 14, 1, 5, 60), Position.of(2, 0));
        Unit e2 = new Unit("D", Role.ROGUE, TeamSide.ENEMY, Stats.of(70, 11, 2, 20, 0), Position.of(1, 1));

        OutputView view = new OutputView();
        view.printBoard(board, List.of(p1, p2, e1, e2));
    }
}