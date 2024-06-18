package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

    private static final String BITTY = """
                                        +-+-+-+
                                        |   | |
                                        + +-+ +
                                        | | | |
                                        +-+-+-+""";

    private static final String EASY = """
                                       ┌─────┬─────┬───────────┬───────────┬─────┐
                                       │     │     │           │           │     │
                                       │     ├─────┴─────┐     │     ┌─────┴─────┤
                                       │     │           │     │     │           │
                                       ├─────┴─────┐     ├─────┤     └─────┐     │
                                       │           │     │     │        5  │     │
                                       ├─────┐     └─────┴─────┤     ┌─────┴─────┤
                                       │     │        4        │  6  │           │
                                       ├─────┴─────┬───────────┼─────┴─────┐     │
                                       │           │           │           │  1  │
                                       ├─────┐     ├─────┐     │     ┌─────┴─────┤
                                       │     │     │  1  │     │     │           │
                                       │     └─────┘     ├─────┤     └─────┐     │
                                       │        4        │     │           │     │
                                       └─────────────────┴─────┴───────────┴─────┘""";

    @Test
    void parse_bitty() {
        var board = Board.parse(BITTY);
        assertEquals(BITTY, board.toString());
        assertArrayEquals(new int[][]{
                                  { 0, 1, 3 },
                                  { 2, 5 },
                                  { 4 } },
                          board.cages());
    }

    @Test
    void parse_easy() {
        var board = Board.parse(EASY, 5);
        String three = board.toString(3);
        assertEquals(three, Board.parse(three, 3).toString(3));
        assertArrayEquals(new int[][]{
                                  { 0, 7 },
                                  { 1 },
                                  { 2, 3, 10 },
                                  { 4, 5, 11, 18, 19, 25 },
                                  { 6 },
                                  { 8, 9, 16 },
                                  { 12, 13, 20 },
                                  { 14, 15, 22, 23, 24 },
                                  { 17 },
                                  { 21 },
                                  { 26, 27, 34 },
                                  { 28, 29, 36 },
                                  { 30, 31, 38 },
                                  { 32, 33, 39, 46, 47 },
                                  { 35, 42, 43, 44, 37 },
                                  { 40, 41, 48 },
                                  { 45 } },
                          board.cages());
    }

}
