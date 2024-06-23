package com.barneyb.games.ripple;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static void assertSolution(Board board, int[] solution) {
        try {
            if (!board.isSolved()) {
                throw new AssertionError("Board was not solved?!");
            }
            for (int c = board.cellCount() - 1; c >= 0; c--) {
                assertEquals(solution[c],
                             board.getCell(c),
                             "Cell " + c + " is incorrect");
            }
        } catch (AssertionError ae) {
            System.out.println(board.toString(5));
            throw ae;
        }
    }

}
