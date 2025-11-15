package game.core.service;

import game.core.board.Board;
import game.core.board.Direction;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public final class MovementService {
    public boolean move(Board board, List<Unit> units, Unit unit, Direction direction) {

        Position cur = unit.position();
        Position next = Position.of(
                cur.x() + direction.dx,
                cur.y() + direction.dy
        );

        if (!board.isInside(next)) {
            System.out.println("보드 밖으로 이동할 수 없습니다.");
            return false;
        }

        for (Unit u : units) {
            if (!u.isDead() && u.position().equals(next)) {
                System.out.println("해당 칸은 이미 다른 유닛이 차지하고 있습니다.");
                return false;
            }
        }

        unit.moveTo(next);
        return true;
    }
}