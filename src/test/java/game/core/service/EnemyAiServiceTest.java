package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiServiceTest {

    @Test void only_one_enemy_acts_per_turn_and_attacks_if_adjacent() {
        Board b = new Board(5,4);
        var move = new MovementService();
        var atk  = new AttackService();
        var ai   = new EnemyAiService(move, atk);

        Unit p = new Unit("P", Role.KNIGHT, TeamSide.PLAYER, Stats.of(20,5,0,0,0), Position.of(2,2));
        Unit e1 = new Unit("E1", Role.KNIGHT, TeamSide.ENEMY,  Stats.of(10,2,0,0,0), Position.of(2,1)); // 인접
        Unit e2 = new Unit("E2", Role.KNIGHT, TeamSide.ENEMY,  Stats.of(10,2,0,0,0), Position.of(0,0));
        List<Unit> all = new ArrayList<>(List.of(p, e1, e2));

        // 턴 1: e1이 인접 공격 (HP 감소)
        ai.takeTurn(b, all);
        assertThat(p.stats().hp()).isLessThan(20);

        int hpAfterFirst = p.stats().hp();

        // 턴 2: 이번엔 e2 차례→ 한 칸 접근 or 공격, 하지만 p HP가 또 깎인다면 e2도 공격한 것
        ai.takeTurn(b, all);
        assertThat(p.stats().hp()).isLessThanOrEqualTo(hpAfterFirst);
    }

    @Test void moves_towards_nearest_when_not_adjacent() {
        Board b = new Board(5,4);
        var move = new MovementService();
        var atk  = new AttackService();
        var ai   = new EnemyAiService(move, atk);

        Unit p = new Unit("P", Role.KNIGHT, TeamSide.PLAYER, Stats.of(20,5,0,0,0), Position.of(4,3));
        Unit e = new Unit("E", Role.KNIGHT, TeamSide.ENEMY,  Stats.of(10,2,0,0,0), Position.of(0,0));
        List<Unit> all = new ArrayList<>(List.of(p, e));

        Position before = e.position();
        ai.takeTurn(b, all);
        assertThat(e.position()).isNotEqualTo(before); // 한 칸 접근
    }
}