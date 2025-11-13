package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BodyBlockSkillTest {

    @Test
    void bodyblock_applies_for_one_turn() {
        Board b = new Board(5,4);
        Unit tank = new Unit("T", Role.TANK, TeamSide.PLAYER,
                Stats.of(40, 3, 0, 0, 5), Position.of(2,2));
        Skill bb = new BodyBlock();

        assertThat(bb.canUse(tank, b, java.util.List.of(tank), tank.position())).isTrue();
        bb.use(tank, b, java.util.List.of(tank), tank.position());

        assertThat(tank.isBodyBlocking()).isTrue();
        tank.tickBodyBlock();
        assertThat(tank.isBodyBlocking()).isFalse();
    }
}