package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiStealthIgnoreTest {

    @Test
    void stealth_targets_are_ignored_unless_taunting() {
        Board b = new Board(5,4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY, Stats.of(25, 5, 0, 0, 0), Position.of(2,1));

        Unit rogue = new Unit("R", Role.ROGUE, TeamSide.PLAYER, Stats.of(18, 5, 0, 0, 3), Position.of(2,2));
        rogue.applyStealth(1); // 은신

        Unit visible = new Unit("V", Role.KNIGHT, TeamSide.PLAYER, Stats.of(24, 5, 0, 0, 0), Position.of(3,1));

        List<Unit> all = new ArrayList<>(List.of(enemy, rogue, visible));
        ContextUnitsHolder.open(all);

        ai.beginEnemyPhase();
        int rHp = rogue.stats().hp();
        int vHp = visible.stats().hp();

        ai.takeTurn(b, all);

        ContextUnitsHolder.close();

        assertThat(rogue.stats().hp()).isEqualTo(rHp);
        assertThat(visible.stats().hp()).isLessThan(vHp);
    }
}