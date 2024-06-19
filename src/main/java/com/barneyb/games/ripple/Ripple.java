package com.barneyb.games.ripple;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

import static com.barneyb.games.ripple.Boards.TOUGH_8x8_v1_b9_7;

public class Ripple {

    public static void main(String[] args) {
        var board = Board.parse(TOUGH_8x8_v1_b9_7,
                                5);
        new Ripple(board).solve();
        System.out.println(board.toString(5));
    }

    private final Board board;
    private final Set<Integer>[] candidatesByCell;
    private final int[][] cagesByCell;
    private final Queue<Integer> queue = new LinkedList<>();

    public Ripple(Board board) {
        this.board = board;
        int cellCount = board.cellCount();
        cagesByCell = new int[cellCount][];
        //noinspection unchecked
        candidatesByCell = new Set[cellCount];
        initialize();
    }

    @FunctionalInterface
    private interface Strat {

        boolean run();

    }

    void solve() {
        //noinspection StatementWithEmptyBody
        while (Stream.<Strat>of(
                this::cancels,
                this::singletons,
                this::pairCorners
        ).anyMatch(Strat::run)) {}
    }

    boolean cancels() {
        var didSomething = false;
        while (!queue.isEmpty()) {
            var cell = queue.remove();
            if (board.getCell(cell) != Board.OPEN) continue;
            var cs = candidatesByCell[cell];
            if (cs.size() != 1) continue;
            int value = cs.iterator().next();
            System.out.println("one candidate: " + cell + " = " + value);
            lockCell(cell, value);
            didSomething = true;
        }
        return didSomething;
    }

    boolean singletons() {
        var didSomething = false;
        for (var cage : board.cages()) {
            var hist = getHist(cage);
            for (int v : hist.withCount(1)) {
                for (int c : cage) {
                    if (candidatesByCell[c].contains(v)) {
                        System.out.println("one cell in cage: " + c + " = " + v);
                        lockCell(c, v);
                        didSomething = true;
                    }
                }
            }
        }
        return didSomething;
    }

    boolean pairCorners() {
        var didSomething = new AtomicBoolean();
        for (var cage : board.cages()) {
            var hist = getHist(cage);
            for (int v : hist.withCount(2)) {
                int a = -1, b = -1;
                for (int c : cage) {
                    if (candidatesByCell[c].contains(v)) {
                        if (a < 0) a = c;
                        else b = c;
                    }
                }
                assert a > 0;
                assert b > 0;
                var aSees = new HashSet<Integer>();
                board.northOf(a).limit(v).forEach(aSees::add);
                board.southOf(a).limit(v).forEach(aSees::add);
                board.eastOf(a).limit(v).forEach(aSees::add);
                board.westOf(a).limit(v).forEach(aSees::add);
                IntConsumer work = c -> {
                    System.out.println("pair remove: " + c + " = " + v);
                    removeCandidate(c, v);
                    didSomething.set(true);
                };
                board.northOf(b).limit(v).filter(aSees::contains).forEach(work);
                board.southOf(b).limit(v).filter(aSees::contains).forEach(work);
                board.eastOf(b).limit(v).filter(aSees::contains).forEach(work);
                board.westOf(b).limit(v).filter(aSees::contains).forEach(work);
            }
        }
        return didSomething.get();
    }

    private Hist getHist(int[] cage) {
        var hist = new Hist();
        for (int c : cage) {
            Set<Integer> ccs = candidatesByCell[c];
            if (ccs.size() > 1)
                hist.addAll(ccs);
        }
        return hist;
    }

    private void lockCell(Integer cell, int value) {
        removeCandidate(cell, value);
        board.setCell(cell, value);
        IntConsumer remove = c -> removeCandidate(c, value);
        for (int c : cagesByCell[cell])
            if (c != cell)
                remove.accept(c);
        board.northOf(cell).limit(value).forEach(remove);
        board.southOf(cell).limit(value).forEach(remove);
        board.eastOf(cell).limit(value).forEach(remove);
        board.westOf(cell).limit(value).forEach(remove);
    }

    private void initialize() {
        Map<Integer, Integer> fixed = new HashMap<>();
        for (int[] cage : board.cages()) {
            for (int cell : cage) {
                cagesByCell[cell] = cage;
                var val = board.getCell(cell);
                if (val == Board.OPEN) {
                    candidatesByCell[cell] = upto(cage.length);
                    this.queue.add(cell);
                } else {
                    candidatesByCell[cell] = Collections.emptySet();
                    fixed.put(cell, val);
                }
            }
        }
        fixed.forEach((c, v) -> {
            System.out.println("init lock: " + c + " = " + v);
            lockCell(c, v);
        });
    }

    private Set<Integer> upto(int n) {
        var ints = new HashSet<Integer>();
        for (int i = 1; i <= n; i++) ints.add(i);
        return ints;
    }

    private void removeCandidate(int c, int value) {
        if (candidatesByCell[c].remove(value)) {
            queue.add(c);
        }
    }

}
