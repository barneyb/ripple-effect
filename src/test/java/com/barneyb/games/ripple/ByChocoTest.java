package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static com.barneyb.games.ripple.Assertions.assertSolution;
import static com.barneyb.games.ripple.Boards.BITTY;

class ByChocoTest {

    @Test
    void bitty() {
        var board = Board.parse(BITTY, 1);
        new ByChoco(board).solve();
        var solution = new int[]{
                1, 2, 1,
                3, 1, 2, };
        assertSolution(board, solution);
    }

}
