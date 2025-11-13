package game.core.entity;

import game.core.skill.*;
import java.util.List;

/** 직업과 보유 스킬 목록 매핑 (여러 스킬 지원) */
public enum Role {

    KNIGHT(new ShieldBash()),
    ROGUE(new Backstab(), new SmokeBomb()),
    ARCHER(new PiercingShot()),
    MAGE(new Fireball()),
    MONK(),
    TANK(new Taunt());

    private final List<Skill> skills;

    Role(Skill... skills) {
        this.skills = List.of(skills);
    }

    public List<Skill> skills() {
        return skills;
    }
}