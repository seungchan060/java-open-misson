package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import game.core.skill.Taunt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiTauntTest {

    @Test
    void ai_attacks_taunt_target_even_if_others_adjacent() {
        Board b = new Board(5, 4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        // 적
        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(20, 4, 0, 0, 0), Position.of(2, 1));

        // 아군 2명 (둘 다 적과 인접)
        Unit tank = new Unit("T", Role.TANK, TeamSide.PLAYER,
                Stats.of(40, 2, 0, 0, 3), Position.of(3, 1)); // (3,1): 적과 인접
        Unit exposed = new Unit("P", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(30, 4, 0, 0, 0), Position.of(2, 2));  // (2,2): 적과 인접

        // 탱커: 도발 1턴
        new Taunt().use(tank, b, List.of(tank, exposed, enemy), tank.position());
        assertThat(tank.isTaunting()).isTrue();

        int tankHpBefore = tank.stats().hp();
        int exposedHpBefore = exposed.stats().hp();

        // 적 턴 → 도발 대상(tank)을 우선 공격해야 함
        ai.takeTurn(b, new ArrayList<>(List.of(tank, exposed, enemy)));

        assertThat(tank.stats().hp()).isLessThan(tankHpBefore);     // 탱커가 맞는다
        assertThat(exposed.stats().hp()).isEqualTo(exposedHpBefore); // 다른 아군은 안전
    }

    @Test
    void ai_moves_toward_taunt_target_instead_of_hitting_adjacent_non_taunt() {
        Board b = new Board(5, 4);
        MovementService move = new MovementService();
        AttackService atk = new AttackService();
        EnemyAiService ai = new EnemyAiService(move, atk);

        // 적
        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(20, 4, 0, 0, 0), Position.of(2, 1));

        // 탱커(도발, 비인접 위치)
        Unit tank = new Unit("T", Role.TANK, TeamSide.PLAYER,
                Stats.of(40, 2, 0, 0, 3), Position.of(4, 1)); // 두 칸 떨어짐
        new Taunt().use(tank, b, List.of(tank, enemy), tank.position());
        assertThat(tank.isTaunting()).isTrue();

        // 인접하지만 도발 아님
        Unit exposed = new Unit("P", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(30, 4, 0, 0, 0), Position.of(3, 1)); // 적과 인접

        int exposedHpBefore = exposed.stats().hp();
        Position enemyBefore = enemy.position();

        // 적 턴 → 인접한 exposed를 때리는 대신, 도발 대상에게 "접근"해야 함
        ai.takeTurn(b, new ArrayList<>(List.of(tank, exposed, enemy)));

        // 인접 아군(exposed)의 HP는 그대로 (공격 안 함)
        assertThat(exposed.stats().hp()).isEqualTo(exposedHpBefore);
        // 적은 도발 대상(tank)을 향해 한 칸 이동했어야 함
        assertThat(enemy.position()).isNotEqualTo(enemyBefore);
    }
}