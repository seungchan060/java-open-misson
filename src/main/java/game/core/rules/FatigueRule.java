package game.core.rules;

import game.core.entity.Unit;

import java.util.List;

public final class FatigueRule {
    public void applyEndOfTurn(int turn, List<Unit> units) {
        int dmg = 0;
        if (turn >= 15) dmg = 10;
        else if (turn >= 10) dmg = 5;

        if (dmg > 0) {
            for (Unit u : units) if (!u.isDead()) u.stats().applyDamage(dmg);
            System.out.printf("피로도 적용: 턴 %d, 모든 생존 유닛에게 -%d HP%n", turn, dmg);
        }
    }
}