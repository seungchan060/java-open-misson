package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BackstabTest {

    @Test
    void deals_bonus_when_attacking_from_behind_enemy() {
        Board b = new Board(5,4);
        Unit rogue = new Unit("R", Role.ROGUE, TeamSide.PLAYER,
                Stats.of(50, 10, 0, 0, 5), Position.of(2,2));
        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(40, 4, 0, 0, 0), Position.of(2,1)); // 바로 위 = 적의 등 뒤

        List<Unit> all = new ArrayList<>(List.of(rogue, enemy));
        Skill backstab = new Backstab();

        assertThat(backstab.canUse(rogue, b, all, enemy.position())).isTrue();
        backstab.use(rogue, b, all, enemy.position());

        // 보너스가 들어가 HP가 크게 줄어야 한다 (기본10 + 보너스6 - def0 = 16)
        assertThat(enemy.stats().hp()).isEqualTo(40 - 16);
    }

    @Test
    void normal_damage_when_not_behind() {
        Board b = new Board(5,4);
        Unit rogue = new Unit("R", Role.ROGUE, TeamSide.PLAYER,
                Stats.of(50, 10, 0, 0, 5), Position.of(2,2));
        Unit enemy = new Unit("E", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(40, 4, 0, 0, 0), Position.of(3,2)); // 옆 = 등 뒤 아님

        List<Unit> all = new ArrayList<>(List.of(rogue, enemy));
        Skill backstab = new Backstab();

        assertThat(backstab.canUse(rogue, b, all, enemy.position())).isTrue();
        backstab.use(rogue, b, all, enemy.position());

        // 기본 10만 들어감
        assertThat(enemy.stats().hp()).isEqualTo(30);
    }
}