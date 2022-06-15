package fr.neutronstars.test;

import fr.neutronstars.neuronal.network.api.Matrix;
import fr.neutronstars.neuronal.network.api.NeuronalNetwork;
import fr.neutronstars.neuronal.network.api.exception.MatrixException;
import fr.neutronstars.neuronal.network.core.ImplMatrix;
import fr.neutronstars.neuronal.network.core.ImplNeuronalNetwork;

public class TestApplication
{
    public static void main(String[] args) throws MatrixException
    {
        final Matrix dataSet = ImplMatrix.create(
            new float[][]{
                {25f,         2.5f},
                {50f,         30f},
                {40f,         7f},
                {45f,         4.5f},
                {27f,         12f},
                {12f,         1.2f},
                {75f,         7f},
                {10.5f,       1.05f},
                {35f,         25f},
                {84f,         8.4f},
                {61f,         10f},
                {264f,        26.4f},
                {41f,         9.7f}
            }
        ).toRate();

        final Matrix dataSetResult = ImplMatrix.create(
            new float[][] {
                {1},
                {0},
                {0},
                {1},
                {0},
                {1},
                {0},
                {1},
                {0},
                {1},
                {0},
            }
        );

        final NeuronalNetwork neuronalNetwork = ImplNeuronalNetwork.create(new int[]{2, 4, 1});
        System.out.println("### Prediction before training: ");

        printPredicate(neuronalNetwork.predicate(dataSet.copy(11, 1)), 264f, 26.4f);
        printPredicate(neuronalNetwork.predicate(dataSet.copy(12, 1)), 41f, 9.7f);

        System.out.println("### Training with 1 000 000 iterations");
        neuronalNetwork.training(dataSet.copy(0, 11), dataSetResult, 1000000);

        System.out.println("### Prediction before training: ");
        printPredicate(neuronalNetwork.predicate(dataSet.copy(11, 1)), 264f, 26.4f);
        printPredicate(neuronalNetwork.predicate(dataSet.copy(12, 1)), 41f, 9.7f);
    }

    private static void printPredicate(Matrix matrix, float x, float y)
    {
        float predictionRate = Math.round(matrix.of(0, 0) * 10000.f) / 100.0f;
        System.out.println(
            "Prediction Rate (< 50%): " + predictionRate + "% |> " + x + " / 10 = " + y + " >> "
                + (predictionRate > 50f
                ? "The prediction is true !"
                : "The prediction is false !")
        );
    }
}
