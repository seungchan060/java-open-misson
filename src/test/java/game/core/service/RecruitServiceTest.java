package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class RecruitServiceTest {

    @Test void roll_is_player_side_and_unplaced() {
        var r = new RecruitService();
        Unit u = r.roll();
        assertThat(u.side()).isEqualTo(TeamSide.PLAYER);
        assertThat(u.position()).isEqualTo(Position.of(-1,-1));
    }

    @Test void place_only_bottom_row_and_empty() {
        Board b = new Board(5,4);
        var units = new ArrayList<Unit>();
        var r = new RecruitService();

        Unit u = new Unit("New", Role.ARCHER, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(-1,-1));

        // 보드 밖
        assertThat(r.placeOnBoard(b, units, u, Position.of(10,10))).isFalse();

        // 상단 라인 (적 라인) 금지
        assertThat(r.placeOnBoard(b, units, u, Position.of(2,0))).isFalse();

        // 하단 라인(y==3)은 허용
        assertThat(r.placeOnBoard(b, units, u, Position.of(2,3))).isTrue();
        assertThat(units).contains(u);
        assertThat(u.position()).isEqualTo(Position.of(2,3));

        // 점유 칸에는 배치 불가
        Unit u2 = new Unit("Another", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(-1,-1));
        assertThat(r.placeOnBoard(b, units, u2, Position.of(2,3))).isFalse();
    }
}