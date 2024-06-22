package com.barneyb.games.ripple;

import java.io.PrintStream;

abstract class BaseRipple implements Ripple {

    final PrintStream out;
    final Board board;

    BaseRipple(Board board) {
        this(System.out, board);
    }

    BaseRipple(PrintStream out, Board board) {
        this.out = out;
        this.board = board;
    }

    @Override
    public void solve() {
        var start = System.currentTimeMillis();
        solveInternal();
        var elapsed = System.currentTimeMillis() - start;
        out.printf("[[ %s in %d ms ]]%n",
                   board.isSolved() ? "Solved" : "Terminated",
                   elapsed);
    }

    abstract void solveInternal();

}
