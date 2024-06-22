package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static com.barneyb.games.ripple.Assertions.assertSolution;
import static com.barneyb.games.ripple.Boards.GRANT_FIKES_101;
import static com.barneyb.games.ripple.Boards.SOLUTION_GRANT_FIKES_101;

class ByChocoTest extends BaseRippleTest {

    @Override
    ByChoco getRipple(Board board) {
        return new ByChoco(board);
    }

    @Test
    void fikes() {
        var board = Board.parse(GRANT_FIKES_101, 5);
        getRipple(board).solve();
        assertSolution(board, SOLUTION_GRANT_FIKES_101);
    }

}
