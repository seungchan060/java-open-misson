package game.core.service;

import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.List;

public final class AttackService {
    public void basicAttack(Unit attacker, Unit defender) {
        if (attacker.isDead() || defender.isDead()) return;

        // 공격력 계산
        int raw = attacker.stats().atk() + attacker.valorAtkBonus();

        // BodyBlock 보호(피격자 인접 탱커) 적용
        double reduceFromBodyBlock = nearestBodyBlockProtection(defender);
        int afterBodyBlock = Math.max(0, (int)Math.floor(raw * (1.0 - reduceFromBodyBlock)));

        // Defender 보호막으로 흡수
        int afterShield = defender.absorbDamage(afterBodyBlock);

        // Defender Valor 경감
        double reduceFromValor = defender.valorDamageReduce();
        int finalDamage = Math.max(1, (int)Math.floor(afterShield * (1.0 - reduceFromValor)));

        // HP 적용
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
            if (dist == 1) {
                return 0.30;
            }
        }
        return 0.0;
    }
}