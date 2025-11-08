package game.app;

import game.cli.OutputView;
import game.core.board.Board;

public class GameApplication {
    public static void main(String[] args) {
        Board board = new Board(5, 4);
        new OutputView().printEmptyBoard(board);
    }
}
