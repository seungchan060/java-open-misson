package game.core.entity;

import game.core.board.Position;
import game.core.skill.ShieldBash;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UnitCooldownStealthTest {

    @Test
    void cooldown_ticks_down_each_turn() {
        Unit u = new Unit("Knight", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(20, 3, 0, 0, 5), Position.of(1,1));
        var skill = new ShieldBash();

        // MP 충분 & 쿨다운 0
        assertThat(u.isSkillReady(skill)).isTrue();
        u.startSkillCooldown(skill);
        assertThat(u.isSkillReady(skill)).isFalse();

        u.tickCooldown(); // 1턴 감소
        // ShieldBash.cooldown() == 1 가정 → 바로 0
        assertThat(u.isSkillReady(skill)).isTrue();
    }

    @Test
    void stealth_last_one_turn() {
        Unit u = new Unit("Rogue", Role.ROGUE, TeamSide.PLAYER,
                Stats.of(20, 3, 0, 0, 5), Position.of(2,2));
        u.applyStealth(1);
        assertThat(u.isStealthed()).isTrue();
        u.tickStealth();
        assertThat(u.isStealthed()).isFalse();
    }
}