package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ShieldBashResistTest {

    @Test
    void bodyblock_grants_knockback_resist() {
        Board b = new Board(5,4);
        Unit knight = new Unit("K", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(30, 5, 0, 0, 5), Position.of(2,3));
        Unit victim = new Unit("V", Role.TANK, TeamSide.ENEMY,
                Stats.of(40, 3, 0, 0, 5), Position.of(2,2));

        // 피해자는 BodyBlock 상태
        new BodyBlock().use(victim, b, List.of(victim), victim.position());

        Skill bash = new ShieldBash();
        assertThat(bash.canUse(knight, b, List.of(knight, victim), victim.position())).isTrue();

        // 위치 저장
        Position before = victim.position();
        bash.use(knight, b, List.of(knight, victim), victim.position());

        // 넉백이 발생하지 않아야 함
        assertThat(victim.position()).isEqualTo(before);
    }
}
