package game.core.entity;

import game.core.board.Position;

import java.util.Objects;

public final class Unit {
    private final String name;
    private final Role role;
    private final TeamSide side;
    private final Stats stats;
    private Position position;

    private int skillCooldownRemaining = 0;

    // 은신 지속 턴
    private int stealthTurnRemaining = 0;

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

    // 은신 여부 확인
    public boolean isStealthed() {
        return stealthTurnRemaining > 0;
    }

    // 은신 적용
    public void applyStealth(int duration) {
        stealthTurnRemaining = duration;
    }

    // 턴 종료마다 은신 감소
    public void tickStealth() {
        if (stealthTurnRemaining > 0) stealthTurnRemaining--;
    }

    public boolean isSkillReady(game.core.skill.Skill skill) {
        return skillCooldownRemaining == 0 && stats.hasMana(skill.mpCost());
    }

    public void startSkillCooldown(game.core.skill.Skill skill) {
        this.skillCooldownRemaining = Math.max(this.skillCooldownRemaining, skill.cooldown());
    }

    public void tickCooldown() {
        if (skillCooldownRemaining > 0) skillCooldownRemaining--;
    }
}