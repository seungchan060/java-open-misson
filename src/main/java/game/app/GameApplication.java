package game.app;

import game.cli.InputView;
import game.cli.OutputView;
import game.core.board.*;
import game.core.entity.*;
import game.core.service.*;

import java.util.ArrayList;
import java.util.List;

public class GameApplication {
    public static void main(String[] args) {
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

        OutputView view = new OutputView();
        InputView input = new InputView();
        MovementService movement = new MovementService();
        AttackService attack = new AttackService();

        // 간단한 플레이어 턴 루프
        while (true) {
            view.printBoard(board, units);

            String sel = input.input("행동할 아군 유닛 좌표 (예: 2,3) 또는 exit:");
            if (sel.equalsIgnoreCase("exit")) break;

            Position pos;
            try { pos = Position.parse(sel); }
            catch (Exception e) { System.out.println("좌표 형식 오류"); continue; }

            Unit me = findUnitAt(units, pos, TeamSide.PLAYER);
            if (me == null) { System.out.println("해당 위치에 아군 유닛이 없습니다."); continue; }
            if (me.isDead()) { System.out.println("사망한 유닛입니다."); continue; }

            String act = input.input("행동 (move / atk):");
            if (act.equalsIgnoreCase("move")) {
                String dir = input.input("방향 (W/A/S/D):");
                Direction d = switch (dir.toUpperCase()) {
                    case "W" -> Direction.UP;
                    case "S" -> Direction.DOWN;
                    case "A" -> Direction.LEFT;
                    case "D" -> Direction.RIGHT;
                    default -> null;
                };
                if (d == null) { System.out.println("방향 입력 오류"); continue; }
                movement.move(board, units, me, d);
            } else if (act.equalsIgnoreCase("atk")) {
                String tgt = input.input("공격 대상 좌표 (예: 2,1):");
                Position tp;
                try { tp = Position.parse(tgt); }
                catch (Exception e) { System.out.println("좌표 형식 오류"); continue; }

                Unit enemy = findUnitAt(units, tp, TeamSide.ENEMY);
                if (enemy == null) { System.out.println("해당 위치에 적이 없습니다."); continue; }

                // 사거리 1칸
                if (me.position().manhattanDistance(enemy.position()) > 1) {
                    System.out.println("사거리 밖!");
                    continue;
                }
                attack.basicAttack(me, enemy);
            } else {
                System.out.println("알 수 없는 행동");
            }
        }
    }

    private static Unit findUnitAt(List<Unit> units, Position p, TeamSide side) {
        for (Unit u : units) {
            if (!u.isDead() && u.side() == side && u.position().equals(p)) return u;
        }
        return null;
    }
}