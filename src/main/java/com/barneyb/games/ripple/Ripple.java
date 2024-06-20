package com.barneyb.games.ripple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.barneyb.games.ripple.Boards.SUPER_TOUGH_10x10_v1_b100_2;

public class Ripple {

    public static void main(String[] args) {
        var board = Board.parse(SUPER_TOUGH_10x10_v1_b100_2,
                                5);
        var r = new Ripple(board);
        r.solve();
        if (!board.isSolved()) {
            for (int c = 0, l = board.cellCount(); c < l; c++) {
                int v = board.getCell(c);
                if (v == Board.OPEN) {
                    System.out.printf("  %s: %s%n", c, r.candidatesByCell[c]);
                } else {
                    System.out.printf("✔ %s: %s%n", c, v);
                }
            }
        }
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
        for (int i = 1; ; i++) {
            System.out.printf("- Round %d -%s%n", i, "-".repeat(20));
            if (Stream.<Strat>of(
                    this::singleCandidate,
                    this::singleCellInCage,
                    this::visibleToAllPotentials
            ).noneMatch(s -> !board.isSolved() && s.run())) break;
        }
    }

    boolean singleCandidate() {
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

    boolean singleCellInCage() {
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

    boolean visibleToAllPotentials() {
        var didSomething = false;
        for (var cage : board.cages()) {
            var cellsByCandidate = new int[cage.length + 1][];
            var counts = new int[cage.length + 1];
            for (int c : cage) {
                if (board.getCell(c) != Board.OPEN) continue;
                for (int v : candidatesByCell[c]) {
                    var cs = cellsByCandidate[v];
                    if (cs == null)
                        cs = cellsByCandidate[v] = new int[1];
                    else if (counts[v] == cs.length)
                        cs = cellsByCandidate[v] = Arrays.copyOf(cs, cs.length * 2);
                    cs[counts[v]++] = c;
                }
            }
            for (int v = 0; v < counts.length; v++) {
                if (counts[v] < 2) continue;
                var cells = cellsByCandidate[v];
                Set<Integer> allSee = null;
                for (int c = 0; c < counts[v]; c++) {
                    var iSee = new HashSet<Integer>();
                    board.northOf(cells[c]).limit(v).forEach(iSee::add);
                    board.southOf(cells[c]).limit(v).forEach(iSee::add);
                    board.eastOf(cells[c]).limit(v).forEach(iSee::add);
                    board.westOf(cells[c]).limit(v).forEach(iSee::add);
                    if (allSee == null) allSee = iSee;
                    else allSee.removeIf(Predicate.not(iSee::contains));
                }
                assert allSee != null;
                for (int c : allSee) {
                    if (removeCandidate(c, v)) {
                        System.out.println("pair remove: " + c + " = " + v);
                        didSomething = true;
                    }
                }
            }
        }
        return didSomething;
    }

    private Hist getHist(int[] cage) {
        var hist = new Hist();
        for (int c : cage)
            hist.addAll(candidatesByCell[c]);
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

    private boolean removeCandidate(int c, int value) {
        if (candidatesByCell[c].remove(value)) {
            queue.add(c);
            return true;
        }
        return false;
    }

}
