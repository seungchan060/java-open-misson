package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiDetourMoveTest {

    @Test
    void enemy_detours_when_direct_path_is_blocked() {
        Board b = new Board(5,4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        // 적 (2,1) → 목표 (4,1), 중간 (3,1)에 다른 아군이 막고 있음
        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY, Stats.of(25, 5, 0, 0, 0), Position.of(2,1));
        Unit blocker = new Unit("P", Role.KNIGHT, TeamSide.PLAYER, Stats.of(30, 4, 0, 0, 0), Position.of(3,1));
        Unit target  = new Unit("T", Role.MAGE,   TeamSide.PLAYER, Stats.of(20, 6, 0, 0, 3), Position.of(4,1));

        List<Unit> all = new ArrayList<>(List.of(enemy, blocker, target));
        ContextUnitsHolder.open(all);

        ai.beginEnemyPhase();
        Position before = enemy.position();

        ai.takeTurn(b, all); // 직진 RIGHT가 막히면 UP/DOWN 우회 중 하나 시도

        ContextUnitsHolder.close();

        assertThat(enemy.position()).isNotEqualTo(before); // 최소 한 칸은 움직였음
    }
}