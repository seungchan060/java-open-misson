package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public final class SmokeBomb implements Skill {

    @Override
    public String name() { return "연막탄 (Smoke Bomb)"; }

    @Override
    public int mpCost() { return 2; }
    @Override
    public int cooldown() { return 3; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position target) {
        return true;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position target) {
        caster.applyStealth(1);
        System.out.println(caster.name() + " 은신 상태가 되었습니다! (1턴)");
    }
}