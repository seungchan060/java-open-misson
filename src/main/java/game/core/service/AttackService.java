package game.core.service;

import game.core.entity.Unit;

import java.util.List;

public final class AttackService {
    public void basicAttack(Unit attacker, Unit defender) {
        if (attacker.isDead() || defender.isDead()) return;

        int raw = attacker.stats().atk() + attacker.valorAtkBonus();

        double reduceFromBodyBlock = nearestBodyBlockProtection(defender);
        int afterBodyBlock = Math.max(0, (int) Math.floor(raw * (1.0 - reduceFromBodyBlock)));

        int afterShield = defender.absorbDamage(afterBodyBlock);

        if (afterShield <= 0) {
            System.out.printf("%s → %s 공격! (남은 HP: %d)%n",
                    attacker.name(), defender.name(), defender.stats().hp());
            return;
        }

        double reduceFromValor = defender.valorDamageReduce();
        int reduced = Math.max(0, (int) Math.floor(afterShield * (1.0 - reduceFromValor)));

        int finalDamage = Math.max(1, reduced);

        defender.stats().applyDamage(finalDamage);

        System.out.printf("%s → %s 공격! (남은 HP: %d)%n",
                attacker.name(), defender.name(), defender.stats().hp());

        if (defender.isDead()) {
            System.out.printf("%s 사망!%n", defender.name());
        }
    }

    private double nearestBodyBlockProtection(Unit defender) {
        List<Unit> all = ContextUnitsHolder.currentUnits();
        if (all == null) return 0.0;

        for (Unit u : all) {
            if (u.isDead()) continue;
            if (u.side() != defender.side()) continue;
            if (!u.isBodyBlocking()) continue;
            // 인접?
            int dist = u.position().manhattanDistance(defender.position());
            if (dist == 1) return 0.30;
        }
        return 0.0;
    }
}