package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static com.barneyb.games.ripple.Assertions.assertSolution;
import static com.barneyb.games.ripple.Boards.BITTY;
import static com.barneyb.games.ripple.Boards.GRANT_FIKES_101;
import static com.barneyb.games.ripple.Boards.SOLUTION_BITTY;
import static com.barneyb.games.ripple.Boards.SOLUTION_GRANT_FIKES_101;

class ByChocoTest {

    @Test
    void bitty() {
        var board = Board.parse(BITTY, 1);
        new ByChoco(board).solve();
        assertSolution(board, SOLUTION_BITTY);
    }

    @Test
    void fikes() {
        var board = Board.parse(GRANT_FIKES_101, 5);
        new ByChoco(board).solve();
        assertSolution(board, SOLUTION_GRANT_FIKES_101);
    }

}
