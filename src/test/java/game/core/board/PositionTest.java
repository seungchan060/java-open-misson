package game.core.board;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PositionTest {

    @Test
    void parse_valid() {
        Position p = Position.parse("3,3");
        assertThat(p.x()).isEqualTo(3);
        assertThat(p.y()).isEqualTo(3);
    }

    @Test
    void parse_with_spaces() {
        Position p = Position.parse(" 1 , 2 ");
        assertThat(p).isEqualTo(Position.of(1,2));
    }

    @Test
    void parse_invalid_format() {
        assertThatThrownBy(() -> Position.parse("1;2"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void manhattan_distance() {
        Position a = Position.of(0,0);
        Position b = Position.of(2,1);
        assertThat(a.manhattanDistance(b)).isEqualTo(3);
    }
}
