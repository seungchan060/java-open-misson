package game.core.rules;

import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.List;

public final class VictoryRule {
    private final int maxTurns;

    public VictoryRule(int maxTurns) { this.maxTurns = maxTurns; }

    public boolean isOver(int turn, List<Unit> units) {
        return turn >= maxTurns || !hasAlive(units, TeamSide.PLAYER) || !hasAlive(units, TeamSide.ENEMY);
    }

    public TeamSide winner(int turn, List<Unit> units) {
        boolean pAlive = hasAlive(units, TeamSide.PLAYER);
        boolean eAlive = hasAlive(units, TeamSide.ENEMY);

        if (pAlive && !eAlive) return TeamSide.PLAYER;
        if (!pAlive && eAlive) return TeamSide.ENEMY;

        if (turn < maxTurns) return null;

        int pCount = countAlive(units, TeamSide.PLAYER);
        int eCount = countAlive(units, TeamSide.ENEMY);
        if (pCount != eCount) return pCount > eCount ? TeamSide.PLAYER : TeamSide.ENEMY;

        int pHp = totalHp(units, TeamSide.PLAYER);
        int eHp = totalHp(units, TeamSide.ENEMY);
        if (pHp != eHp) return pHp > eHp ? TeamSide.PLAYER : TeamSide.ENEMY;

        return null;
    }

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