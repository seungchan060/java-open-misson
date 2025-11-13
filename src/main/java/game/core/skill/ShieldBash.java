package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.Unit;
import game.core.service.AttackService;
import game.core.service.MovementService;

import java.util.List;

public final class ShieldBash implements Skill {
    @Override public String name() { return "Shield Bash"; }
    @Override public int mpCost() { return 1; }
    @Override public int cooldown() { return 1; }

    private final AttackService atk = new AttackService();
    private final MovementService move = new MovementService();

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position target) {
        for (Unit u : units) {
            if (!u.isDead() && u.position().equals(target)) {
                return caster.position().manhattanDistance(target) == 1;
            }
        }
        return false;
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position target) {
        Unit victim = units.stream()
                .filter(u -> !u.isDead() && u.position().equals(target))
                .findFirst().orElse(null);
        if (victim == null) return;

        atk.basicAttack(caster, victim);

        if (victim.isBodyBlocking()) {
            System.out.printf("%s 는 단단히 버텼습니다! (넉백 저항)%n", victim.name());
            return;
        }

        int dx = Integer.compare(victim.position().x(), caster.position().x());
        int dy = Integer.compare(victim.position().y(), caster.position().y());
        Position pushTo = Position.of(victim.position().x() + dx, victim.position().y() + dy);

        boolean inside = board.isInside(pushTo);
        boolean occupied = units.stream().anyMatch(u -> !u.isDead() && u.position().equals(pushTo));
        if (inside && !occupied) {
            victim.moveTo(pushTo);
            System.out.printf("%s 를 밀어냈습니다! → (%d,%d)%n", victim.name(), pushTo.x(), pushTo.y());
        }
    }
}