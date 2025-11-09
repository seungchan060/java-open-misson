package game.core.service;

import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AttackServiceTest {
    @Test void basic_attack_reduces_hp_and_kills() {
        Unit a = new Unit("A", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10, 5, 0,0,0), Position.of(0,0));
        Unit b = new Unit("B", Role.KNIGHT, TeamSide.ENEMY,  Stats.of(6,  1, 0,0,0), Position.of(0,1));

        var svc = new AttackService();
        svc.basicAttack(a, b);
        assertThat(b.stats().hp()).isEqualTo(1);
        svc.basicAttack(a, b);
        assertThat(b.isDead()).isTrue();
    }
}