package com.barneyb.games.ripple;

import java.util.ArrayList;

public class Ripple {

    private static final int WALL = -1;
    private static final int OPEN = 0;

    public static void main(String[] args) {
        var board = parse("""
                          ┌───┬───┬───────┬───────┬───┐
                          │   │   │       │       │   │
                          │   ├───┴───┐   │   ┌───┴───┤
                          │   │       │   │   │       │
                          ├───┴───┐   ├───┤   └───┐   │
                          │       │   │   │     5 │   │
                          ├───┐   └───┴───┤   ┌───┴───┤
                          │   │     4     │ 6 │       │
                          ├───┴───┬───────┼───┴───┐   │
                          │       │       │       │ 1 │
                          ├───┐   ├───┐   │   ┌───┴───┤
                          │   │   │ 1 │   │   │       │
                          │   └───┘   ├───┤   └───┐   │
                          │     4     │   │       │   │
                          └───────────┴───┴───────┴───┘
                          """, 3);
        System.out.println(render(board, 5));
    }

    private static String render(int[][] board, int hpitch) {
        var sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            var row = board[i];
            for (int j = 0; j < row.length; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        // corner
                        sb.append(row[j] == WALL ? '+' : ' ');
                    } else {
                        // horizontal
                        sb.append((row[j] == WALL ? "-" : " ").repeat(hpitch));
                    }
                } else if (j % 2 == 0) {
                    // vertical
                    sb.append(row[j] == WALL ? '|' : ' ');
                } else if (row[j] == OPEN) {
                    sb.append(" ".repeat(hpitch));
                } else {
                    sb.append(" ".repeat(hpitch / 2));
                    sb.append(row[j]);
                    sb.append(" ".repeat(hpitch / 2));
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static int[][] parse(String board, int hpitch) {
        return board.trim().lines()
                .map(l -> {
                    var chars = new int[2 * (l.length() - 1) / (hpitch + 1) + 1];
                    var cs = l.toCharArray();
                    var c = 0;
                    chars[c++] = WALL; // edges are ALWAYS walls
                    var i = 1;
                    while (i < cs.length) {
                        i += hpitch / 2;
                        chars[c++] = cs[i++] == ' ' ? OPEN : Character.digit(cs[i - 1], 10);
                        i += hpitch / 2;
                        chars[c++] = cs[i++] == ' ' ? OPEN : WALL;
                    }
                    return chars;
                }).toArray(int[][]::new);
    }

}
