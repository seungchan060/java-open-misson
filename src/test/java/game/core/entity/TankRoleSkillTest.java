package game.core.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TankRoleSkillTest {

    @Test
    void tank_has_taunt_skill() {
        assertThat(Role.TANK.skills())
                .isNotEmpty()
                .anySatisfy(s -> assertThat(s.name()).containsIgnoringCase("taunt"));
    }
}