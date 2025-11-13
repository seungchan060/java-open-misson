package game.core.service;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EnemyAiStealthTest {

    @Test
    void ai_ignores_stealthed_units_and_does_nothing_if_only_stealth_targets() {
        Board b = new Board(5,4);
        var move = new MovementService();
        var atk  = new AttackService();
        var ai   = new EnemyAiService(move, atk);

        Unit stealthed = new Unit("Rogue", Role.ROGUE, TeamSide.PLAYER,
                Stats.of(20, 5, 0, 0, 5), Position.of(2,2));
        Unit enemy = new Unit("Mob", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(20, 2, 0, 0, 0), Position.of(2,1));

        stealthed.applyStealth(1);

        List<Unit> all = new ArrayList<>(List.of(stealthed, enemy));
        Position before = enemy.position();

        ai.takeTurn(b, all);

        // 유일한 플레이어가 은신 중 → AI는 아무것도 못함
        assertThat(enemy.position()).isEqualTo(before);
        assertThat(stealthed.stats().hp()).isEqualTo(20); // 피해 없음
    }

    @Test
    void ai_targets_non_stealthed_if_available() {
        Board b = new Board(5,4);
        var move = new MovementService();
        var atk  = new AttackService();
        var ai   = new EnemyAiService(move, atk);

        Unit stealthed = new Unit("Rogue", Role.ROGUE, TeamSide.PLAYER,
                Stats.of(20, 5, 0, 0, 5), Position.of(2,2));
        Unit exposed = new Unit("Knight", Role.KNIGHT, TeamSide.PLAYER,
                Stats.of(30, 4, 0, 0, 0), Position.of(2,3));
        Unit enemy = new Unit("Mob", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(20, 2, 0, 0, 0), Position.of(2,1));

        stealthed.applyStealth(1);

        List<Unit> all = new ArrayList<>(List.of(stealthed, exposed, enemy));

        ai.takeTurn(b, all); // 인접한 exposed를 우선 공격

        assertThat(exposed.stats().hp()).isLessThan(30);
        assertThat(stealthed.stats().hp()).isEqualTo(20); // 은신 대상은 안전
    }
}