package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;
import game.core.entity.TeamSide;
import game.core.service.AttackService;
import java.util.List;

public class Fireball implements Skill {

    @Override
    public String name() { return "Fireball"; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position target) {
        return board.isInside(target);
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position target) {
        System.out.println(caster.name() + " → 화염구 시전!");

        var atk = new AttackService();
        List<Position> aoe = List.of(
                target,
                Position.of(target.x(), target.y() - 1),
                Position.of(target.x(), target.y() + 1),
                Position.of(target.x() - 1, target.y()),
                Position.of(target.x() + 1, target.y())
        );

        for (Position p : aoe) {
            for (Unit u : units) {
                if (!u.isDead() && u.side() != caster.side() && u.position().equals(p)) {
                    atk.basicAttack(caster, u);
                }
            }
        }
    }
}