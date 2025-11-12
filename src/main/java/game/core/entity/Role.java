package game.core.entity;

import game.core.skill.*;

public enum Role {
    KNIGHT(new ShieldBash()),
    ROGUE(noSkill()),
    ARCHER(new PiercingShot()),
    MAGE(new Fireball()),
    MONK(noSkill()),
    TANK(noSkill());

    private final Skill skill;

    Role(Skill skill) {
        this.skill = skill;
    }

    public Skill skill() {
        return skill;
    }

    private static Skill noSkill() {
        return new Skill() {
            @Override
            public String name() { return "None"; }

            @Override
            public boolean canUse(game.core.entity.Unit caster,
                                  game.core.board.Board board,
                                  java.util.List<game.core.entity.Unit> units,
                                  game.core.board.Position target) {
                return false; // 항상 사용 불가
            }

            @Override
            public void use(game.core.entity.Unit caster,
                            game.core.board.Board board,
                            java.util.List<game.core.entity.Unit> units,
                            game.core.board.Position target) {
            }
        };
    }
}