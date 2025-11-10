package game.core.service;

import game.core.board.Board;
import game.core.board.Direction;
import game.core.board.Position;
import game.core.entity.TeamSide;
import game.core.entity.Unit;

public final class EnemyAiService {
    private final MovementService movement;
    private final AttackService attack;

    public EnemyAiService(MovementService movement, AttackService attack) {
        this.movement = movement; this.attack = attack;
    }

    private Direction[] stepOrder(Position from, Position to) {
        int dx = Integer.compare(to.x(), from.x());
        int dy = Integer.compare(to.y(), from.y());
        Direction stepX = dx == 0 ? null : (dx > 0 ? Direction.RIGHT : Direction.LEFT);
        Direction stepY = dy == 0 ? null : (dy > 0 ? Direction.DOWN  : Direction.UP);
        return new Direction[]{ stepX, stepY };
    }
}