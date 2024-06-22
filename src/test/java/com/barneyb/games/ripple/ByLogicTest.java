package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static com.barneyb.games.ripple.Assertions.assertSolution;
import static com.barneyb.games.ripple.Boards.BITTY;
import static com.barneyb.games.ripple.Boards.KD_CHALLENGING_8x8_v1_b1_1;
import static com.barneyb.games.ripple.Boards.KD_EASY_7x7_v1_b1_1;
import static com.barneyb.games.ripple.Boards.KD_SUPER_TOUGH_10x10_v1_b100_2;
import static com.barneyb.games.ripple.Boards.KD_SUPER_TOUGH_10x10_v1_b1_1;
import static com.barneyb.games.ripple.Boards.KD_TOUGH_8x8_v1_b9_7;
import static com.barneyb.games.ripple.Boards.SOLUTION_BITTY;
import static com.barneyb.games.ripple.Boards.SOLUTION_KD_CHALLENGING_8x8_v1_b1_1;
import static com.barneyb.games.ripple.Boards.SOLUTION_KD_EASY_7x7_v1_b1_1;
import static com.barneyb.games.ripple.Boards.SOLUTION_KD_SUPER_TOUGH_10x10_v1_b100_2;
import static com.barneyb.games.ripple.Boards.SOLUTION_KD_SUPER_TOUGH_10x10_v1_b1_1;
import static com.barneyb.games.ripple.Boards.SOLUTION_KD_TOUGH_8x8_v1_b9_7;
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
        assertSolution(board, SOLUTION_BITTY);
    }

    @Test
    void easy_7x7_v1_b1_1() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
        new ByLogic(board).solve();
        assertSolution(board, SOLUTION_KD_EASY_7x7_v1_b1_1);
    }

    @Test
    void challenging_8x8_v1_b1_1() {
        var board = Board.parse(KD_CHALLENGING_8x8_v1_b1_1, 5);
        new ByLogic(board).solve();
        assertSolution(board, SOLUTION_KD_CHALLENGING_8x8_v1_b1_1);
    }

    @Test
    void tough_8x8_v1_b9_7() {
        var board = Board.parse(KD_TOUGH_8x8_v1_b9_7, 5);
        new ByLogic(board).solve();
        assertSolution(board, SOLUTION_KD_TOUGH_8x8_v1_b9_7);
    }

    @Test
    void super_tough_10x10_v1_b1_1() {
        var board = Board.parse(KD_SUPER_TOUGH_10x10_v1_b1_1, 5);
        new ByLogic(board).solve();
        assertSolution(board, SOLUTION_KD_SUPER_TOUGH_10x10_v1_b1_1);
    }

    @Test
    void super_tough_10x10_v1_b100_2() {
        var board = Board.parse(KD_SUPER_TOUGH_10x10_v1_b100_2, 5);
        new ByLogic(board).solve();
        assertSolution(board, SOLUTION_KD_SUPER_TOUGH_10x10_v1_b100_2);
    }

}
