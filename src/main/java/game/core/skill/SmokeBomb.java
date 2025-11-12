package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public class SmokeBomb implements Skill {

    @Override public String name() { return "Smoke Bomb"; }
    @Override public int mpCost() { return 2; }
    @Override public int cooldown() { return 3; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position ignored) {
        return true;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position ignored) {
        caster.applyStealth(1); // 1턴 은신
        System.out.printf("%s 연막! (1턴 동안 타겟 불가)\n", caster.name());
    }
}