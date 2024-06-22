package com.barneyb.games.ripple;

import java.io.PrintStream;
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

import static java.util.Collections.singleton;

public class ByLogic extends BaseRipple {

    private final Set<Integer>[] candidatesByCell;
    private final int[][] cagesByCell;
    private final Queue<Integer> queue = new LinkedList<>();

    public ByLogic(Board board) {
        this(System.out, board);
    }

    public ByLogic(PrintStream out,
                   Board board) {
        super(out, board);
        int cellCount = board.cellCount();
        cagesByCell = new int[cellCount][];
        //noinspection unchecked
        candidatesByCell = new Set[cellCount];
        initialize();
    }

    @FunctionalInterface
    private interface Logic {

        /**
         * Attempt to apply logic, and return whether anything happened.
         */
        boolean attempt();

    }

    @Override
    void solveInternal() {
        for (int i = 1; ; i++) {
            out.printf("- Round %d -%s%n", i, "-".repeat(20));
            if (Stream.<Logic>of(
                    this::singleCandidate,
                    this::singleCellInCage,
                    this::visibleToAllPotentials
            ).noneMatch(Logic::attempt)) break;
            if (board.isSolved()) break;
            if (i > 100) {
                out.println("Cowardly refusing to continue past " + i + " rounds");
                return;
            }
        }
    }

    boolean singleCandidate() {
        var didSomething = false;
        while (!queue.isEmpty()) {
            var cell = queue.remove();
            if (!board.isOpen(cell)) continue;
            var cs = candidatesByCell[cell];
            if (cs.size() != 1) continue;
            int value = cs.iterator().next();
            out.println("one candidate: " + cell + " = " + value);
            lockCell(cell, value);
            didSomething = true;
        }
        return didSomething;
    }

    boolean singleCellInCage() {
        var didSomething = false;
        for (var cage : board.cages()) {
            var hist = new Hist();
            for (int c : cage)
                if (board.isOpen(c))
                    hist.addAll(candidatesByCell[c]);
            for (int v : hist.withCount(1)) {
                for (int c : cage) {
                    if (candidatesByCell[c].contains(v)) {
                        out.println("one cell in cage: " + c + " = " + v);
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
                if (!board.isOpen(c)) continue;
                for (int v : candidatesByCell[c]) {
                    var cs = cellsByCandidate[v];
                    if (cs == null)
                        cs = cellsByCandidate[v] = new int[2];
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
                        String vizTo = Arrays.toString(Arrays.copyOf(cells, counts[v]));
                        out.println("visible to " + vizTo + ": " + c + " != " + v);
                        didSomething = true;
                    }
                }
            }
        }
        return didSomething;
    }

    private void lockCell(Integer cell, int value) {
        candidatesByCell[cell] = singleton(value);
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
            out.println("init lock: " + c + " = " + v);
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

    @Override
    public void printState() {
        for (int c = 0, l = board.cellCount(); c < l; c++) {
            int v = board.getCell(c);
            if (v == Board.OPEN) {
                out.printf("  %s: %s%n", c, candidatesByCell[c]);
            } else {
                out.printf("✔ %s: %s%n", c, v);
            }
        }
    }

}
