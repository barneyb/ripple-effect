package com.barneyb.games.ripple;

import java.io.PrintStream;

public class ByChoco implements Ripple {

    private final PrintStream out;
    private final Board board;

    public ByChoco(Board board) {
        this(System.out, board);
    }

    public ByChoco(PrintStream out, Board board) {
        this.out = out;
        this.board = board;
    }

    @Override
    public void solve() {
        out.println("... doing nothing. Yet!");
    }

    @Override
    public void printState() {
    }

}
