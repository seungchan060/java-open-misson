package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;

import java.util.List;
import java.util.Random;

public final class RecruitService {
    private final Random random = new Random();

    public Unit roll() {
        Role[] roles = Role.values();
        Role r = roles[random.nextInt(roles.length)];

        // 스탯 랜덤
        Stats s = Stats.of(
                50 + random.nextInt(40),
                8 + random.nextInt(6),
                2 + random.nextInt(3),
                10 + random.nextInt(8),
                random.nextInt(40)
        );

        return new Unit(r.name(), r, TeamSide.PLAYER, s, Position.of(-1, -1)); // 위치는 나중 배치
    }

    public boolean placeOnBoard(Board board, List<Unit> units, Unit u, Position pos) {
        if (!board.isInside(pos)) return false;
        if (pos.y() != board.height() - 1) return false;
        boolean occupied = units.stream().anyMatch(e -> !e.isDead() && e.position().equals(pos));
        if (occupied) return false;
        u.moveTo(pos);
        units.add(u);
        return true;
    }
}