package core;

import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.prog.regression.Sample;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

import java.util.Arrays;

public class SquareEquationGeneticProgrammingSolver implements IPolynomialEquationGeneticProgrammingResolver {
    @Override
    public ISeq<Op<Double>> operations() {
        return ISeq.of(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.SQRT);
    }

    @Override
    public ISeq<Op<Double>> terminals() {
        return ISeq.of(
                Var.of("a", 0),
                Var.of("b", 1),
                Var.of("c", 2),
                EphemeralConst.of(() -> (double) RandomRegistry.getRandom().nextInt(10)));
    }

    @Override
    public Sample<Double>[] samples() {
        int xSpace = 3;
        int aSpace = 3;
        int bSpace = 3;

        Sample[] result = new Sample[xSpace * aSpace * bSpace];

        for (int i = 0; i < xSpace; i++) {
            for (int j = 0; j < aSpace; j++) {
                for (int k = 0; k < bSpace; k++) {
                    double x = i;
                    double a = j + 1;
                    double b = k;
                    double c = - (a * x * x + b * x);

                    int index = i * aSpace * bSpace + j * bSpace  + k;

                    result[index] = Sample.ofDouble(a, b, c, x);
                }
            }
        }

        System.out.println("(a, b, c, x): " + Arrays.toString(result));

        return result;
    }

    @Override
    public Double fitnessThreshold() {
        return 0.1;
    }

    @Override
    public Integer treeDepth() {
        return 6;
    }
}
