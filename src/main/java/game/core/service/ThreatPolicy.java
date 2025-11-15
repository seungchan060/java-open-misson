package game.core.service;

import game.core.entity.Role;
import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.Comparator;
import java.util.List;

final class ThreatPolicy {

    List<Unit> candidates(List<Unit> all) {
        return all.stream()
                .filter(u -> !u.isDead() && u.side() == TeamSide.PLAYER)
                .filter(u -> u.isTaunting() || !u.isStealthed())
                .toList();
    }

    // 점수 계산 (값이 클수록 우선)
    double score(Unit attacker, Unit target) {
        int dist = attacker.position().manhattanDistance(target.position());
        boolean adjacent = (dist == 1);

        double s = 0.0;

        // 도발이면 최우선 (다른 요소 전부 무시할 정도의 가중치)
        if (target.isTaunting()) {
            s += 10_000;
        }

        // 역할 가중치 — "누가 더 위험한가?"
        //  → 이 값이 거리/인접 보너스보다 훨씬 크게 설정되어 있음
        s += switch (target.role()) {
            case MONK   -> 5_000;  // 힐러 / 버퍼
            case MAGE   -> 4_000;  // 폭딜
            case ARCHER -> 3_000;  // 원거리 딜러
            case ROGUE  -> 2_000;  // 암살자
            case KNIGHT -> 1_000;  // 근접 딜탱
            case TANK   ->   500;  // 순수 탱커
        };

        // 거리 패널티 (멀수록 손해)
        //  - 칸당 -50 정도만 줘서, 역할 점수보다 훨씬 작은 비중
        s -= dist * 50.0;

        // 인접 보너스 (바로 공격 가능하면 약간 가산)
        if (adjacent) {
            s += 80.0;
        }

        // 체력이 낮을수록 약간 가산
        s += Math.max(0.0, 50.0 - target.stats().hp() * 2.0);

        // 보호막/발로어가 있으면 약간 감점 (딜이 덜 박히니까)
        if (target.shieldAmount() > 0) s -= 120.0;
        if (target.hasValor())         s -= 100.0;

        return s;
    }

    // 최고 점수 선택 (동점이면 HP 낮은 순 → 더 가까운 순)
    Unit bestTarget(Unit attacker, List<Unit> pool) {
        return pool.stream()
                .max(
                        Comparator
                                .<Unit>comparingDouble(t -> score(attacker, t))
                                .thenComparingInt(t -> t.stats().hp()) // hp 낮을수록 우선
                                .thenComparingInt(t -> attacker.position().manhattanDistance(t.position()))
                )
                .orElse(null);
    }
}