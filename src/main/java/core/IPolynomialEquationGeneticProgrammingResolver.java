package core;

import io.jenetics.Mutator;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.MathExpr;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.regression.Error;
import io.jenetics.prog.regression.LossFunction;
import io.jenetics.prog.regression.Regression;
import io.jenetics.prog.regression.Sample;
import io.jenetics.util.ISeq;

public interface IPolynomialEquationGeneticProgrammingResolver {

    default TreeNode<Op<Double>> solve() {
        Regression regression = this.regression();

        final Engine<ProgramGene<Double>, Double> engine = Engine
                .builder(regression)
                .minimizing()
                .alterers(
                        new SingleNodeCrossover<>(0.3),
                        new Mutator<>(0.3))
                .build();

        final EvolutionResult<ProgramGene<Double>, Double> result = engine.stream()
                .limit(Limits.byFitnessThreshold(this.fitnessThreshold()))
                .collect(EvolutionResult.toBestEvolutionResult());

        final ProgramGene<Double> program = result.getBestPhenotype()
                .getGenotype()
                .getGene();

        final TreeNode<Op<Double>> tree = program.toTreeNode();
        MathExpr.rewrite(tree);

        System.out.println("Generations: " + result.getTotalGenerations());
        System.out.println("Function:    " + new MathExpr(tree));
        System.out.println("Error:       " + regression.error(tree));

        return tree;
    }

    ISeq<Op<Double>> operations();

    ISeq<Op<Double>> terminals();

    Sample<Double>[] samples();

    Integer treeDepth();

    Double fitnessThreshold();

    default Regression<Double> regression() {
        return Regression.of(
                Regression.codecOf(
                        this.operations(),
                        this.terminals(),
                        this.treeDepth()
                ),

                Error.of(LossFunction::mse),

                this.samples()
        );
    }
}
