package com.barneyb.games.ripple;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.barneyb.games.ripple.Boards.BITTY;
import static com.barneyb.games.ripple.Boards.KD_EASY_7x7_v1_b1_1;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

    @Test
    void parse_bitty() {
        var board = Board.parse(BITTY, 1);
        assertEquals("""
                     ┌───────┬───┐
                     │       │   │
                     │   ┌───┤   │
                     │   │   │   │
                     └───┴───┴───┘""", board.toString(3));
        assertArrayEquals(new int[][]{
                                  { 0, 1, 3 },
                                  { 2, 5 },
                                  { 4 } },
                          board.cages());
        for (int i = 0; i < 6; i++)
            assertEquals(Board.OPEN,
                         board.getCell(i));
    }

    @Test
    void parse_easy() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
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
        Map<Integer, Integer> values = Map.of(
                19, 5,
                23, 4,
                25, 6,
                34, 1,
                37, 1,
                43, 4);
        for (int i = 0; i < 49; i++)
            assertEquals(values.getOrDefault(i, Board.OPEN),
                         board.getCell(i));
    }

    @Test
    void setCell() {
        var board = Board.parse(BITTY, 1);
        assertEquals(Board.OPEN,
                     board.getCell(4));
        board.setCell(4, 1);
        assertEquals(1,
                     board.getCell(4));
    }

    @Test
    void northOf() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
        assertEquals(List.of(35, 28, 21, 14, 7, 0), list(board.northOf(42)));
        assertEquals(List.of(), list(board.northOf(0)));
        assertEquals(List.of(), list(board.northOf(3)));
        assertEquals(List.of(), list(board.northOf(6)));
        assertEquals(List.of(0), list(board.northOf(7)));
        assertEquals(List.of(3), list(board.northOf(10)));
        assertEquals(List.of(6), list(board.northOf(13)));
        assertEquals(List.of(7, 0), list(board.northOf(14)));
        assertEquals(List.of(10, 3), list(board.northOf(17)));
        assertEquals(List.of(13, 6), list(board.northOf(20)));
    }

    @Test
    void southOf() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
        assertEquals(List.of(7, 14, 21, 28, 35, 42), list(board.southOf(0)));
        assertEquals(List.of(42), list(board.southOf(35)));
        assertEquals(List.of(45), list(board.southOf(38)));
        assertEquals(List.of(48), list(board.southOf(41)));
        assertEquals(List.of(), list(board.southOf(42)));
        assertEquals(List.of(), list(board.southOf(45)));
        assertEquals(List.of(), list(board.southOf(48)));
    }

    @Test
    void eastOf() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
        assertEquals(List.of(1, 2, 3, 4, 5, 6), list(board.eastOf(0)));
        assertEquals(List.of(), list(board.eastOf(6)));
        assertEquals(List.of(12, 13), list(board.eastOf(11)));
    }

    @Test
    void westOf() {
        var board = Board.parse(KD_EASY_7x7_v1_b1_1, 5);
        assertEquals(List.of(5, 4, 3, 2, 1, 0), list(board.westOf(6)));
        assertEquals(List.of(), list(board.westOf(0)));
        assertEquals(List.of(8, 7), list(board.westOf(9)));
    }

    private List<Integer> list(IntStream stream) {
        return stream.boxed().toList();
    }

}
