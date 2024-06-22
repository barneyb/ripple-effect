package com.barneyb.games.ripple;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.PrintStream;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class ByChoco extends BaseRipple {

    public ByChoco(Board board) {
        this(System.out, board);
    }

    public ByChoco(PrintStream out, Board board) {
        super(out, board);
    }

    @Override
    void solveInternal() {
        var model = new Model();
        var cellVars = initialize(model);
        doSolve(model, cellVars);
    }

    private IntVar[] initialize(Model model) {
        var cellVars = new IntVar[board.cellCount()];
        for (var cage : board.cages()) {
            var cageVars = new IntVar[cage.length];
            for (int i = 0; i < cage.length; i++) {
                var c = cage[i];
                cageVars[i] = cellVars[c] = board.isOpen(c)
                        ? model.intVar("c" + c, 1, cage.length)
                        : model.intVar("c" + c, board.getCell(c));
            }
            model.allDifferent(cageVars).post();
        }

        for (var cage : board.cages()) {
            for (var c : cage) {
                upto(cage.length).forEach(v -> {
                    IntConsumer nope = n -> model.ifThen(
                            model.arithm(cellVars[c], "=", v),
                            model.arithm(cellVars[n], "!=", v));
                    board.northOf(c).limit(v).forEach(nope);
                    board.southOf(c).limit(v).forEach(nope);
                    board.eastOf(c).limit(v).forEach(nope);
                    board.westOf(c).limit(v).forEach(nope);
                });
            }
        }
        return cellVars;
    }

    private IntStream upto(int n) {
        return IntStream.range(1, n + 1);
    }

    private void doSolve(Model model, IntVar[] cellVars) {
        var solver = model.getSolver();
        if (solver.solve()) {
            for (int i = 0; i < cellVars.length; i++) {
                var v = cellVars[i];
                if (v.isInstantiated()) {
                    board.setCell(i, v.getValue());
                }
            }
            if (solver.solve()) {
                out.println("Found multiple solutions, not THE solution");
                out.println(board.toString(5));
            }
        }
    }

    @Override
    public void printState() {
    }

}
