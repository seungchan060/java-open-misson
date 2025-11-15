package game.core.service;

import game.core.board.Board;
import game.core.board.Direction;
import game.core.board.Position;
import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class EnemyAiService {

    private final MovementService movement;
    private final AttackService attack;
    private final ThreatPolicy threat = new ThreatPolicy();

    private int enemyTurnIndex = 0;
    private Unit focusTarget = null;

    public EnemyAiService(MovementService movement, AttackService attack) {
        this.movement = movement;
        this.attack = attack;
    }

    public void beginEnemyPhase() {
        focusTarget = null;
    }

    public void takeTurn(Board board, List<Unit> all) {
        var enemies = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.ENEMY)
                .sorted(Comparator.comparing(u -> u.name()))
                .toList();
        if (enemies.isEmpty()) return;

        enemyTurnIndex = enemyTurnIndex % enemies.size();
        Unit enemy = enemies.get(enemyTurnIndex);
        enemyTurnIndex++;

        actSingleEnemy(board, all, enemy);
    }

    private void actSingleEnemy(Board board, List<Unit> all, Unit enemy) {
        // 도발 대상이 하나라도 있으면 그 캐릭터가 무조건 타겟
        Optional<Unit> tauntTarget = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER && u.isTaunting())
                .findFirst();

        Unit target;
        if (tauntTarget.isPresent()) {
            target = tauntTarget.get();
            focusTarget = target;
        } else {
            // 기존 포커스 타겟이 여전히 유효하면 유지
            if (isValidFocus(focusTarget, all)) {
                target = focusTarget;
            } else {
                // 새 타겟 선정 (스텔스 제외, 힐러/딜러 우선)
                var pool = threat.candidates(all);
                target = threat.bestTarget(enemy, pool);
                focusTarget = target;
            }
        }

        if (target == null) {
            return;
        }

        int dist = enemy.position().manhattanDistance(target.position());

        // 타겟과 인접하면 → 공격 (blocker는 무시)
        if (dist == 1) {
            attack.basicAttack(enemy, target);
            if (target.isDead()) {
                focusTarget = null;
            }
            return;
        }

        // 인접이 아니면 → 타겟 방향으로 한 칸 이동 (막히면 우회)
        moveOneStepToward(board, all, enemy, target.position());
    }

    private boolean isValidFocus(Unit t, List<Unit> all) {
        if (t == null || t.isDead()) return false;
        if (t.side() != TeamSide.PLAYER) return false;

        // 도발 중이면 스텔스 여부 상관없이 타겟 가능
        if (t.isTaunting()) return true;

        // 도발이 아니면 스텔스 타겟은 무효
        if (t.isStealthed()) return false;

        // 아직 전장에 존재하는지
        return all.stream().anyMatch(u -> u == t);
    }

    private void moveOneStepToward(Board board, List<Unit> all, Unit mover, Position to) {
        Position from = mover.position();

        for (Direction d : stepOrderWithDetour(from, to)) {
            if (d == null) continue;
            Position next = Position.of(from.x() + d.dx, from.y() + d.dy);

            boolean inside = board.isInside(next);
            boolean occupied = all.stream()
                    .anyMatch(u -> !u.isDead() && u.position().equals(next));

            if (inside && !occupied) {
                movement.move(board, all, mover, d);
                return;
            }
        }
    }

    private Direction[] stepOrderWithDetour(Position from, Position to) {
        int dx = Integer.compare(to.x(), from.x());
        int dy = Integer.compare(to.y(), from.y());

        Direction goX   = dx == 0 ? null : (dx > 0 ? Direction.RIGHT : Direction.LEFT);
        Direction backX = dx == 0 ? null : (dx > 0 ? Direction.LEFT  : Direction.RIGHT);

        Direction goY   = dy == 0 ? null : (dy > 0 ? Direction.DOWN  : Direction.UP);
        Direction backY = dy == 0 ? null : (dy > 0 ? Direction.UP    : Direction.DOWN);

        if (dy == 0 && goX != null) {
            return new Direction[] { goX, Direction.UP, Direction.DOWN, backX };
        }
        if (dx == 0 && goY != null) {
            return new Direction[] { goY, Direction.LEFT, Direction.RIGHT, backY };
        }
        return new Direction[] { goX, goY, backY, backX };
    }
}