package game.core.bench;

import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BenchTest {
    @Test void capacity_and_remove() {
        Bench bench = new Bench(3);
        var u1 = new Unit("U1", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(-1,-1));
        var u2 = new Unit("U2", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(-1,-1));
        var u3 = new Unit("U3", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(-1,-1));
        var u4 = new Unit("U4", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(-1,-1));

        assertThat(bench.add(u1)).isTrue();
        assertThat(bench.add(u2)).isTrue();
        assertThat(bench.add(u3)).isTrue();
        assertThat(bench.add(u4)).isFalse(); // 초과

        var removed = bench.remove(1);
        assertThat(removed.name()).isEqualTo("U2");
        assertThat(bench.add(u4)).isTrue();
    }
}