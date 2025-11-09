package game.core.entity;

import game.core.board.Position;
import java.util.Objects;

public final class Character {
    private final String name;
    private final Role role;
    private final TeamSide side;
    private final Stats stats;
    private Position position;

    public Character(String name, Role role, TeamSide side, Stats stats, Position position) {
        this.name = Objects.requireNonNull(name);
        this.role = Objects.requireNonNull(role);
        this.side = Objects.requireNonNull(side);
        this.stats = Objects.requireNonNull(stats);
        this.position = Objects.requireNonNull(position);
    }

    public String name() { return name; }
    public Role role() { return role; }
    public TeamSide side() { return side; }
    public Stats stats() { return stats; }
    public Position position() { return position; }

    public void moveTo(Position p) {
        this.position = Objects.requireNonNull(p);
    }

    public boolean isDead() { return stats.isDead(); }
}