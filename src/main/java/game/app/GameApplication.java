package game.app;

import game.cli.InputView;
import game.cli.OutputView;
import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import game.core.engine.BattleEngine;

import java.util.ArrayList;
import java.util.List;

public class GameApplication {
    public static void main(String[] args) {
        System.out.println("Tactics Sim 시작!");

        Board board = new Board(5, 4);
        List<Unit> units = new ArrayList<>();

        // 샘플 배치
        units.add(new Unit("그라가스", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(90, 12, 4, 10, 0), Position.of(2, 3)));
        units.add(new Unit("애쉬", Role.ARCHER, TeamSide.PLAYER,
                Stats.of(65, 13, 2, 15, 0), Position.of(3, 3)));
        units.add(new Unit("피들스틱", Role.MAGE, TeamSide.ENEMY,
                Stats.of(55, 14, 1, 5, 60), Position.of(2, 0)));
        units.add(new Unit("고블린", Role.ROGUE, TeamSide.ENEMY,
                Stats.of(70, 11, 2, 20, 0), Position.of(1, 1)));

        new BattleEngine(board, units, new OutputView(), new InputView()).run();
    }
}