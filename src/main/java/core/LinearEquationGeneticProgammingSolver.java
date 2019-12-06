package core;

import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.prog.regression.Sample;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

import java.util.Arrays;

public class LinearEquationGeneticProgammingSolver implements IPolynomialEquationGeneticProgrammingResolver {

    @Override
    public ISeq<Op<Double>> operations() {
        return ISeq.of(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV);
    }

    @Override
    public Double fitnessThreshold() {
        return 0.001;
    }

    @Override
    public Sample<Double>[] samples() {
        int xSpace = 10;
        int aSpace = 10;

        Sample[] result = new Sample[xSpace * aSpace];

        for (int i = 0; i < xSpace; i++) {
            for (int j = 0; j < aSpace; j++) {
                double x = i;
                double a = j + 1;
                double b = -a * x;

                result[i * aSpace + j] = Sample.ofDouble(a, b, x);
            }
        }

        System.out.println("(a, b, x): " + Arrays.toString(result));

        return result;
    }

    @Override
    public Integer treeDepth() {
        return 2;
    }

    @Override
    public ISeq<Op<Double>> terminals() {
        return ISeq.of(Var.of("a", 0), Var.of("b", 1),
                EphemeralConst.of(() ->
                        (double) RandomRegistry.getRandom().nextInt(10))
        );
    }
}
