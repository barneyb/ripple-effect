package com.barneyb.games.ripple;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @param board half-pitch representation of the board
 * @param cages array of array of cell indexes w/in the same cage
 */
public record Board(int[][] board, int[][] cages) {

    private static final int WALL = -1;
    public static final int OPEN = 0;

    private record Cell(int r, int c) {}

    public static Board of(int[][] board) {
        int w = board[0].length / 2;
        int h = board.length / 2;
        var cages = new int[10][];
        var cageCount = 0;
        var cells = new Cell[w * h];
        for (int r = 1; r < board.length; r += 2) {
            for (int c = 1; c < board[r].length; c += 2) {
                cells[(r / 2) * w + (c / 2)] = new Cell(r, c);
            }
        }
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == null) continue;
            var cage = new int[9];
            var n = addCell(board, cells, cage, 0, i);
            if (cageCount == cages.length) cages = Arrays.copyOf(cages, cageCount * 2);
            cages[cageCount++] = Arrays.copyOf(cage, n);
        }
        return new Board(board, Arrays.copyOf(cages, cageCount));
    }

    private static int addCell(int[][] board, Cell[] cells, int[] cage, int n, int i) {
        var cell = cells[i];
        if (cell == null) return n;
        cells[i] = null;
        cage[n++] = i;
        int w = board[0].length / 2;
        if (board[cell.r][cell.c - 1] == OPEN) n = addCell(board, cells, cage, n, i - 1);
        if (board[cell.r - 1][cell.c] == OPEN) n = addCell(board, cells, cage, n, i - w);
        if (board[cell.r][cell.c + 1] == OPEN) n = addCell(board, cells, cage, n, i + 1);
        if (board[cell.r + 1][cell.c] == OPEN) n = addCell(board, cells, cage, n, i + w);
        return n;
    }

    public static Board parse(String ascii, int hpitch) {
        int lpad = (hpitch - 1) / 2;
        int rpad = hpitch - lpad - 1;
        int[][] grid = ascii.trim().lines()
                .map(String::toCharArray)
                .map(cs -> {
                    var chars = new int[2 * (cs.length - 1) / (hpitch + 1) + 1];
                    var c = 0;
                    chars[c++] = WALL; // edges are ALWAYS walls
                    var i = 1;
                    while (i < cs.length) {
                        i += lpad;
                        chars[c++] = cs[i++] == ' ' ? OPEN : Character.digit(cs[i - 1], 10);
                        i += rpad;
                        chars[c++] = cs[i++] == ' ' ? OPEN : WALL;
                    }
                    return chars;
                }).toArray(int[][]::new);
        return Board.of(grid);
    }

    public int width() {
        return board[0].length / 2;
    }

    public int height() {
        return board.length / 2;
    }

    public int cellCount() {
        return width() * height();
    }

    public int getCell(int cell) {
        return board[toRow(cell)][toCol(cell)];
    }

    public boolean isOpen(int cell) {
        return getCell(cell) == OPEN;
    }

    private int toCol(int cell) {
        return cell % width() * 2 + 1;
    }

    private int toRow(int cell) {
        return cell / width() * 2 + 1;
    }

    public void setCell(int cell, int value) {
        int r = toRow(cell);
        int c = toCol(cell);
        if (board[r][c] != OPEN && board[r][c] != value)
            throw new IllegalStateException(String.format(
                    "Cell %s is already set to %s, can't change to %s",
                    cell,
                    board[r][c],
                    value));
        board[r][c] = value;
    }

    public IntStream northOf(int cell) {
        return downTo(cell, width(), 0);
    }

    private IntStream downTo(int anchor, int step, int min) {
        return IntStream.iterate(anchor - step,
                                 n -> n >= min,
                                 n -> n - step);
    }

    public IntStream southOf(int cell) {
        int w = width();
        return upTo(cell, w, w * height());
    }

    private IntStream upTo(int cell, int step, int max) {
        return IntStream.iterate(cell + step,
                                 n -> n < max,
                                 n -> n + step);
    }

    public IntStream eastOf(int cell) {
        int w = width();
        return upTo(cell, 1, cell + w - (cell % w));
    }

    public IntStream westOf(int cell) {
        return downTo(cell, 1, cell - cell % width());
    }

    public boolean isSolved() {
        for (int c = cellCount() - 1; c >= 0; c--)
            if (getCell(c) == OPEN) return false;
        return true;
    }

    public String toString(int hpitch) {
        int lpad = (hpitch - 1) / 2;
        int rpad = hpitch - lpad - 1;
        var sb = new StringBuilder();
        for (int r = 0; r < board.length; r++) {
            if (r > 0) sb.append('\n');
            var row = board[r];
            for (int c = 0; c < row.length; c++) {
                if (r % 2 == 0) {
                    if (c % 2 == 0) {
                        // corner
                        sb.append(cornerChar(r, c));
                    } else {
                        // horizontal
                        sb.append((row[c] == WALL ? "─" : " ").repeat(hpitch));
                    }
                } else if (c % 2 == 0) {
                    // vertical
                    sb.append(row[c] == WALL ? '│' : ' ');
                } else if (row[c] == OPEN) {
                    sb.append(" ".repeat(hpitch));
                } else {
                    sb.append(" ".repeat(lpad));
                    sb.append(row[c]);
                    sb.append(" ".repeat(rpad));
                }
            }
        }
        return sb.toString();
    }

    private char cornerChar(int r, int c) {
        var v = 0;
        if (r > 0 && board[r - 1][c] == WALL) {
            v |= 0b0001; // north
        }
        if (c > 0 && board[r][c - 1] == WALL) {
            v |= 0b0010; // west
        }
        if (r < board.length - 1 && board[r + 1][c] == WALL) {
            v |= 0b0100; // south
        }
        if (c < board[r].length - 1 && board[r][c + 1] == WALL) {
            v |= 0b1000; // east
        }
        return switch (v) {
            case 0b0000 -> '·';
            case 0b0011 -> '┘';
            case 0b0101 -> '│';
            case 0b0110 -> '┐';
            case 0b0111 -> '┤';
            case 0b1001 -> '└';
            case 0b1010 -> '─';
            case 0b1011 -> '┴';
            case 0b1100 -> '┌';
            case 0b1101 -> '├';
            case 0b1110 -> '┬';
            case 0b1111 -> '┼';
            default -> '@';
        };
    }

    @Override
    public String toString() {
        return toString(1);
    }

}
