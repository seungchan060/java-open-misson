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

    // 은신(연막)
    private int stealthTurnRemaining = 0;
    // 도발(탱커)
    private int tauntTurnRemaining = 0;
    // 바디블록(보호/넉백저항)
    private int bodyBlockTurnRemaining = 0;

    // Valor(용기) 버프: 임시 공격력 증가 + 피해 경감
    private int valorTurnRemaining = 0;
    private int valorAtkBonus = 0;          // 가산 공격력
    private double valorDamageReduce = 0.0;  // 0.2 = 20% 경감

    // Heal 대체: 보호막(Shield) (지속 턴 동안 피해 흡수)
    private int shieldTurnRemaining = 0;
    private int shieldAmount = 0;

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

    // ----- Valor (임시 버프) -----
    public boolean hasValor() { return valorTurnRemaining > 0; }
    public int valorAtkBonus() { return hasValor() ? valorAtkBonus : 0; }
    public double valorDamageReduce() { return hasValor() ? valorDamageReduce : 0.0; }
    public void applyValor(int duration, int atkBonus, double damageReduce) {
        this.valorTurnRemaining = duration;
        this.valorAtkBonus = Math.max(0, atkBonus);
        this.valorDamageReduce = Math.max(0.0, Math.min(0.9, damageReduce));
    }
    public void tickValor() {
        if (valorTurnRemaining > 0) valorTurnRemaining--;
        if (valorTurnRemaining == 0) {
            valorAtkBonus = 0;
            valorDamageReduce = 0.0;
        }
    }

    public boolean hasShield() { return shieldTurnRemaining > 0 && shieldAmount > 0; }
    public int shieldAmount() { return hasShield() ? shieldAmount : 0; }
    public void applyShield(int amount, int duration) {
        this.shieldAmount = Math.max(0, amount);
        this.shieldTurnRemaining = duration;
    }

    public int absorbDamage(int incoming) {
        if (incoming <= 0) return 0;
        if (!hasShield()) return incoming;
        int absorbed = Math.min(shieldAmount, incoming);
        shieldAmount -= absorbed;
        return incoming - absorbed;
    }
    public void tickShield() {
        if (shieldTurnRemaining > 0) shieldTurnRemaining--;
        if (shieldTurnRemaining == 0) shieldAmount = 0;
    }

    public boolean isSkillReady(game.core.skill.Skill skill) {
        return skillCooldownRemaining == 0 && stats.hasMana(skill.mpCost());
    }
    public void startSkillCooldown(game.core.skill.Skill skill) {
        this.skillCooldownRemaining = Math.max(this.skillCooldownRemaining, skill.cooldown());
    }
    public void tickCooldown() { if (skillCooldownRemaining > 0) skillCooldownRemaining--; }
}