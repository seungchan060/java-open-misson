package game.core.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class StatsTest {
    @Test void damage_never_negative_hp() {
        Stats s = Stats.of(50, 10, 3, 10, 0);
        s.applyDamage(100);
        assertThat(s.hp()).isZero();
        assertThat(s.isDead()).isTrue();
    }

    @Test void defense_applied_min1() {
        Stats s = Stats.of(20, 5, 10, 0, 0);
        s.applyDamage(8);
        assertThat(s.hp()).isEqualTo(19);
    }
}