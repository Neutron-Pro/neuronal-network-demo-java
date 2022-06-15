package fr.neutronstars.neuronal.network.api;

import fr.neutronstars.neuronal.network.api.exception.MatrixException;

public interface Matrix
{
    int getRows();

    int getColumns();

    float of(int row, int column);

    float getMax(int axis);

    Matrix toRate();

    Matrix addition(Matrix matrix);

    Matrix subtract(Matrix matrix);

    Matrix multiply(Matrix matrix);

    Matrix dot(Matrix matrix) throws MatrixException;

    Matrix flip();

    Matrix sigmoid();

    Matrix reverseSigmoid();

    Matrix copy(int from, int count);

    Matrix round(int radius);
}
