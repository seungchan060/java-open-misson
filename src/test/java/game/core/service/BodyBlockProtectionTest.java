package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import game.core.skill.BodyBlock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BodyBlockProtectionTest {

    @Test
    void adjacent_tank_with_bodyblock_reduces_damage_by_30_percent() {
        Board b = new Board(5,4);
        AttackService atk = new AttackService();

        Unit tank = new Unit("T", Role.TANK, TeamSide.PLAYER,
                Stats.of(50, 2, 0, 0, 5), Position.of(2,2));
        new BodyBlock().use(tank, b, List.of(tank), tank.position()); // bodyblock on

        Unit ally = new Unit("A", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(30, 3, 0, 0, 0), Position.of(2,3)); // 탱커와 인접
        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(30, 10, 0, 0, 0), Position.of(2,4)); // 적이 ally를 때림

        List<Unit> all = new ArrayList<>(List.of(tank, ally, enemy));

        ContextUnitsHolder.open(all);
        atk.basicAttack(enemy, ally);
        ContextUnitsHolder.close();

        assertThat(ally.stats().hp()).isEqualTo(30 - 7);
    }
}