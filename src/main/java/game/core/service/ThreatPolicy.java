package game.core.service;

import game.core.entity.Role;
import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.Comparator;
import java.util.List;

final class ThreatPolicy {
    List<Unit> candidates(List<Unit> all) {
        return all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER)
                .filter(u -> u.isTaunting() || !u.isStealthed())
                .toList();
    }

    double score(Unit attacker, Unit target) {
        int dist = attacker.position().manhattanDistance(target.position());
        boolean adjacent = dist == 1;

        double s = 0.0;

        // 도발이면 무조건 크게 가중
        if (target.isTaunting()) s += 1000;

        // 즉시 때릴 수 있으면 가중
        if (adjacent) s += 50;

        s += switch (target.role()) {
            case MONK   -> 30;   // 지원/힐
            case MAGE   -> 25;   // 딜러
            case ARCHER -> 22;   // 딜러
            case ROGUE  -> 18;
            case KNIGHT -> 10;
            case TANK   -> 5;
        };

        s += Math.max(0, 20 - target.stats().hp() * 0.5);
        s += Math.max(0, 15 - dist * 3);
        if (target.shieldAmount() > 0) s -= 8;
        if (target.hasValor()) s -= 6;

        return s;
    }

    Unit bestTarget(Unit attacker, List<Unit> pool) {
        return pool.stream()
                .max(Comparator.<Unit>comparingDouble(t -> score(attacker, t))
                        .thenComparingInt(t -> t.stats().hp() * -1)
                        .thenComparingInt(t -> -attacker.position().manhattanDistance(t.position())))
                .orElse(null);
    }
}