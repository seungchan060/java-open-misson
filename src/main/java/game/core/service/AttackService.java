package game.core.service;

import game.core.entity.Unit;

public final class AttackService {

    public void basicAttack(Unit attacker, Unit target) {
        target.stats().applyDamage(attacker.stats().atk());
        System.out.printf("⚔️  %s → %s 공격!  (HP: %d)\n",
                attacker.name(), target.name(), target.stats().hp());
        if (target.isDead()) {
            System.out.printf("💀 %s 사망!\n", target.name());
        }
    }
}