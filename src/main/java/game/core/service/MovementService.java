package game.core.service;

import game.core.board.Board;
import game.core.board.Direction;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public final class MovementService {

    public void move(Board board, List<Unit> units, Unit unit, Direction direction) {
        Position newPos = Position.of(unit.position().x() + direction.dx,
                unit.position().y() + direction.dy);

        // 경계 체크
        if (!board.isInside(newPos)) {
            System.out.println("보드 밖으로 이동할 수 없습니다.");
            return;
        }

        // 이미 다른 유닛이 해당 칸에 있는지 체크
        boolean occupied = units.stream()
                .anyMatch(u -> !u.isDead() && u.position().equals(newPos));
        if (occupied) {
            System.out.println("해당 위치는 이미 다른 유닛이 차지하고 있습니다.");
            return;
        }

        unit.moveTo(newPos);
    }
}