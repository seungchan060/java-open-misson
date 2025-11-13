package game.core.entity;

import game.core.board.Position;

import java.util.Objects;

public final class Unit {
    private final String name;
    private final Role role;
    private final TeamSide side;
    private final Stats stats;
    private Position position;

    // 스킬 쿨다운
    private int skillCooldownRemaining = 0;

    // 은신 지속(연막탄)
    private int stealthTurnRemaining = 0;

    // 도발 지속(탱커 Taunt)
    private int tauntTurnRemaining = 0;

    // 바디블록 지속(보호/넉백저항)
    private int bodyBlockTurnRemaining = 0;

    public Unit(String name, Role role, TeamSide side, Stats stats, Position position) {
        this.name = Objects.requireNonNull(name);
        this.role = Objects.requireNonNull(role);
        this.side = Objects.requireNonNull(side);
        this.stats = Objects.requireNonNull(stats);
        this.position = Objects.requireNonNull(position);
    }

    public String name() { return name; }
    public Role role() { return role; }
    public TeamSide side() { return side; }
    public Stats stats() { return stats; }
    public Position position() { return position; }

    public void moveTo(Position p) {
        this.position = Objects.requireNonNull(p);
    }

    public boolean isDead() { return stats.isDead(); }

    // ----- Stealth (은신) -----
    public boolean isStealthed() { return stealthTurnRemaining > 0; }
    public void applyStealth(int duration) { stealthTurnRemaining = duration; }
    public void tickStealth() { if (stealthTurnRemaining > 0) stealthTurnRemaining--; }

    // ----- Taunt (도발) -----
    public boolean isTaunting() { return tauntTurnRemaining > 0; }
    public void applyTaunt(int duration) { tauntTurnRemaining = duration; }
    public void tickTaunt() { if (tauntTurnRemaining > 0) tauntTurnRemaining--; }

    // ----- Body Block (보호/넉백저항) -----
    public boolean isBodyBlocking() { return bodyBlockTurnRemaining > 0; }
    public void applyBodyBlock(int duration) { bodyBlockTurnRemaining = duration; }
    public void tickBodyBlock() { if (bodyBlockTurnRemaining > 0) bodyBlockTurnRemaining--; }

    // ----- Skill cooldown -----
    public boolean isSkillReady(game.core.skill.Skill skill) {
        return skillCooldownRemaining == 0 && stats.hasMana(skill.mpCost());
    }
    public void startSkillCooldown(game.core.skill.Skill skill) {
        this.skillCooldownRemaining = Math.max(this.skillCooldownRemaining, skill.cooldown());
    }
    public void tickCooldown() { if (skillCooldownRemaining > 0) skillCooldownRemaining--; }
}