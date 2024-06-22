package com.barneyb.games.ripple;

import static com.barneyb.games.ripple.Boards.GRANT_FIKES_101;

public interface Ripple {

    static void main(String[] args) {
        var board = Board.parse(GRANT_FIKES_101,
                                5);
        Ripple r = new ByChoco(System.out, board);
        r.solve();
        if (!board.isSolved()) {
            r.printState();
        }
        System.out.println(board.toString(5));
    }

    void solve();

    void printState();

}
