package game.core.skill;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.List;

public class Backstab implements Skill {

    @Override public String name() { return "Backstab"; }
    @Override public int mpCost() { return 2; }   // 필요 MP
    @Override public int cooldown() { return 2; } // 재사용 대기 2턴

    @Override
    public boolean canUse(Unit caster, Board board, List<Unit> units, Position target) {
        // 타겟 칸에 적 유닛이 있어야 함
        Unit enemy = findAt(units, target);
        if (enemy == null || enemy.side() == caster.side()) return false;

        // 인접
        if (caster.position().manhattanDistance(target) != 1) return false;

        // 보드 안쪽
        return board.isInside(target);
    }

    @Override
    public void use(Unit caster, Board board, List<Unit> units, Position target) {
        Unit enemy = findAt(units, target);
        if (enemy == null) return;

        // 기본 피해
        int base = caster.stats().atk();

        // 등 뒤 판정
        // 적이 ENEMY면 아래(y+1)가 등 뒤
        // 적이 PLAYER면 위(y-1)가 등 뒤
        boolean isBehind = false;
        if (enemy.side() == TeamSide.ENEMY) {
            // 플레이어가 적의 바로 아래칸에 있으면 등 뒤
            isBehind = caster.position().equals(Position.of(enemy.position().x(), enemy.position().y() + 1));
        } else { // enemy == PLAYER
            isBehind = caster.position().equals(Position.of(enemy.position().x(), enemy.position().y() - 1));
        }

        int bonus = isBehind ? 6 : 0; // 등 뒤 보너스 피해
        int totalRaw = base + bonus;

        // MP는 BattleEngine에서 이미 차감/쿨다운 적용함
        enemy.stats().applyDamage(totalRaw);

        if (isBehind) {
            System.out.printf("%s 가 %s 의 등 뒤를 찔렀습니다! (+%d 보너스)  (남은 HP: %d)%n",
                    caster.name(), enemy.name(), bonus, enemy.stats().hp());
        } else {
            System.out.printf("%s 가 %s 를 찔렀습니다. (남은 HP: %d)%n",
                    caster.name(), enemy.name(), enemy.stats().hp());
        }

        if (enemy.isDead()) {
            System.out.printf("%s 사망!%n", enemy.name());
        }
    }

    private Unit findAt(List<Unit> units, Position p) {
        for (Unit u : units) {
            if (!u.isDead() && u.position().equals(p)) return u;
        }
        return null;
    }
}