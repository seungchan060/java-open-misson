package game.core.rules;

import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FatigueRuleTest {

    @Test void apply_after_10_and_15() {
        Unit u = new Unit("P1", Role.KNIGHT, TeamSide.PLAYER, Stats.of(30,1,0,0,0), Position.of(0,0));
        List<Unit> list = List.of(u);
        FatigueRule f = new FatigueRule();

        // turn 9: 0
        f.applyEndOfTurn(9, list);
        assertThat(u.stats().hp()).isEqualTo(30);

        // turn 10: -5
        f.applyEndOfTurn(10, list);
        assertThat(u.stats().hp()).isEqualTo(25);

        // turn 15: -10
        f.applyEndOfTurn(15, list);
        assertThat(u.stats().hp()).isEqualTo(15);
    }
}