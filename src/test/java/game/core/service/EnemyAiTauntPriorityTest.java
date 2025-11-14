package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import game.core.skill.Taunt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiTauntPriorityTest {

    @Test
    void taunt_target_is_prioritized_over_adjacent_non_taunt() {
        Board b = new Board(5,4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY, Stats.of(25, 5, 0, 0, 0), Position.of(2,1));

        // 인접하지만 도발 아님
        Unit exposed = new Unit("P1", Role.KNIGHT, TeamSide.PLAYER, Stats.of(30, 4, 0, 0, 0), Position.of(2,2));
        // 두 칸 떨어져 있지만 도발 상태
        Unit tank = new Unit("Tank", Role.TANK, TeamSide.PLAYER, Stats.of(40, 3, 0, 0, 5), Position.of(4,1));
        new Taunt().use(tank, b, List.of(tank, exposed, enemy), tank.position());

        List<Unit> all = new ArrayList<>(List.of(enemy, exposed, tank));
        ContextUnitsHolder.open(all);

        ai.beginEnemyPhase();
        int exposedHpBefore = exposed.stats().hp();
        Position enemyBefore = enemy.position();

        ai.takeTurn(b, all); // 도발 대상 쪽으로 움직이거나, 도달 시 공격

        ContextUnitsHolder.close();

        assertThat(exposed.stats().hp()).isEqualTo(exposedHpBefore);
        assertThat(enemy.position()).isNotNull();
        assertThat(tank.isTaunting()).isTrue();
    }

    @Test
    void taunt_overrides_stealth() {
        Board b = new Board(5,4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY, Stats.of(25, 5, 0, 0, 0), Position.of(2,1));

        Unit tank = new Unit("Tank", Role.TANK, TeamSide.PLAYER, Stats.of(35, 3, 0, 0, 5), Position.of(3,1));
        // 은신 + 도발 동시: 은신이어도 도발이면 타겟팅 가능해야 함
        tank.applyStealth(1);
        new Taunt().use(tank, b, List.of(tank, enemy), tank.position());

        List<Unit> all = new ArrayList<>(List.of(enemy, tank));
        ContextUnitsHolder.open(all);

        ai.beginEnemyPhase();
        int tankHpBefore = tank.stats().hp();
        ai.takeTurn(b, all);

        ContextUnitsHolder.close();

        assertThat(tank.stats().hp()).isLessThan(tankHpBefore); // 공격받았거나 근접
    }
}