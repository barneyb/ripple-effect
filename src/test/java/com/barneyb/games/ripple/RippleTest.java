package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static com.barneyb.games.ripple.Boards.BITTY;
import static com.barneyb.games.ripple.Boards.CHALLENGING_8x8_v1_b1_1;
import static com.barneyb.games.ripple.Boards.EASY_7x7_v1_b1_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RippleTest {

    @Test
    void bitty() {
        var board = Board.parse(BITTY, 1);
        new Ripple(board);
        var solution = new int[]{
                1, 2, 1,
                3, 1, 2
        };
        assertSolution(board, solution);
    }

    @Test
    void easy_7x7_v1_b1_1() {
        var board = Board.parse(EASY_7x7_v1_b1_1, 5);
        new Ripple(board);
        var solution = new int[]{
                2, 1, 3, 1, 4, 2, 1,
                1, 3, 1, 2, 1, 3, 2,
                3, 1, 2, 1, 3, 5, 1,
                1, 2, 4, 5, 6, 2, 3,
                2, 1, 3, 1, 5, 4, 1,
                5, 3, 1, 2, 1, 3, 2,
                3, 4, 2, 1, 3, 2, 1 };
        assertSolution(board, solution);
    }

    @Test
    void challenging_8x8_v1_b1_1() {
        var board = Board.parse(CHALLENGING_8x8_v1_b1_1, 5);
        new Ripple(board);
        var solution = new int[]{
                2, 3, 1, 2, 4, 5, 2, 1,
                6, 1, 3, 1, 2, 4, 3, 2,
                4, 2, 1, 3, 1, 2, 1, 3,
                3, 5, 2, 4, 3, 1, 2, 1,
                1, 3, 1, 2, 1, 3, 4, 5,
                2, 1, 3, 1, 4, 2, 3, 1,
                1, 2, 4, 3, 2, 5, 1, 3,
                3, 4, 1, 2, 3, 1, 2, 1 };
        assertSolution(board, solution);
    }

    private static void assertSolution(Board board, int[] solution) {
        for (int c = board.cellCount() - 1; c >= 0; c--) {
            assertEquals(solution[c],
                         board.getCell(c),
                         "Cell " + c + " is incorrect");
        }
    }

}
