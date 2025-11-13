package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public final class Taunt implements Skill {

    @Override public String name() { return "Taunt"; }
    @Override public int mpCost() { return 2; }
    @Override public int cooldown() { return 2; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position ignored) {
        return true;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position ignored) {
        caster.applyTaunt(1);
        System.out.printf("%s 가 도발을 사용했습니다! (1턴 동안 적의 우선 타겟)%n", caster.name());
    }
}