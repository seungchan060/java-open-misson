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
                .toList();
        if (enemies.isEmpty()) return;

        enemyTurnIndex = enemyTurnIndex % enemies.size();
        Unit enemy = enemies.get(enemyTurnIndex);
        enemyTurnIndex++;

        actSingleEnemy(board, all, enemy);
    }

    private void actSingleEnemy(Board board, List<Unit> all, Unit enemy) {
        Optional<Unit> tauntTarget = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER && u.isTaunting())
                .findFirst();

        Unit target;
        if (tauntTarget.isPresent()) {
            target = tauntTarget.get();
            focusTarget = target;
        } else {
            if (isValidFocus(all, focusTarget)) {
                target = focusTarget;
            } else {
                var pool = threat.candidates(all);
                target = threat.bestTarget(enemy, pool);
                focusTarget = target;
            }
        }
        if (target == null) return;

        int d = enemy.position().manhattanDistance(target.position());
        if (d == 1) {
            attack.basicAttack(enemy, target);
            if (target.isDead()) focusTarget = null;
            return;
        }

        moveOneStepToward(board, all, enemy, target.position());
    }

    private boolean isValidFocus(List<Unit> all, Unit t) {
        if (t == null || t.isDead()) return false;
        if (t.isStealthed() && !t.isTaunting()) return false;
        return all.stream().anyMatch(u -> u == t);
    }

    private void moveOneStepToward(Board board, List<Unit> all, Unit mover, Position to) {
        Position from = mover.position();
        for (Direction d : stepOrderWithDetour(from, to)) {
            if (d == null) continue;
            Position next = Position.of(from.x() + d.dx, from.y() + d.dy);
            boolean inside = board.isInside(next);
            boolean occupied = all.stream().anyMatch(u -> !u.isDead() && u.position().equals(next));
            if (inside && !occupied) {
                movement.move(board, all, mover, d);
                return;
            }
        }
    }

    private Direction[] stepOrderWithDetour(Position from, Position to) {
        int dx = Integer.compare(to.x(), from.x());
        int dy = Integer.compare(to.y(), from.y());

        Direction goX = dx == 0 ? null : (dx > 0 ? Direction.RIGHT : Direction.LEFT);
        Direction backX = dx == 0 ? null : (dx > 0 ? Direction.LEFT : Direction.RIGHT);

        Direction goY = dy == 0 ? null : (dy > 0 ? Direction.DOWN : Direction.UP);
        Direction backY = dy == 0 ? null : (dy > 0 ? Direction.UP : Direction.DOWN);

        // 같은 행
        if (dy == 0 && goX != null) {
            return new Direction[] {
                    goX,             // 직진
                    Direction.UP,    // 우회
                    Direction.DOWN,  // 우회
                    backX            // 마지막 수단
            };
        }

        if (dx == 0 && goY != null) {
            return new Direction[] {
                    goY,
                    Direction.LEFT,
                    Direction.RIGHT,
                    backY
            };
        }
        return new Direction[] { goX, goY, backY, backX };
    }
}