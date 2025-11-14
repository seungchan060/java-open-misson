package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import game.core.skill.BodyBlock;
import game.core.skill.HealShield;
import game.core.skill.Valor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SupportSynergyTest {

    @Test
    void bodyblock_shield_valor_stack_in_expected_order() {
        Board b = new Board(5,4);
        AttackService atk = new AttackService();

        Unit tank = new Unit("Tank", Role.TANK, TeamSide.PLAYER,
                Stats.of(40, 3, 0, 0, 5), Position.of(2,2));
        Unit carry = new Unit("Carry", Role.ARCHER, TeamSide.PLAYER,
                Stats.of(28, 6, 0, 0, 0), Position.of(2,3));
        Unit enemy = new Unit("Boss", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(40, 12, 0, 0, 0), Position.of(2,4));

        new BodyBlock().use(tank, b, List.of(tank, carry, enemy), tank.position());
        new HealShield().use(new Unit("Monk", Role.MONK, TeamSide.PLAYER,
                Stats.of(30,3,0,0,5), Position.of(1,1)), b, List.of(tank, carry, enemy), carry.position());
        new Valor().use(carry, b, List.of(tank, carry, enemy), carry.position());

        List<Unit> all = new ArrayList<>(List.of(tank, carry, enemy));
        ContextUnitsHolder.open(all);

        atk.basicAttack(enemy, carry);
        ContextUnitsHolder.close();

        assertThat(carry.stats().hp()).isEqualTo(28);
        assertThat(carry.shieldAmount()).isEqualTo(4);
    }
}