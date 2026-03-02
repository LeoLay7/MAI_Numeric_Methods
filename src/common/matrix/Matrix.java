package common.matrix;

public class Matrix {
    private MatrixElement[][] matrix;
    private MatrixSize size;

    public Matrix(MatrixElement[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, matrix[i].length);
        }
    }

    public Matrix(int i, int j) {
        this.matrix = new MatrixElement[i][j];
        setSize(i, j);
        zeroMatrix();
    }

    public Matrix(MatrixSize size) {
        this.matrix = new MatrixElement[size.getRows()][size.getCols()];
        this.size = size;
        zeroMatrix();
    }

    public void set(int i, int j, MatrixElement value) {
        this.matrix[i][j] = value;
    }

    public void set(int i, int j, double value) {
        this.matrix[i][j] = MatrixElement.of(value);
    }

    public MatrixElement get(int i, int j) {
        return this.matrix[i][j];
    }

    public int getInt(int i, int j) {
        return get(i, j).toInt();
    }

    public double getDouble(int i, int j) {
        return get(i, j).toDouble();
    }

    private void setSize(int i, int j) {
        this.size = new MatrixSize(i, j);
    }

    public MatrixSize getSize() {
        return this.size;
    }

    public void setRow(int row, MatrixElement[] values) {
        matrix[row] = values;
    }

    public void zeroMatrix() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                set(row, col, 0);
            }
        }
    }
}
