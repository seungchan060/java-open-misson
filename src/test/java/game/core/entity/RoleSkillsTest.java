package game.core.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class RoleSkillsTest {

    @Test
    void rogue_has_two_skills() {
        assertThat(Role.ROGUE.skills()).hasSize(2);
        assertThat(Role.ROGUE.skills().get(0).name()).isNotBlank();
        assertThat(Role.ROGUE.skills().get(1).name()).isNotBlank();
    }
}