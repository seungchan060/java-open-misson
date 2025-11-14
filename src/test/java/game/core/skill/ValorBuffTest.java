package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.*;
import game.core.service.AttackService;
import game.core.service.ContextUnitsHolder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ValorBuffTest {

    @Test
    void valor_increases_attack_and_reduces_incoming_damage() {
        Board b = new Board(5,4);
        AttackService atk = new AttackService();

        // 공격자에게 Valor → ATK +2
        Unit attacker = new Unit("A", Role.MONK, TeamSide.PLAYER,
                Stats.of(30, 5, 0, 0, 5), Position.of(2,2));
        new Valor().use(attacker, b, List.of(attacker), attacker.position());

        Unit defender = new Unit("D", Role.KNIGHT, TeamSide.ENEMY,
                Stats.of(35, 4, 0, 0, 0), Position.of(2,3));

        ContextUnitsHolder.open(List.of(attacker, defender));
        atk.basicAttack(attacker, defender);
        ContextUnitsHolder.close();

        assertThat(defender.stats().hp()).isEqualTo(35 - 7);

        // 이제 defender도 Valor를 받아서 피해 20% 경감
        new Valor().use(defender, b, List.of(attacker, defender), defender.position());

        int hpBefore = defender.stats().hp();
        ContextUnitsHolder.open(List.of(attacker, defender));
        atk.basicAttack(attacker, defender); // 7 피해의 20% 경감 → floor(7*0.8)=5
        ContextUnitsHolder.close();

        assertThat(defender.stats().hp()).isEqualTo(hpBefore - 5);

        // 턴 감소 테스트
        defender.tickValor();
        defender.tickValor(); // 2턴 지나면 해제
        assertThat(defender.valorDamageReduce()).isEqualTo(0.0);
        assertThat(defender.hasValor()).isFalse();
    }
}