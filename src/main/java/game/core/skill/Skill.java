package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;

import java.util.List;

public interface Skill {
    String name();
    boolean canUse(Unit caster, Board board, List<Unit> units, Position target);
    void use(Unit caster, Board board, List<Unit> units, Position target);
}