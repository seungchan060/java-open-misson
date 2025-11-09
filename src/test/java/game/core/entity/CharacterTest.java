package game.core.entity;

import game.core.board.Position;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class CharacterTest {
    @Test void move_position() {
        Character c = new Character("KnightA", Role.KNIGHT, TeamSide.PLAYER, Stats.of(30,10,2,5,0), Position.of(1,1));
        c.moveTo(Position.of(2,1));
        assertThat(c.position()).isEqualTo(Position.of(2,1));
    }
}