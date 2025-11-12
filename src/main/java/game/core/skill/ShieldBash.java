package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;
import game.core.service.AttackService;
import java.util.List;

public class ShieldBash implements Skill {
    @Override
    public String name() { return "Shield Bash"; }
    @Override public int mpCost() { return 1; }
    @Override public int cooldown() { return 1; }

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position target) {
        var enemy = units.stream()
                .filter(u -> !u.isDead() && u.position().equals(target) && u.side() != caster.side())
                .findFirst();
        return enemy.isPresent() &&
                caster.position().manhattanDistance(target) == 1;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position target) {
        var svc = new AttackService();
        var enemy = units.stream()
                .filter(u -> !u.isDead() && u.position().equals(target))
                .findFirst().orElseThrow();

        svc.basicAttack(caster, enemy);

        // 밀치기: caster → enemy 방향으로 한 칸
        int dx = target.x() - caster.position().x();
        int dy = target.y() - caster.position().y();
        Position pushed = Position.of(target.x() + dx, target.y() + dy);

        boolean canPush = board.isInside(pushed) &&
                units.stream().noneMatch(u -> !u.isDead() && u.position().equals(pushed));

        if (canPush) enemy.moveTo(pushed);
    }
}