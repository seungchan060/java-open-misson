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
        boolean p = hasAlive(units, TeamSide.PLAYER);
        boolean e = hasAlive(units, TeamSide.ENEMY);
        if (p && !e) return TeamSide.PLAYER;
        if (!p && e) return TeamSide.ENEMY;
        if (p && e) return null; // 아직 진행중

        // 턴 한도 또는 동시 전멸 -> 타이브레이커: 생존 수 -> 총 HP
        int pAlive = countAlive(units, TeamSide.PLAYER);
        int eAlive = countAlive(units, TeamSide.ENEMY);
        if (pAlive != eAlive) return pAlive > eAlive ? TeamSide.PLAYER : TeamSide.ENEMY;

        int pHp = totalHp(units, TeamSide.PLAYER);
        int eHp = totalHp(units, TeamSide.ENEMY);
        if (pHp != eHp) return pHp > eHp ? TeamSide.PLAYER : TeamSide.ENEMY;

        return null; // 완전 무승부
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