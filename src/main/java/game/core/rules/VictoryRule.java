package game.core.rules;

import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.List;

public final class VictoryRule {
    private final int maxTurns;

    public VictoryRule(int maxTurns) { this.maxTurns = maxTurns; }

    private boolean hasAlive(List<Unit> units, TeamSide side) {
        for (Unit u : units) if (!u.isDead() && u.side() == side) return true;
        return false;
    }
    private int countAlive(List<Unit> units, TeamSide side) {
        int c = 0; for (Unit u : units) if (!u.isDead() && u.side() == side) c++; return c;
    }
    private int totalHp(List<Unit> units, TeamSide side) {
        int sum = 0; for (Unit u : units) if (!u.isDead() && u.side() == side) sum += u.stats().hp(); return sum;
    }
}