package game.cli;

import game.core.board.Board;
import game.core.board.Position;
import game.core.entity.TeamSide;
import game.core.entity.Unit;

import java.util.Comparator;
import java.util.List;

public final class OutputView {
    public void printBoard(Board board, List<Unit> units) {
        System.out.println();
        for (int y = 0; y < board.height(); y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < board.width(); x++) {
                Position p = Position.of(x, y);

                Unit u = findUnitAt(units, p);
                if (u == null || u.isDead()) {
                    line.append(". ");
                } else {
                    String symbol = roleSymbol(u);
                    if (u.side() == TeamSide.ENEMY) {
                        line.append(symbol.toLowerCase()).append(" ");
                    } else {
                        line.append(symbol).append(" ");
                    }
                }
            }
            System.out.println(line);
        }
        System.out.println();
    }

    public void printStatus(List<Unit> units) {
        System.out.println("=== UNIT STATUS ===");

        List<Unit> sorted = units.stream()
                .sorted(Comparator
                        .comparing(Unit::side)
                        .thenComparing(u -> u.name()))
                .toList();

        for (Unit u : sorted) {
            if (u.isDead()) {
                System.out.printf("☠️  [%s] %s (DEAD)%n",
                        u.side(), u.name());
                continue;
            }

            String role = u.role().name();
            int hp = u.stats().hp();
            int mp = u.stats().mana();
            String pos = u.position().toString();

            StringBuilder status = new StringBuilder();

            if (u.isStealthed())   status.append("🕶 ");
            if (u.isTaunting())    status.append("🛡 ");
            if (u.isBodyBlocking())status.append("🧱 ");
            if (u.hasValor())      status.append("💪 ");
            if (u.shieldAmount() > 0) status.append("✨ ");

            System.out.printf("[%s] %-8s (%s) HP:%3d MP:%2d  Pos:%-6s  %s%n",
                    u.side(), u.name(), role, hp, mp, pos, status);
        }
        System.out.println();
    }

    private Unit findUnitAt(List<Unit> units, Position p) {
        for (Unit u : units) {
            if (!u.isDead() && u.position().equals(p)) {
                return u;
            }
        }
        return null;
    }

    private String roleSymbol(Unit u) {
        return switch (u.role()) {
            case KNIGHT -> "K";
            case MAGE   -> "M";
            case ROGUE  -> "R";
            case ARCHER -> "A";
            case TANK   -> "T";
            case MONK   -> "O";
        };
    }
}