package game.app;

import game.cli.InputView;
import game.cli.OutputView;
import game.core.board.Board;
import game.core.board.Position;
import game.core.engine.BattleEngine;
import game.core.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameApplication {
    private static final Random RANDOM = new Random();
    private record Blueprint(
            String name,
            Role role,
            TeamSide side,
            int hp,
            int atk,
            int def,
            int armor,
            int mana
    ) { }

    // 플레이어 후보 캐릭터들
    private static final List<Blueprint> PLAYER_POOL = List.of(
            new Blueprint("강철 수호자 레온",   Role.KNIGHT, TeamSide.PLAYER, 90, 12, 4, 10, 0),
            new Blueprint("폭풍 마도사 이레나", Role.MAGE,   TeamSide.PLAYER, 70, 14, 2,  5, 20),
            new Blueprint("그림자 도적 카인",   Role.ROGUE,  TeamSide.PLAYER, 65, 13, 2,  5, 10),
            new Blueprint("새벽 사수 유리",     Role.ARCHER, TeamSide.PLAYER, 60, 11, 2,  5, 15),
            new Blueprint("방패 거인 브루노",   Role.TANK,   TeamSide.PLAYER, 110, 8,  6, 15,  0),
            new Blueprint("고요한 주먹 현우",   Role.MONK,   TeamSide.PLAYER, 75,  9, 3,  8, 20)
    );

    // 적 후보 캐릭터들
    private static final List<Blueprint> ENEMY_POOL = List.of(
            new Blueprint("검은 기사 드레이크",  Role.KNIGHT, TeamSide.ENEMY, 90, 11, 4, 10, 0),
            new Blueprint("저주술사 모르가",     Role.MAGE,   TeamSide.ENEMY, 65, 13, 2,  5, 25),
            new Blueprint("암살자 니힐",         Role.ROGUE,  TeamSide.ENEMY, 60, 12, 2,  5, 10),
            new Blueprint("독시의 궁수 사일런",  Role.ARCHER, TeamSide.ENEMY, 55, 10, 2,  5, 15),
            new Blueprint("쇳덩이 골렘 크르룩",  Role.TANK,   TeamSide.ENEMY, 115, 8,  7, 18, 0),
            new Blueprint("타락한 수도승 제론",  Role.MONK,   TeamSide.ENEMY, 70,  9, 3,  8, 20)
    );

    public static void main(String[] args) {
        Board board = new Board(5, 4);

        List<Unit> units = new ArrayList<>();
        setupInitialUnitsRandom(board, units);

        OutputView out = new OutputView();
        InputView in = new InputView();

        BattleEngine engine = new BattleEngine(board, units, out, in);
        System.out.println("Tactics Game 시작!");
        engine.run();
    }

    private static void setupInitialUnitsRandom(Board board, List<Unit> units) {
        int playerCount = 3;
        int enemyCount  = 3;

        // 플레이어 유닛 뽑기
        List<Blueprint> playerCandidates = new ArrayList<>(PLAYER_POOL);
        Collections.shuffle(playerCandidates, RANDOM);
        for (int i = 0; i < playerCount && i < playerCandidates.size(); i++) {
            Blueprint bp = playerCandidates.get(i);
            Position pos = randomFreePosition(board, units, TeamSide.PLAYER);
            units.add(spawn(bp, pos));
        }

        // 적 유닛 뽑기
        List<Blueprint> enemyCandidates = new ArrayList<>(ENEMY_POOL);
        Collections.shuffle(enemyCandidates, RANDOM);
        for (int i = 0; i < enemyCount && i < enemyCandidates.size(); i++) {
            Blueprint bp = enemyCandidates.get(i);
            Position pos = randomFreePosition(board, units, TeamSide.ENEMY);
            units.add(spawn(bp, pos));
        }
    }

    private static Unit spawn(Blueprint bp, Position pos) {
        return new Unit(
                bp.name(),
                bp.role(),
                bp.side(),
                Stats.of(bp.hp(), bp.atk(), bp.def(), bp.armor(), bp.mana()),
                pos
        );
    }

    private static Position randomFreePosition(Board board, List<Unit> units, TeamSide side) {
        while (true) {
            int x = RANDOM.nextInt(board.width());

            int y;
            if (side == TeamSide.PLAYER) {
                int base = board.height() - 2;
                y = base + RANDOM.nextInt(2);
            } else {
                y = RANDOM.nextInt(2);
            }

            Position p = Position.of(x, y);
            if (!board.isInside(p)) {
                continue;
            }

            boolean occupied = units.stream()
                    .anyMatch(u -> !u.isDead() && u.position().equals(p));

            if (!occupied) {
                return p;
            }
        }
    }
}