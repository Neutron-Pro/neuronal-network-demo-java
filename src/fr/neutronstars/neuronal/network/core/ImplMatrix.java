package fr.neutronstars.neuronal.network.core;

import fr.neutronstars.neuronal.network.api.Matrix;
import fr.neutronstars.neuronal.network.api.exception.MatrixException;
import fr.neutronstars.neuronal.network.api.exception.MatrixRuntimeException;

import java.util.Random;

public class ImplMatrix implements Matrix
{
    public static Matrix create(int rows, int columns)
    {
        return new ImplMatrix(rows, columns);
    }

    public static Matrix random(int rows, int columns) throws MatrixException
    {
        return ImplMatrix.random(rows, columns, new Random());
    }

    public static Matrix random(int rows, int columns, Random random) throws MatrixException
    {
        float[][] matrix = new float[rows][columns];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                matrix[x][y] = random.nextFloat();
            }
        }
        return ImplMatrix.create(matrix);
    }

    public static Matrix create(float[][] matrix) throws MatrixException {
        final ImplMatrix matrix1 = new ImplMatrix(matrix.length, matrix[0].length);
        for (int x = 0; x < matrix1.rows; x++) {
            if (matrix[x].length != matrix1.columns) {
                throw new MatrixException("All rows must have the same number of columns.");
            }
            System.arraycopy(matrix[x], 0, matrix1.matrix[x], 0, matrix1.columns);
        }
        return matrix1;
    }

    private final float[][] matrix;
    private final int rows;
    private final int columns;

    private ImplMatrix(int rows, int columns)
    {
        this.matrix = new float[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public int getRows()
    {
        return this.rows;
    }

    @Override
    public int getColumns()
    {
        return this.columns;
    }

    @Override
    public float of(int row, int column)
    {
        return this.matrix[row][column];
    }

    @Override
    public float getMax(int axis)
    {
        if (this.columns <= axis) {
            throw new MatrixRuntimeException("The axis must be smaller than the number of columns.");
        }
        float value = this.matrix[0][axis];
        for (int x = 1; x < this.rows; x++) {
            if (value < this.matrix[x][axis]) {
                value = this.matrix[x][axis];
            }
        }
        return value;
    }

    @Override
    public Matrix toRate()
    {
        final ImplMatrix matrix = new ImplMatrix(this.rows, this.columns);
        float[] valueMax = new float[this.columns];
        for (int y = 0; y < this.columns; y++) {
            valueMax[y] = this.getMax(y);
        }
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                matrix.matrix[x][y] = this.matrix[x][y] / valueMax[y];
            }
        }
        return matrix;
    }

    @Override
    public Matrix addition(Matrix matrix)
    {
        if (this.rows != matrix.getRows() || this.columns != matrix.getColumns()) {
            throw new MatrixRuntimeException("The axis must be smaller than the number of columns.");
        }
        final ImplMatrix newMatrix = new ImplMatrix(this.rows, this.columns);
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                newMatrix.matrix[x][y] = this.matrix[x][y] + matrix.of(x, y);
            }
        }
        return newMatrix;
    }

    @Override
    public Matrix subtract(Matrix matrix)
    {
        if (this.rows != matrix.getRows() || this.columns != matrix.getColumns()) {
            throw new MatrixRuntimeException("The axis must be smaller than the number of columns.");
        }
        final ImplMatrix newMatrix = new ImplMatrix(this.rows, this.columns);
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                newMatrix.matrix[x][y] = this.matrix[x][y] - matrix.of(x, y);
            }
        }
        return newMatrix;
    }

    @Override
    public Matrix multiply(Matrix matrix)
    {
        if (this.rows != matrix.getRows() || this.columns != matrix.getColumns()) {
            throw new MatrixRuntimeException("The axis must be smaller than the number of columns.");
        }
        final ImplMatrix newMatrix = new ImplMatrix(this.rows, this.columns);
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                newMatrix.matrix[x][y] = this.matrix[x][y] * matrix.of(x, y);
            }
        }
        return newMatrix;
    }

    @Override
    public Matrix dot(Matrix matrix) throws MatrixException
    {
        if (this.columns != matrix.getRows()) {
            throw new MatrixRuntimeException("Impossible to multiply the matrices.");
        }

        float[][] matrices = new float[this.rows][matrix.getColumns()];
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < matrix.getColumns(); y++) {
                float result = 0;
                for (int z = 0; z < this.columns; z++) {
                    result += (this.matrix[x][z] * matrix.of(z, y));
                }
                matrices[x][y] = result;
            }
        }
        return ImplMatrix.create(matrices);
    }

    @Override
    public Matrix flip()
    {
        final ImplMatrix matrix = new ImplMatrix(this.columns, this.rows);
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                matrix.matrix[y][x] = this.matrix[x][y];
            }
        }
        return matrix;
    }

    @Override
    public Matrix copy(int from, int count) {
        count = Math.min(count, this.rows - from);
        final ImplMatrix matrix = new ImplMatrix(count, this.columns);
        for (int x = 0; x < count; x++) {
            System.arraycopy(this.matrix[from + x], 0, matrix.matrix[x], 0, this.columns);
        }
        return matrix;
    }

    @Override
    public Matrix round(int radius) {
        int multiply = 1;
        for (int i = 0; i < radius; i++) {
            multiply *= 10;
        }
        final ImplMatrix matrix = new ImplMatrix(this.rows, this.columns);
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                matrix.matrix[x][y] = ((float) Math.round(this.matrix[x][y] * multiply)) / multiply;
            }
        }
        return matrix;
    }

    @Override
    public Matrix sigmoid()
    {
        final ImplMatrix matrix = new ImplMatrix(this.rows, this.columns);
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                matrix.matrix[x][y] = 1.0f / (1.0f + (float) Math.exp(-this.matrix[x][y]));
            }
        }
        return matrix;
    }

    @Override
    public Matrix reverseSigmoid()
    {
        final ImplMatrix matrix = new ImplMatrix(this.rows, this.columns);
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < matrix.getColumns(); y++) {
                matrix.matrix[x][y] = 1 - this.matrix[x][y];
            }
        }
        return this.multiply(matrix);
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int x = 0; x < this.rows; x++) {
            builder.append("\n  [");
            for (int y = 0; y < this.columns; y++) {
                if (y > 0) {
                    builder.append(", ");
                }
                builder.append(this.matrix[x][y]);
            }
            builder.append("]");
        }
        return builder.append("\n]").toString();
    }
}
