package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SmokeBombTest {

    @Test
    void apply_stealth_for_one_turn() {
        Board b = new Board(5,4);
        Unit rogue = new Unit("R", Role.ROGUE, TeamSide.PLAYER,
                Stats.of(30, 5, 0, 0, 2), Position.of(1,1));

        List<Unit> all = new ArrayList<>(List.of(rogue));
        Skill smoke = new SmokeBomb();

        assertThat(smoke.canUse(rogue, b, all, rogue.position())).isTrue();

        // 엔진에서 MP/쿨다운을 처리하므로 여기서는 은신만 적용됨
        smoke.use(rogue, b, all, null);
        assertThat(rogue.isStealthed()).isTrue();

        // 한 턴이 지나면 해제
        rogue.tickStealth();
        assertThat(rogue.isStealthed()).isFalse();
    }
}