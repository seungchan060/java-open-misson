package game.cli;

import game.core.board.Board;

public final class OutputView {
    public void printEmptyBoard(Board board) {
        System.out.println();
        for (int y = 0; y < board.height(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < board.width(); x++) {
                row.append(". ");
            }
            System.out.println(row);
        }
        System.out.println();
    }
}
