package game.core.board;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class BoardTest {

    @Test
    void inside_check() {
        Board b = new Board(5,4);
        assertThat(b.isInside(Position.of(0,0))).isTrue();
        assertThat(b.isInside(Position.of(4,3))).isTrue();
        assertThat(b.isInside(Position.of(5,0))).isFalse();
        assertThat(b.isInside(Position.of(0,4))).isFalse();
    }
}
