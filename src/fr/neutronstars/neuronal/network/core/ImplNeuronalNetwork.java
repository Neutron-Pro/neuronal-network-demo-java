package fr.neutronstars.neuronal.network.core;

import fr.neutronstars.neuronal.network.api.Matrix;
import fr.neutronstars.neuronal.network.api.NeuronalNetwork;
import fr.neutronstars.neuronal.network.api.exception.MatrixException;

public class ImplNeuronalNetwork implements NeuronalNetwork {

    public static NeuronalNetwork create(int[] neuronalCount) throws MatrixException
    {
        final Matrix[] matrices = new Matrix[neuronalCount.length - 1];
        for (int x = 0; x < matrices.length; x++) {
            matrices[x] = ImplMatrix.random(neuronalCount[x], neuronalCount[x + 1]);
        }
        return new ImplNeuronalNetwork(neuronalCount, matrices);
    }

    public static NeuronalNetwork create(int[] neuronalCount, Matrix[] weightMatrices)
    {
        return new ImplNeuronalNetwork(neuronalCount, weightMatrices);
    }

    private final int[] neuronalCount;
    private final Matrix[] weightSynapse;
    private final Matrix[] lastForwardResult;

    private ImplNeuronalNetwork(int[] neuronalCount, Matrix[] weightMatrices)
    {
        this.neuronalCount = neuronalCount;
        this.weightSynapse = weightMatrices;
        this.lastForwardResult = new Matrix[neuronalCount.length - 2];
    }

    public Matrix predicate(Matrix matrix) throws MatrixException
    {
        Matrix resultMatrix = matrix;
        int x = 0;
        for (Matrix weighMatrix : this.weightSynapse) {
            resultMatrix = resultMatrix.dot(weighMatrix).sigmoid();
            if (x < this.lastForwardResult.length) {
                this.lastForwardResult[x] = resultMatrix;
                x++;
            }
        }
        return resultMatrix;
    }

    private void learn(Matrix input, Matrix realOutput, Matrix output) throws MatrixException
    {
        Matrix[] delta = new Matrix[this.weightSynapse.length + 1];
        delta[delta.length - 1] = realOutput.subtract(output).multiply(output.reverseSigmoid());

        for (int x = delta.length - 2; x > 0; x--) {
            delta[x] = delta[x + 1].dot(this.weightSynapse[x].flip())
                .multiply(this.lastForwardResult[x - 1].reverseSigmoid());
        }

        for (int x = 0; x < this.weightSynapse.length; x++) {
            if (x == 0) {
                this.weightSynapse[x] = this.weightSynapse[x].addition(input.flip().dot(delta[x + 1]));
                continue;
            }
            this.weightSynapse[x] = this.weightSynapse[x]
                .addition(this.lastForwardResult[x-1].flip().dot(delta[x + 1]));
        }
    }

    public void training(Matrix input, Matrix output, int iterate) throws MatrixException
    {
        for (int i = 0; i < iterate; i++) {
            this.learn(input, output, this.predicate(input));
        }
    }
}
