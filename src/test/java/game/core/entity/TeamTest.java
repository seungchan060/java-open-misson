package game.core.entity;

import game.core.board.Position;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TeamTest {
    @Test void field_capacity() {
        Team t = new Team(TeamSide.PLAYER);
        for (int i = 0; i < Team.MAX_FIELD; i++) {
            t.addToField(new Character("u"+i, Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(0,0)));
        }
        assertThatThrownBy(() ->
                t.addToField(new Character("uX", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(0,0)))
        ).isInstanceOf(IllegalStateException.class);
    }
}