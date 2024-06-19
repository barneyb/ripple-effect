package com.barneyb.games.ripple;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class Ripple {

    public static void main(String[] args) {
        var board = Board.parse("""
                                ┌─────┬─────┬───────────┬───────────┬─────┐
                                │     │     │           │           │     │
                                │     ├─────┴─────┐     │     ┌─────┴─────┤
                                │     │           │     │     │           │
                                ├─────┴─────┐     ├─────┤     └─────┐     │
                                │           │     │     │        5  │     │
                                ├─────┐     └─────┴─────┤     ┌─────┴─────┤
                                │     │        4        │  6  │           │
                                ├─────┴─────┬───────────┼─────┴─────┐     │
                                │           │           │           │  1  │
                                ├─────┐     ├─────┐     │     ┌─────┴─────┤
                                │     │     │  1  │     │     │           │
                                │     └─────┘     ├─────┤     └─────┐     │
                                │        4        │     │           │     │
                                └─────────────────┴─────┴───────────┴─────┘""",
                                5);
        new Ripple(board);
        System.out.println(board.toString(5));
    }

    private final Board board;
    private final Map<Integer, Set<Integer>> candidatesByCell = new HashMap<>();
    private final Map<Integer, int[]> cagesByCell = new HashMap<>();
    private final NavigableSet<Integer> queue = new TreeSet<>();

    public Ripple(Board board) {
        this.board = board;
        initialize();
        while (!queue.isEmpty()) {
            var cell = queue.first();
            queue.remove(cell);
            var cs = candidatesByCell.get(cell);
            if (cs.size() != 1) continue;
            int value = cs.iterator().next();
            board.setCell(cell, value);
            System.out.printf("set %d to %d%n", cell, value);
            // clear the cage
            for (int c : cagesByCell.get(cell)) {
                System.out.printf("CAGE remove %d from %d%n", value, c);
                if (c != cell) {
                    removeCandidate(c, value);
                }
            }
            board.northOf(cell).limit(value).forEach(c -> {
                System.out.printf("NORTH remove %d from %d%n", value, c);
                removeCandidate(c, value);
            });
            board.southOf(cell).limit(value).forEach(c -> {
                System.out.printf("SOUTH remove %d from %d%n", value, c);
                removeCandidate(c, value);
            });
            board.eastOf(cell).limit(value).forEach(c -> {
                System.out.printf("EAST remove %d from %d%n", value, c);
                removeCandidate(c, value);
            });
            board.westOf(cell).limit(value).forEach(c -> {
                System.out.printf("WEST remove %d from %d%n", value, c);
                removeCandidate(c, value);
            });
        }
    }

    private void initialize() {
        for (int[] cage : board.cages()) {
            for (int cell : cage) {
                cagesByCell.put(cell, cage);
                var val = board.getCell(cell);
                candidatesByCell.put(cell, val == Board.OPEN
                        ? upto(cage.length)
                        : Collections.singleton(val));
            }
        }
        queue.addAll(candidatesByCell.keySet());
    }

    private Set<Integer> upto(int n) {
        var ints = new HashSet<Integer>();
        for (int i = 1; i <= n; i++) ints.add(i);
        return ints;
    }

    private void removeCandidate(int c, int value) {
        if (candidatesByCell.get(c).remove(value)) {
            queue.add(c);
        }
    }

}
