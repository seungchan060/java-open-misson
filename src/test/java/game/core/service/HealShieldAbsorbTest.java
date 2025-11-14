package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import game.core.skill.HealShield;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HealShieldAbsorbTest {

    @Test
    void shield_absorbs_damage_before_hp() {
        Board b = new Board(5,4);
        AttackService atk = new AttackService();

        Unit monk = new Unit("Monk", Role.MONK, TeamSide.PLAYER,
                Stats.of(30, 3, 0, 0, 5), Position.of(2,2));
        Unit ally = new Unit("Ally", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(20, 4, 0, 0, 0), Position.of(2,3));
        Unit enemy = new Unit("Enemy", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(20, 10, 0, 0, 0), Position.of(2,4));

        // 보호막 +12, 2턴
        new HealShield().use(monk, b, List.of(monk, ally), ally.position());
        assertThat(ally.hasShield()).isTrue();
        assertThat(ally.shieldAmount()).isEqualTo(12);

        List<Unit> all = new ArrayList<>(List.of(monk, ally, enemy));
        ContextUnitsHolder.open(all);
        atk.basicAttack(enemy, ally); // raw 10 → shield 12가 먼저 흡수 → HP 그대로
        ContextUnitsHolder.close();

        assertThat(ally.stats().hp()).isEqualTo(20);
        // 남은 보호막은 2
        assertThat(ally.shieldAmount()).isEqualTo(2);

        // 다음 공격 10 → 남은 보호막 2 흡수 → 8 HP 감소
        ContextUnitsHolder.open(all);
        atk.basicAttack(enemy, ally);
        ContextUnitsHolder.close();

        assertThat(ally.stats().hp()).isEqualTo(20 - 8);
        assertThat(ally.hasShield()).isTrue(); // 지속 2턴 동안은 존재
    }
}