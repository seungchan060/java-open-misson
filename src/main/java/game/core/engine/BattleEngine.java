package game.core.engine;

import game.cli.InputView;
import game.cli.OutputView;
import game.core.board.*;
import game.core.entity.*;
import game.core.rules.FatigueRule;
import game.core.rules.VictoryRule;
import game.core.service.*;

import java.util.List;

public final class BattleEngine {
    private final Board board;
    private final List<Unit> units;
    private final OutputView view;
    private final InputView input;
    private final MovementService movement;
    private final AttackService attack;
    private final EnemyAiService enemyAi;
    private final VictoryRule victory;
    private final FatigueRule fatigue;

    public BattleEngine(Board board, List<Unit> units, OutputView view, InputView input) {
        this.board = board; this.units = units; this.view = view; this.input = input;
        this.movement = new MovementService();
        this.attack = new AttackService();
        this.enemyAi = new EnemyAiService(movement, attack);
        this.victory = new VictoryRule(20); // 턴 한도 20
        this.fatigue = new FatigueRule();
    }

    private boolean playerTurn() {
        String sel = input.input("행동할 아군 유닛 좌표 (예: 2,3) 또는 exit:");
        if (sel.equalsIgnoreCase("exit")) return false;

        Position pos;
        try { pos = Position.parse(sel); }
        catch (Exception e) { System.out.println("좌표 형식 오류"); return true; }

        Unit me = findUnitAt(pos, TeamSide.PLAYER);
        if (me == null) { System.out.println("해당 위치에 아군 유닛이 없습니다."); return true; }
        if (me.isDead()) { System.out.println("사망한 유닛입니다."); return true; }

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
            if (d == null) { System.out.println("방향 입력 오류"); return true; }
            new MovementService().move(board, units, me, d);
        } else if (act.equalsIgnoreCase("atk")) {
            String tgt = input.input("공격 대상 좌표 (예: 2,1):");
            Position tp;
            try { tp = Position.parse(tgt); }
            catch (Exception e) { System.out.println("좌표 형식 오류"); return true; }

            Unit enemy = findUnitAt(tp, TeamSide.ENEMY);
            if (enemy == null) { System.out.println("해당 위치에 적이 없습니다."); return true; }
            if (me.position().manhattanDistance(enemy.position()) > 1) {
                System.out.println("사거리 밖!");
                return true;
            }
            new AttackService().basicAttack(me, enemy);
        } else {
            System.out.println("알 수 없는 행동");
        }
        return true;
    }

    private Unit findUnitAt(Position p, TeamSide side) {
        for (Unit u : units) {
            if (!u.isDead() && u.side() == side && u.position().equals(p)) return u;
        }
        return null;
    }
}