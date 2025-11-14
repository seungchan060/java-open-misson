package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiHealerPriorityTest {

    @Test
    void healer_is_preferred_target_when_all_else_equal() {
        Board b = new Board(5,4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY, Stats.of(25, 5, 0, 0, 0), Position.of(2,1));

        // 동일 거리(둘 다 인접)
        Unit monk   = new Unit("Healer", Role.MONK,   TeamSide.PLAYER, Stats.of(22, 3, 0, 0, 5), Position.of(2,2));
        Unit knight = new Unit("Front",  Role.KNIGHT, TeamSide.PLAYER, Stats.of(28, 6, 0, 0, 0), Position.of(3,1));

        List<Unit> all = new ArrayList<>(List.of(enemy, monk, knight));
        ContextUnitsHolder.open(all);

        ai.beginEnemyPhase();
        int monkHpBefore = monk.stats().hp();
        int knightHpBefore = knight.stats().hp();

        ai.takeTurn(b, all); // 인접이면 공격

        ContextUnitsHolder.close();

        // 힐러가 먼저 맞아야 함
        assertThat(monk.stats().hp()).isLessThan(monkHpBefore);
        assertThat(knight.stats().hp()).isEqualTo(knightHpBefore);
    }
}