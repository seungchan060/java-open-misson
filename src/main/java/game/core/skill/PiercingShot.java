package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.TeamSide;
import game.core.entity.Unit;
import game.core.service.AttackService;

import java.util.List;

public class PiercingShot implements Skill {

    @Override
    public String name() { return "Piercing Shot"; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position target) {
        // target이 caster와 같은 직선상인지 확인
        return caster.position().x() == target.x() || caster.position().y() == target.y();
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position target) {
        System.out.println(caster.name() + " → 관통 사격!");

        var atk = new AttackService();
        int dx = Integer.compare(target.x(), caster.position().x());
        int dy = Integer.compare(target.y(), caster.position().y());

        Position p = caster.position();
        while (true) {
            p = Position.of(p.x() + dx, p.y() + dy);
            if (!board.isInside(p)) break;

            for (Unit u : units) {
                if (!u.isDead() && u.side() != caster.side() && u.position().equals(p)) {
                    atk.basicAttack(caster, u);
                }
            }
        }
    }
}