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

public abstract class BaseRippleTest {

    abstract Ripple getRipple(Board board);

    @Test
    void bitty() {
        var board = Board.parse(BITTY, 1);
        getRipple(board).solve();
        assertSolution(board, SOLUTION_BITTY);
    }

    @Test
    void easy_7x7_v1_b1_1() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
        getRipple(board).solve();
        assertSolution(board, SOLUTION_KD_EASY_7x7_v1_b1_1);
    }

    @Test
    void challenging_8x8_v1_b1_1() {
        var board = Board.parse(KD_CHALLENGING_8x8_v1_b1_1, 5);
        getRipple(board).solve();
        assertSolution(board, SOLUTION_KD_CHALLENGING_8x8_v1_b1_1);
    }

    @Test
    void tough_8x8_v1_b9_7() {
        var board = Board.parse(KD_TOUGH_8x8_v1_b9_7, 5);
        getRipple(board).solve();
        assertSolution(board, SOLUTION_KD_TOUGH_8x8_v1_b9_7);
    }

    @Test
    void super_tough_10x10_v1_b1_1() {
        var board = Board.parse(KD_SUPER_TOUGH_10x10_v1_b1_1, 5);
        getRipple(board).solve();
        assertSolution(board, SOLUTION_KD_SUPER_TOUGH_10x10_v1_b1_1);
    }

    @Test
    void super_tough_10x10_v1_b100_2() {
        var board = Board.parse(KD_SUPER_TOUGH_10x10_v1_b100_2, 5);
        getRipple(board).solve();
        assertSolution(board, SOLUTION_KD_SUPER_TOUGH_10x10_v1_b100_2);
    }

}
