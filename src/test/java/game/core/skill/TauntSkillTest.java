package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TauntSkillTest {

    @Test
    void taunt_applies_state_for_one_turn() {
        Board b = new Board(5, 4);
        Unit tank = new Unit("Tank", Role.TANK, TeamSide.PLAYER,
                Stats.of(40, 4, 0, 0, 5), Position.of(2, 2));
        Skill taunt = new Taunt();

        // canUse는 항상 true
        assertThat(taunt.canUse(tank, b, List.of(tank), tank.position())).isTrue();

        // 사용 → 도발 1턴
        taunt.use(tank, b, List.of(tank), tank.position());
        assertThat(tank.isTaunting()).isTrue();

        // 턴 종료 → 도발 감소
        tank.tickTaunt();
        assertThat(tank.isTaunting()).isFalse();
    }
}