package game.core.service;

import game.core.board.Board;
import game.core.board.Direction;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.*;

class MovementServiceTest {
    @Test void cannot_move_outside_or_into_occupied() {
        Board b = new Board(3,3);
        Unit me = new Unit("Me", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(1,1));
        Unit block = new Unit("Block", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(2,1));
        var list = new ArrayList<Unit>();
        list.add(me); list.add(block);

        MovementService m = new MovementService();
        m.move(b, list, me, Direction.RIGHT); // 차단
        assertThat(me.position()).isEqualTo(Position.of(1,1));

        m.move(b, list, me, Direction.UP);    // 정상 이동
        assertThat(me.position()).isEqualTo(Position.of(1,0));

        m.move(b, list, me, Direction.UP);    // 보드 밖
        assertThat(me.position()).isEqualTo(Position.of(1,0));
    }
}