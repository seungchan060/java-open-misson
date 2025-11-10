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

    // 한 턴에 적 1명만 행동하도록 라운드로빈 인덱스 유지
    private int enemyTurnIndex = 0;

    public EnemyAiService(MovementService movement, AttackService attack) {
        this.movement = movement;
        this.attack = attack;
    }

    // 적 턴
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

    // 인접해 있으면 공격, 아니면 목표에 가까워지는 방향으로 한 칸 이동(막히면 우회 시도)
    private void actSingleEnemy(Board board, List<Unit> all, Unit enemy) {
        // 인접한 플레이어 중 HP 가장 낮은 대상 공격
        Optional<Unit> adjacent = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER)
                .filter(u -> enemy.position().manhattanDistance(u.position()) == 1)
                .min(Comparator.comparingInt(u -> u.stats().hp()));

        if (adjacent.isPresent()) {
            attack.basicAttack(enemy, adjacent.get());
            return;
        }

        // 가장 가까운 플레이어에게 접근
        Optional<Unit> nearest = all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER)
                .min(Comparator.comparingInt(u -> enemy.position().manhattanDistance(u.position())));

        if (nearest.isEmpty()) return;

        Position from = enemy.position();
        Position to = nearest.get().position();
        Direction[] tryDirs = stepOrder(from, to);

        // 이동 성공할 수 있는 방향을 순서대로 시도
        for (Direction d : tryDirs) {
            if (d == null) continue;
            Position next = Position.of(from.x() + d.dx, from.y() + d.dy);
            boolean inside = board.isInside(next);
            boolean occupied = all.stream().anyMatch(u -> !u.isDead() && u.position().equals(next));
            if (inside && !occupied) {
                movement.move(board, all, enemy, d);
                break;
            }
        }
    }

    // 목표에 가까워지는 기본 X→Y 우선 + 우회 대안 방향까지 제공
    private Direction[] stepOrder(Position from, Position to) {
        int dx = Integer.compare(to.x(), from.x());
        int dy = Integer.compare(to.y(), from.y());

        Direction primaryX = dx == 0 ? null : (dx > 0 ? Direction.RIGHT : Direction.LEFT);
        Direction primaryY = dy == 0 ? null : (dy > 0 ? Direction.DOWN  : Direction.UP);

        // 대안: X 실패 시 Y, 그래도 실패하면 반대축 먼저/나중 조합 시도
        Direction alt1 = (primaryY != null) ? primaryY : (primaryX != null ? primaryX : null);
        Direction alt2 = (primaryX != null) ? primaryX : (primaryY != null ? primaryY : null);

        return new Direction[] { primaryX, primaryY, alt1, alt2 };
    }
}