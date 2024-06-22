package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static com.barneyb.games.ripple.Boards.KD_TOUGH_8x8_v1_b9_7;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ByLogicTest extends BaseRippleTest {

    @Override
    ByLogic getRipple(Board board) {
        return new ByLogic(board);
    }

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

}
