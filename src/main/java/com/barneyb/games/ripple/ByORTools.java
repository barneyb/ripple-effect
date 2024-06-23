package com.barneyb.games.ripple;

import com.google.ortools.Loader;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class ByORTools extends BaseRipple {

    static {
        Loader.loadNativeLibraries();
    }

    public ByORTools(Board board) {
        this(System.out, board);
    }

    public ByORTools(PrintStream out, Board board) {
        super(out, board);
    }

    @Override
    void solveInternal() {
        var model = new CpModel();
        var cellVars = initialize(model);
        doSolve(model, cellVars);
    }

    private IntVar[] initialize(CpModel model) {
        var cellVars = new IntVar[board.cellCount()];
        for (var cage : board.cages()) {
            var cageVars = new IntVar[cage.length];
            int i = 0;
            for (var c : cage) {
                cageVars[i++] = cellVars[c] = board.isOpen(c)
                        ? model.newIntVar(1, cage.length, "" + c)
                        : model.newConstant(board.getCell(c));
            }
            model.addAllDifferent(cageVars);
        }

        var rb = new AtomicInteger();
        for (var cage : board.cages()) {
            for (int c : cage) {
                upto(cage.length).forEach(v -> {
                    IntConsumer nope = n -> {
                        BoolVar b = model.newBoolVar("b" + (rb.getAndIncrement()));
                        // Implement b == (c == v).
                        model.addEquality(cellVars[c], v).onlyEnforceIf(b);
                        model.addDifferent(cellVars[c], v).onlyEnforceIf(b.not());
                        // Create the half-reified constraint: b implies n != v
                        model.addDifferent(cellVars[n], v).onlyEnforceIf(b);
                    };
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

    private void doSolve(CpModel model, IntVar[] cellVars) {
        var solver = new CpSolver();
        out.println(model.modelStats());
        var status = solver.solve(model);
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            for (int c = 0; c < cellVars.length; c++) {
                IntVar v = cellVars[c];
                out.printf("  c%s = %d", c, solver.value(v));
                try {
                    board.setCell(c, (int) solver.value(v));
                } catch (Throwable t) {t.printStackTrace(System.err);}
            }
            out.println();
        } else {
            out.println("No solution found: " + status);
        }
    }

    @Override
    public void printState() {
    }

}
