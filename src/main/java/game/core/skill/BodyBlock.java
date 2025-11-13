package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public final class BodyBlock implements Skill {

    @Override public String name() { return "Body Block"; }
    @Override public int mpCost() { return 2; }
    @Override public int cooldown() { return 2; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position ignored) {
        return true;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position ignored) {
        caster.applyBodyBlock(1); // 1턴 지속
        System.out.printf("%s 가 몸으로 막아섭니다! (주변 아군 보호, 넉백 저항)%n", caster.name());
    }
}