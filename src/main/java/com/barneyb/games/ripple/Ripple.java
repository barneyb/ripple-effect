package com.barneyb.games.ripple;

public class Ripple {

    public static void main(String[] args) {
        var board = Board.parse("""
                                ┌───┬───┬───────┬───────┬───┐
                                │   │   │       │       │   │
                                │   ├───┴───┐   │   ┌───┴───┤
                                │   │       │   │   │       │
                                ├───┴───┐   ├───┤   └───┐   │
                                │       │   │   │     5 │   │
                                ├───┐   └───┴───┤   ┌───┴───┤
                                │   │     4     │ 6 │       │
                                ├───┴───┬───────┼───┴───┐   │
                                │       │       │       │ 1 │
                                ├───┐   ├───┐   │   ┌───┴───┤
                                │   │   │ 1 │   │   │       │
                                │   └───┘   ├───┤   └───┐   │
                                │     4     │   │       │   │
                                └───────────┴───┴───────┴───┘
                                """, 3);
        String five = board.toString(5);
        System.out.println(board);
        System.out.println(five);
        assert five.equals(Board.parse(five, 5).toString(5));
    }

}
