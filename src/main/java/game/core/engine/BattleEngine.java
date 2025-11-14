package game.core.engine;

import game.cli.InputView;
import game.cli.OutputView;
import game.core.board.Board;
import game.core.board.Direction;
import game.core.board.Position;
import game.core.bench.Bench;
import game.core.entity.TeamSide;
import game.core.entity.Unit;
import game.core.rules.FatigueRule;
import game.core.rules.VictoryRule;
import game.core.service.*;
import game.core.skill.Skill;

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

    private final Bench bench = new Bench(3);
    private final RecruitService recruit = new RecruitService();

    public BattleEngine(Board board, List<Unit> units, OutputView view, InputView input) {
        this.board = board;
        this.units = units;
        this.view = view;
        this.input = input;

        this.movement = new MovementService();
        this.attack = new AttackService();
        this.enemyAi = new EnemyAiService(movement, attack);
        this.victory = new VictoryRule(20); // 턴 한도
        this.fatigue = new FatigueRule();
    }

    public void run() {
        int turn = 1;

        while (true) {
            ContextUnitsHolder.open(units);

            view.printBoard(board, units);
            System.out.printf("=== Turn %d : PLAYER ===%n", turn);
            boolean keep = playerSingleAction();
            if (!keep) { ContextUnitsHolder.close(); break; }
            if (victory.isOver(turn, units)) { ContextUnitsHolder.close(); break; }

            System.out.printf("=== Turn %d : ENEMY ===%n", turn);
            enemyAi.beginEnemyPhase();
            enemyAi.takeTurn(board, units);
            if (victory.isOver(turn, units)) { ContextUnitsHolder.close(); break; }

            fatigue.applyEndOfTurn(turn, units);

            for (var u : units) {
                u.tickCooldown();
                u.tickStealth();
                u.tickTaunt();
                u.tickBodyBlock();
                u.tickValor();
                u.tickShield();
            }

            if (turn % 3 == 0) {
                recruitPhase();
            }

            ContextUnitsHolder.close();
            turn++;
        }

        view.printBoard(board, units);
        TeamSide w = victory.winner(Integer.MAX_VALUE, units);
        if (w == null)      System.out.println("무승부입니다.");
        else if (w == TeamSide.PLAYER) System.out.println("플레이어 승리!");
        else                 System.out.println("패배했습니다…");
    }

    private boolean playerSingleAction() {
        String sel = input.input("행동할 아군 유닛 좌표 (예: 2,3) 또는 exit:");
        if (sel.equalsIgnoreCase("exit")) return false;

        Position pos;
        try { pos = Position.parse(sel); }
        catch (Exception e) { System.out.println("좌표 형식 오류"); return true; }

        Unit me = findUnitAt(pos, TeamSide.PLAYER);
        if (me == null) { System.out.println("해당 위치에 아군 유닛이 없습니다."); return true; }
        if (me.isDead()) { System.out.println("사망한 유닛입니다."); return true; }

        String act = input.input("행동 (move / atk / skill):");
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
            movement.move(board, units, me, d);
            return true;
        }

        if (act.equalsIgnoreCase("atk")) {
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
            attack.basicAttack(me, enemy);
            return true;
        }

        if (act.equalsIgnoreCase("skill")) {
            var skills = me.role().skills();
            if (skills.isEmpty()) {
                System.out.println("이 역할은 사용 가능한 스킬이 없습니다.");
                return true;
            }
            System.out.println("사용할 스킬 번호를 선택하세요:");
            for (int i = 0; i < skills.size(); i++) {
                Skill s = skills.get(i);
                System.out.printf("%d) %s (MP:%d, CD:%d)%n", i + 1, s.name(), s.mpCost(), s.cooldown());
            }
            String no = input.input("번호:");
            int idx;
            try { idx = Integer.parseInt(no) - 1; }
            catch (Exception e) { System.out.println("번호 형식 오류"); return true; }
            if (idx < 0 || idx >= skills.size()) { System.out.println("범위를 벗어났습니다."); return true; }

            Skill sk = skills.get(idx);
            if (!me.isSkillReady(sk)) {
                System.out.println("스킬 사용 불가 (쿨다운 또는 MP 부족)");
                return true;
            }

            String tgt = input.input("스킬 대상 좌표 (예: x,y) / 자기대상 스킬은 Enter:");
            Position tp = null;
            if (tgt != null && !tgt.trim().isEmpty()) {
                try { tp = Position.parse(tgt); }
                catch (Exception e) { System.out.println("좌표 형식 오류"); return true; }
            }
            if (tp == null) tp = me.position();

            if (!sk.canUse(me, board, units, tp)) {
                System.out.println("스킬 사용 불가 (사거리/대상 조건)");
                return true;
            }

            if (!me.stats().consumeMana(sk.mpCost())) {
                System.out.println("MP가 부족합니다.");
                return true;
            }

            sk.use(me, board, units, tp);
            me.startSkillCooldown(sk);
            return true;
        }

        System.out.println("알 수 없는 행동");
        return true;
    }

    private Unit findUnitAt(Position p, TeamSide side) {
        for (Unit u : units) {
            if (!u.isDead() && u.side() == side && u.position().equals(p))
                return u;
        }
        return null;
    }

    private void recruitPhase() {
        System.out.println("새로운 유닛을 영입할 수 있습니다!");

        Unit rolled = recruit.roll();
        System.out.printf("획득 유닛: %s (%s) HP:%d ATK:%d%n",
                rolled.name(), rolled.role(), rolled.stats().hp(), rolled.stats().atk());

        if (!bench.add(rolled)) {
            System.out.println("벤치가 가득 찼습니다. 제거할 슬롯 번호 또는 skip:");
            viewBench();
            String sel = input.input("번호 또는 skip:");
            if (!sel.equalsIgnoreCase("skip")) {
                try {
                    int idx = Integer.parseInt(sel);
                    bench.remove(idx);
                    bench.add(rolled);
                } catch (Exception ignore) {}
            }
        }

        System.out.println("벤치:");
        viewBench();

        String place = input.input("바로 배치할까요? (y/n):");
        if (place.equalsIgnoreCase("y")) {
            String posInput = input.input("배치할 위치 (예: 2,3):");
            try {
                Position pos = Position.parse(posInput);
                boolean ok = recruit.placeOnBoard(board, units, rolled, pos);
                if (!ok) System.out.println("배치 실패: 위치가 잘못되었거나 차있거나 아군 라인이 아님");
            } catch (Exception e) {
                System.out.println("좌표 오류");
            }
        }
    }

    private void viewBench() {
        int i = 0;
        for (Unit u : bench.slots()) {
            System.out.printf("[%d] %s (%s) HP:%d ATK:%d%n",
                    i++, u.name(), u.role(), u.stats().hp(), u.stats().atk());
        }
    }
}