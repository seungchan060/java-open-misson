package game.core.rules;

import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VictoryRuleTest {

    @Test void over_when_one_side_all_dead() {
        VictoryRule v = new VictoryRule(20);

        List<Unit> units = new ArrayList<>();
        units.add(new Unit("P1", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(0,0)));
        units.add(new Unit("E1", Role.KNIGHT, TeamSide.ENEMY,  Stats.of(10,1,0,0,0), Position.of(0,1)));

        // ENEMY 전멸
        units.get(1).stats().applyDamage(999);
        assertThat(v.isOver(1, units)).isTrue();
        assertThat(v.winner(1, units)).isEqualTo(TeamSide.PLAYER);
    }

    @Test void max_turn_decides_by_alive_then_hp() {
        VictoryRule v = new VictoryRule(3);

        List<Unit> units = new ArrayList<>();
        units.add(new Unit("P1", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(0,0)));
        units.add(new Unit("P2", Role.KNIGHT, TeamSide.PLAYER, Stats.of(10,1,0,0,0), Position.of(1,0)));
        units.add(new Unit("E1", Role.KNIGHT, TeamSide.ENEMY,  Stats.of(5,1,0,0,0),  Position.of(0,1)));

        // 최대 턴 도달 시: 생존 수 우선 → PLAYER 승
        assertThat(v.isOver(3, units)).isTrue();
        assertThat(v.winner(3, units)).isEqualTo(TeamSide.PLAYER);
    }
}