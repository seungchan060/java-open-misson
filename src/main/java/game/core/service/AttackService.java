package game.core.service;

import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.List;

public final class AttackService {
    public void basicAttack(Unit attacker, Unit defender) {
        if (attacker.isDead() || defender.isDead()) return;

        int raw = attacker.stats().atk(); // 기본 공격력

        double factor = 1.0 - nearestBodyBlockProtection(defender, attacker); // 0.3이면 70%로
        int adjusted = Math.max(1, (int)Math.floor(raw * factor));

        defender.stats().applyDamage(adjusted);

        System.out.printf("⚔%s → %s 공격! (남은 HP: %d)%n",
                attacker.name(), defender.name(), defender.stats().hp());

        if (defender.isDead()) {
            System.out.printf("%s 사망!%n", defender.name());
        }
    }

    private double nearestBodyBlockProtection(Unit defender, Unit attacker) {
        List<Unit> all = ContextUnitsHolder.currentUnits();
        if (all == null) return 0.0;

        for (Unit u : all) {
            if (u.isDead()) continue;
            if (u.side() != defender.side()) continue;
            if (!u.isBodyBlocking()) continue;
            // 인접?
            int dist = u.position().manhattanDistance(defender.position());
            if (dist == 1) {
                return 0.30;
            }
        }
        return 0.0;
    }
}