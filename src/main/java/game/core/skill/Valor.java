package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public final class Valor implements Skill {

    @Override public String name() { return "Valor"; }
    @Override public int mpCost() { return 2; }
    @Override public int cooldown() { return 3; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position ignored) {
        return true;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position ignored) {
        caster.applyValor(2, 2, 0.20);
        System.out.printf("%s 가 용기를 얻었습니다! (2턴: ATK +2, 피해 -20%%)%n", caster.name());
    }
}