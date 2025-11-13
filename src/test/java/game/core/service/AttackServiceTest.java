package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AttackServiceTest {

    @Test
    void basic_attack_reduces_hp() {
        Board b = new Board(5,4);
        AttackService atk = new AttackService();

        Unit attacker = new Unit("A", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(30, 10, 0, 0, 0), Position.of(2,2));
        Unit defender = new Unit("B", Role.MAGE, TeamSide.ENEMY,
                Stats.of(25, 3, 0, 0, 0), Position.of(2,3));

        List<Unit> all = List.of(attacker, defender);

        ContextUnitsHolder.open(all);

        atk.basicAttack(attacker, defender);

        ContextUnitsHolder.close();

        assertThat(defender.stats().hp()).isEqualTo(25 - 10);
    }
}