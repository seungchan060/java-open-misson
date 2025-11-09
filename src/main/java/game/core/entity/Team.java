package game.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Team {
    public static final int MAX_FIELD = 7;
    public static final int MAX_BENCH = 4;

    private final TeamSide side;
    private final List<Unit> field = new ArrayList<>();
    private final List<Unit> bench = new ArrayList<>();

    public Team(TeamSide side) { this.side = side; }

    public TeamSide side() { return side; }
    public List<Unit> field() { return Collections.unmodifiableList(field); }
    public List<Unit> bench() { return Collections.unmodifiableList(bench); }

    public boolean canAddToField() { return field.size() < MAX_FIELD; }
    public boolean canAddToBench() { return bench.size() < MAX_BENCH; }

    public void addToField(Unit c) {
        if (!canAddToField()) throw new IllegalStateException("필드가 가득 찼습니다.");
        if (c.side() != side) throw new IllegalArgumentException("팀 사이드가 다릅니다.");
        field.add(c);
    }

    public void addToBench(Unit c) {
        if (!canAddToBench()) throw new IllegalStateException("벤치가 가득 찼습니다.");
        if (c.side() != side) throw new IllegalArgumentException("팀 사이드가 다릅니다.");
        bench.add(c);
    }
}