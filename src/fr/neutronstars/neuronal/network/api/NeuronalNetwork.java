package fr.neutronstars.neuronal.network.api;

import fr.neutronstars.neuronal.network.api.exception.MatrixException;

public interface NeuronalNetwork
{
    Matrix predicate(Matrix matrix) throws MatrixException;

    void training(Matrix input, Matrix output, int iterate) throws MatrixException;
}
