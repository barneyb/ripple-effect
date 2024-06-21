package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static com.barneyb.games.ripple.Assertions.assertSolution;
import static com.barneyb.games.ripple.Boards.BITTY;
import static com.barneyb.games.ripple.Boards.KD_CHALLENGING_8x8_v1_b1_1;
import static com.barneyb.games.ripple.Boards.KD_EASY_7x7_v1_b1_1;
import static com.barneyb.games.ripple.Boards.KD_SUPER_TOUGH_10x10_v1_b100_2;
import static com.barneyb.games.ripple.Boards.KD_SUPER_TOUGH_10x10_v1_b1_1;
import static com.barneyb.games.ripple.Boards.KD_TOUGH_8x8_v1_b9_7;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ByLogicTest {

    @Test
    void visibleToAllPotentials() {
        var board = Board.parse(KD_TOUGH_8x8_v1_b9_7,
                                5);
        var r = new ByLogic(board);
        assertTrue(r.singleCandidate());
        assertEquals(Board.OPEN, board.getCell(53));
        System.out.println(board.toString(5));
        assertTrue(r.visibleToAllPotentials());
        assertTrue(r.singleCandidate());
        assertEquals(3, board.getCell(53));
        System.out.println(board.toString(5));
    }

    @Test
    void bitty() {
        var board = Board.parse(BITTY, 1);
        new ByLogic(board).solve();
        var solution = new int[]{
                1, 2, 1,
                3, 1, 2, };
        assertSolution(board, solution);
    }

    @Test
    void easy_7x7_v1_b1_1() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
        new ByLogic(board).solve();
        var solution = new int[]{
                2, 1, 3, 1, 4, 2, 1,
                1, 3, 1, 2, 1, 3, 2,
                3, 1, 2, 1, 3, 5, 1,
                1, 2, 4, 5, 6, 2, 3,
                2, 1, 3, 1, 5, 4, 1,
                5, 3, 1, 2, 1, 3, 2,
                3, 4, 2, 1, 3, 2, 1, };
        assertSolution(board, solution);
    }

    @Test
    void challenging_8x8_v1_b1_1() {
        var board = Board.parse(KD_CHALLENGING_8x8_v1_b1_1, 5);
        new ByLogic(board).solve();
        var solution = new int[]{
                2, 3, 1, 2, 4, 5, 2, 1,
                6, 1, 3, 1, 2, 4, 3, 2,
                4, 2, 1, 3, 1, 2, 1, 3,
                3, 5, 2, 4, 3, 1, 2, 1,
                1, 3, 1, 2, 1, 3, 4, 5,
                2, 1, 3, 1, 4, 2, 3, 1,
                1, 2, 4, 3, 2, 5, 1, 3,
                3, 4, 1, 2, 3, 1, 2, 1, };
        assertSolution(board, solution);
    }

    @Test
    void tough_8x8_v1_b9_7() {
        var board = Board.parse(KD_TOUGH_8x8_v1_b9_7, 5);
        new ByLogic(board).solve();
        var solution = new int[]{
                3, 4, 2, 1, 3, 1, 2, 1,
                1, 2, 1, 3, 2, 4, 5, 2,
                2, 3, 4, 5, 1, 3, 6, 4,
                4, 1, 3, 2, 5, 1, 3, 1,
                5, 2, 1, 4, 1, 7, 2, 3,
                1, 4, 2, 1, 3, 2, 4, 1,
                2, 3, 5, 2, 1, 3, 1, 2,
                1, 2, 3, 1, 2, 4, 3, 1, };
        assertSolution(board, solution);
    }

    @Test
    void super_tough_10x10_v1_b1_1() {
        var board = Board.parse(KD_SUPER_TOUGH_10x10_v1_b1_1, 5);
        new ByLogic(board).solve();
        var solution = new int[]{
                3, 1, 2, 1, 5, 4, 3, 6, 1, 2,
                1, 5, 4, 2, 1, 3, 1, 2, 4, 3,
                2, 1, 6, 1, 3, 1, 2, 7, 1, 5,
                4, 6, 2, 3, 1, 2, 5, 1, 2, 1,
                1, 3, 5, 1, 2, 1, 4, 3, 1, 2,
                3, 2, 1, 4, 1, 3, 2, 1, 5, 1,
                2, 1, 4, 1, 3, 7, 6, 2, 3, 4,
                1, 4, 1, 3, 1, 2, 1, 5, 4, 2,
                4, 1, 3, 2, 5, 1, 2, 4, 1, 3,
                1, 2, 6, 1, 2, 4, 3, 1, 2, 1, };
        assertSolution(board, solution);
    }

    @Test
    void super_tough_10x10_v1_b100_2() {
        var board = Board.parse(KD_SUPER_TOUGH_10x10_v1_b100_2, 5);
        new ByLogic(board).solve();
        var solution = new int[]{
                4, 6, 1, 5, 3, 4, 1, 2, 3, 5,
                5, 2, 6, 4, 2, 1, 5, 3, 7, 6,
                2, 5, 3, 1, 6, 2, 4, 5, 2, 3,
                3, 4, 1, 2, 1, 3, 1, 6, 4, 2,
                1, 3, 5, 6, 4, 1, 3, 1, 5, 4,
                4, 1, 2, 3, 5, 6, 1, 2, 3, 1,
                6, 2, 3, 5, 1, 4, 2, 3, 6, 5,
                5, 6, 4, 2, 3, 1, 5, 4, 2, 1,
                2, 1, 6, 4, 2, 5, 3, 1, 4, 6,
                3, 5, 2, 1, 4, 2, 6, 5, 3, 4, };
        assertSolution(board, solution);
    }

}