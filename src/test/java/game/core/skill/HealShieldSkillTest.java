package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HealShieldSkillTest {

    @Test
    void healshield_applies_12shield_for_2turns_and_expires() {
        Board b = new Board(5,4);
        Unit monk = new Unit("Monk", Role.MONK, TeamSide.PLAYER,
                Stats.of(30, 3, 0, 0, 5), Position.of(2,2));
        Unit ally = new Unit("Ally", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(25, 4, 0, 0, 0), Position.of(2,3));

        Skill heal = new HealShield();

        // 사거리 2 이내 아군에게만 사용 가능
        assertThat(heal.canUse(monk, b, List.of(monk, ally), ally.position())).isTrue();

        heal.use(monk, b, List.of(monk, ally), ally.position());
        assertThat(ally.hasShield()).isTrue();
        assertThat(ally.shieldAmount()).isEqualTo(12);

        // 1턴 경과: 아직 남아야 함
        ally.tickShield();
        assertThat(ally.hasShield()).isTrue();

        // 2턴 경과: 만료
        ally.tickShield();
        assertThat(ally.hasShield()).isFalse();
        assertThat(ally.shieldAmount()).isZero();
    }
}