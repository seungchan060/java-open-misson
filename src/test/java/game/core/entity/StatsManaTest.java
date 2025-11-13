package game.core.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StatsManaTest {

    @Test
    void consume_mana_and_check_hasMana() {
        Stats s = Stats.of(30, 5, 1, 0, 3);
        assertThat(s.hasMana(2)).isTrue();
        assertThat(s.consumeMana(2)).isTrue();
        assertThat(s.mana()).isEqualTo(1);

        // 부족하면 소비 실패하고 그대로
        assertThat(s.consumeMana(5)).isFalse();
        assertThat(s.mana()).isEqualTo(1);
    }
}