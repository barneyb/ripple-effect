package com.barneyb.games.ripple;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static void assertSolution(Board board, int[] solution) {
        if (!board.isSolved()) {
            System.out.println(board.toString(5));
            throw new AssertionError("Board was not solved?!");
        }
        for (int c = board.cellCount() - 1; c >= 0; c--) {
            assertEquals(solution[c],
                         board.getCell(c),
                         "Cell " + c + " is incorrect");
        }
    }

}
