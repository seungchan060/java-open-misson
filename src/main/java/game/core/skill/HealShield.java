package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.List;

public final class HealShield implements Skill {

    @Override public String name() { return "Heal (Shield)"; }
    @Override public int mpCost() { return 2; }
    @Override public int cooldown() { return 2; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position target) {
        if (!board.isInside(target)) return false;
        Unit ally = findAt(units, target);
        if (ally == null || ally.side() != caster.side() || ally.isDead()) return false;
        return caster.position().manhattanDistance(target) <= 2;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position target) {
        Unit ally = findAt(units, target);
        if (ally == null) return;
        ally.applyShield(12, 2);
        System.out.printf("%s 가 %s 에게 보호막을 부여했습니다! (+12, 2턴)%n",
                caster.name(), ally.name());
    }

    private Unit findAt(List<Unit> units, Position p) {
        for (Unit u : units) {
            if (!u.isDead() && u.position().equals(p)) return u;
        }
        return null;
    }
}