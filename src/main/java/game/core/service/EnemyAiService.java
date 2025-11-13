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
    private int enemyTurnIndex = 0;

    public EnemyAiService(MovementService movement, AttackService attack) {
        this.movement = movement;
        this.attack = attack;
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
        // 도발 대상 우선
        Optional<Unit> tauntTarget = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER && u.isTaunting())
                .findFirst();

        if (tauntTarget.isPresent()) {
            Unit t = tauntTarget.get();
            if (enemy.position().manhattanDistance(t.position()) == 1) {
                attack.basicAttack(enemy, t);
            } else {
                moveOneStepToward(board, all, enemy, t.position());
            }
            return;
        }

        // 일반 로직: 은신 제외, 인접 우선
        Optional<Unit> adjacent = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER && !u.isStealthed())
                .filter(u -> enemy.position().manhattanDistance(u.position()) == 1)
                .min(Comparator.comparingInt(u -> u.stats().hp()));

        if (adjacent.isPresent()) {
            attack.basicAttack(enemy, adjacent.get());
            return;
        }

        // 가장 가까운 비은신 플레이어에게 접근
        Optional<Unit> nearest = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER && !u.isStealthed())
                .min(Comparator.comparingInt(u -> enemy.position().manhattanDistance(u.position())));
        if (nearest.isEmpty()) return;

        moveOneStepToward(board, all, enemy, nearest.get().position());
    }

    // 목표를 향해 한 칸 이동 직진이 막히면 수직/수평 우회도 시도
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
        int dx = Integer.compare(to.x(), from.x()); // -1,0,1
        int dy = Integer.compare(to.y(), from.y()); // -1,0,1

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
                    backX            // 마지막 수단(멀어지지만 막힘 해소용)
            };
        }
        // 같은 열
        if (dx == 0 && goY != null) {
            return new Direction[] {
                    goY,             // 직진
                    Direction.LEFT,  // 우회
                    Direction.RIGHT, // 우회
                    backY            // 마지막 수단
            };
        }
        // 대각선 관계: 주축 -> 보조축 -> 보조축 반대 -> 주축 반대
        return new Direction[] {
                goX, goY,
                backY, backX
        };
    }
}