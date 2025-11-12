package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public interface Skill {
    String name();

    default int mpCost() { return 0; }
    default int cooldown() { return 0; }

    boolean canUse(Unit caster, Board board, List<Unit> units, Position target);
    void use(Unit caster, Board board, List<Unit> units, Position target);
}
