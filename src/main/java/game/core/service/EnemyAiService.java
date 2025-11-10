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

    public EnemyAiService(MovementService movement, AttackService attack) {
        this.movement = movement; this.attack = attack;
    }

    public void takeTurn(Board board, List<Unit> all) {
        for (Unit enemy : all) {
            if (enemy.isDead() || enemy.side() != TeamSide.ENEMY) continue;

            // 인접한 플레이어 중 HP 낮은 대상 우선
            Optional<Unit> adj = all.stream()
                    .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER)
                    .filter(u -> enemy.position().manhattanDistance(u.position()) == 1)
                    .min(Comparator.comparingInt(u -> u.stats().hp()));

            if (adj.isPresent()) {
                attack.basicAttack(enemy, adj.get());
                continue;
            }

            // 가장 가까운 플레이어에게 접근
            Optional<Unit> nearest = all.stream()
                    .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER)
                    .min(Comparator.comparingInt(u -> enemy.position().manhattanDistance(u.position())));

            if (nearest.isEmpty()) return;

            // 한 칸 이동
            Position e = enemy.position();
            Position t = nearest.get().position();
            Direction[] order = stepOrder(e, t);
            for (Direction d : order) {
                if (d == null) continue;
                Position next = Position.of(enemy.position().x() + d.dx, enemy.position().y() + d.dy);
                if (board.isInside(next) && all.stream().noneMatch(u -> !u.isDead() && u.position().equals(next))) {
                    movement.move(board, all, enemy, d);
                    break;
                }
            }
        }
    }

    private Direction[] stepOrder(Position from, Position to) {
        int dx = Integer.compare(to.x(), from.x());
        int dy = Integer.compare(to.y(), from.y());

        Direction primaryX = dx == 0 ? null : (dx > 0 ? Direction.RIGHT : Direction.LEFT);
        Direction primaryY = dy == 0 ? null : (dy > 0 ? Direction.DOWN : Direction.UP);

        // 기본 이동 우선순위: 목표에 가까워지는 방향 → 못하면 다른 방향 → 그래도 못하면 제자리
        return new Direction[]{
                primaryX, primaryY,
                (dx != 0 ? (dy > 0 ? Direction.DOWN : Direction.UP)
                        : (dx > 0 ? Direction.RIGHT : Direction.LEFT)),
                (dy != 0 ? (dx > 0 ? Direction.RIGHT : Direction.LEFT)
                        : (dy > 0 ? Direction.DOWN : Direction.UP))
        };
    }
}