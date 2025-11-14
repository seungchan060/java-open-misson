package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiFocusFireTest {

    @Test
    void enemies_focus_same_target_within_one_enemy_phase() {
        Board b = new Board(5,4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        Unit e1 = new Unit("E1", Role.KNIGHT, TeamSide.ENEMY, Stats.of(25, 6, 0, 0, 0), Position.of(2,1));
        Unit e2 = new Unit("E2", Role.KNIGHT, TeamSide.ENEMY, Stats.of(25, 6, 0, 0, 0), Position.of(3,1));

        Unit monk   = new Unit("Monk", Role.MONK,   TeamSide.PLAYER, Stats.of(18, 3, 0, 0, 5), Position.of(2,2));
        Unit knight = new Unit("P",    Role.KNIGHT, TeamSide.PLAYER, Stats.of(24, 5, 0, 0, 0), Position.of(3,2));

        List<Unit> all = new ArrayList<>(List.of(e1, e2, monk, knight));
        ContextUnitsHolder.open(all);

        ai.beginEnemyPhase();

        int monkHpBefore = monk.stats().hp();

        ai.takeTurn(b, all);
        ai.takeTurn(b, all);

        ContextUnitsHolder.close();

        assertThat(monk.stats().hp()).isLessThan(monkHpBefore - 5);
        assertThat(knight.stats().hp()).isEqualTo(24);
    }
}